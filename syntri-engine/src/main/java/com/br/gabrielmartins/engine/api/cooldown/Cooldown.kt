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
    private val version = Bukkit.getServer().javaClass.packageName.split(".")[3]
    private val majorVersion = parseVersion(version)

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
                    sendActionBar(player, "${ChatColor.GREEN}Pronto!")
                    cancel()
                    return
                }

                sendActionBar(player, "$messagePrefix${ChatColor.WHITE}${remaining}s")
            }
        }.runTaskTimer(plugin, 0L, 20L)
    }

    fun isCoolingDown(player: Player): Boolean {
        val end = cooldowns[player.uniqueId]
        return end != null && System.currentTimeMillis() < end
    }

    fun sendActionBar(player: Player, message: String) {
        try {
            if (majorVersion >= 108) {
                player.spigot().sendMessage(
                    ChatMessageType.ACTION_BAR,
                    TextComponent(message)
                )
            } else {
                player.sendMessage("${ChatColor.GRAY}[Cooldown] $message")
            }
        } catch (e: Exception) {
            e.printStackTrace()
            player.sendMessage("${ChatColor.RED}[Erro ActionBar] $message")
        }
    }

    private fun parseVersion(version: String): Int {
        return try {
            if ("_" in version) version.split("_")[1].toInt() else 999
        } catch (e: Exception) {
            999
        }
    }
}
