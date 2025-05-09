package com.br.gabrielmartins.version3.impl.utils

import org.bukkit.Bukkit
import org.bukkit.plugin.Plugin
import org.bukkit.scheduler.BukkitRunnable

object TickUtils {
    fun runLater(plugin: Plugin, ticks: Long, block: () -> Unit) {
        object : BukkitRunnable() {
            override fun run() {
                block()
            }
        }.runTaskLater(plugin, ticks)
    }

    fun runAsync(plugin: Plugin, block: () -> Unit) {
        object : BukkitRunnable() {
            override fun run() {
                block()
            }
        }.runTaskAsynchronously(plugin)
    }
}
