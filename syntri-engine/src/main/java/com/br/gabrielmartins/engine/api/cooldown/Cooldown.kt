package com.br.gabrielmartins.engine.api.cooldown

import net.md_5.bungee.api.ChatMessageType
import net.md_5.bungee.api.chat.TextComponent
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.entity.Player
import org.bukkit.plugin.java.JavaPlugin
import org.bukkit.scheduler.BukkitRunnable
import java.util.*

object Cooldown {

    private val cooldowns = mutableMapOf<UUID, Long>()

    private val version: String = runCatching {
        Bukkit.getServer().javaClass.packageName.split(".").getOrNull(3) ?: "UNKNOWN"
    }.getOrElse { "UNKNOWN" }

    private val majorVersion: Int = parseVersion(version)

    fun start(plugin: JavaPlugin, player: Player, seconds: Int, messagePrefix: String) {
        val uuid = player.uniqueId
        val endTime = System.currentTimeMillis() + (seconds * 1000L)
        cooldowns[uuid] = endTime

        object : BukkitRunnable() {
            override fun run() {
                if (!player.isOnline) {
                    cooldowns.remove(uuid)
                    cancel()
                    return
                }

                val now = System.currentTimeMillis()
                val remaining = (endTime - now) / 1000

                if (remaining <= 0) {
                    cooldowns.remove(uuid)
                    sendActionBar(
                        plugin = plugin,
                        player = player,
                        durationSeconds = 1,
                        message = "${ChatColor.GREEN}Pronto!"
                    )
                    cancel()
                    return
                }

                sendActionBar(
                    plugin = plugin,
                    player = player,
                    durationSeconds = 1,
                    message = "$messagePrefix${ChatColor.WHITE}${remaining}s"
                )
            }
        }.runTaskTimer(plugin, 0L, 20L)
    }

    fun isCoolingDown(player: Player): Boolean {
        val end = cooldowns[player.uniqueId]
        return end != null && System.currentTimeMillis() < end
    }

    fun sendActionBar(
        plugin: JavaPlugin,
        player: Player,
        durationSeconds: Int,
        message: String,
        delayTicks: Long = 0L,
        intervalTicks: Long = 20L
    ) {
        object : BukkitRunnable() {
            var count = 0
            override fun run() {
                if (count >= durationSeconds) {
                    cancel()
                    return
                }
                try {
                    player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent(message))
                } catch (e: Exception) {
                    player.sendMessage("${ChatColor.RED}[Erro ActionBar] $message")
                }
                count++
            }
        }.runTaskTimer(plugin, delayTicks, intervalTicks)
    }

    fun sendActionBar(player: Player, message: String) {
        try {
            player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent(message))
        } catch (e: Exception) {
            player.sendMessage("${ChatColor.RED}[Erro ActionBar] $message")
        }
    }

    private fun parseVersion(version: String): Int {
        return version.substringAfter("_", "999").toIntOrNull() ?: 999
    }
}
