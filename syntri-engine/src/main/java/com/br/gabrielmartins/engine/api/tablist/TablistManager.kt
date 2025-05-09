package com.br.gabrielmartins.engine.api.tablist

import com.br.gabrielmartins.engine.api.tablist.adapter.LegacyTablistAdapter
import com.br.gabrielmartins.engine.api.tablist.adapter.ModernTablistAdapter
import com.br.gabrielmartins.engine.api.tablist.adapter.TablistAdapter
import me.clip.placeholderapi.PlaceholderAPI
import org.bukkit.Bukkit
import org.bukkit.configuration.file.FileConfiguration
import org.bukkit.entity.Player
import org.bukkit.plugin.Plugin
import org.bukkit.scheduler.BukkitTask

class TablistManager(
    private val plugin: Plugin,
    private val config: FileConfiguration
) {
    private var task: BukkitTask? = null
    private val gradientRegex = Regex("<gradient:(#[A-Fa-f0-9]{6}):(#[A-Fa-f0-9]{6})>(.*?)</gradient>")
    private val adapter: TablistAdapter = if (isModern()) ModernTablistAdapter() else LegacyTablistAdapter()

    fun start() {
        if (!config.getBoolean("tablist.enabled", true)) return

        val interval = config.getLong("tablist.refresh_ticks", 60L)

        task = Bukkit.getScheduler().runTaskTimer(plugin, Runnable {
            Bukkit.getOnlinePlayers().forEach { updateTablist(it) }
        }, 0L, interval)
    }

    fun stop() {
        task?.cancel()
        task = null
    }

    fun updateTablist(player: Player) {
        val headerLines = config.getStringList("tablist.header")
        val footerLines = config.getStringList("tablist.footer")

        val header = headerLines.joinToString("\n") { format(player, it) }
        val footer = footerLines.joinToString("\n") { format(player, it) }

        adapter.sendTablist(player, header, footer)
    }

    private fun format(player: Player, text: String): String {
        var parsed = text.replace("&", "§").replace('$', '§')
        if (Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")) {
            parsed = PlaceholderAPI.setPlaceholders(player, parsed)
        }
        return applyGradientTags(parsed)
    }

    private fun applyGradientTags(text: String): String {
        var result = text
        gradientRegex.findAll(text).forEach {
            val (start, end, content) = it.destructured
            val gradient = GradientUtil.generateGradient(content, start, end)
            result = result.replace(it.value, gradient)
        }
        return result
    }

    private fun isModern(): Boolean {
        val version = Bukkit.getBukkitVersion().split("-")[0]
        return version.split(".").getOrNull(1)?.toIntOrNull()?.let { it >= 13 } ?: false
    }
}
