package com.br.gabrielmartins.syntri.modulo.list.scoreboard

import com.br.gabrielmartins.syntri.SyntriPlugin
import com.br.gabrielmartins.syntri.listener.scoreboard.ScoreboardListener
import com.br.gabrielmartins.syntri.modulo.ModuleLoader
import com.br.gabrielmartins.syntri.modulo.handler.ModuleHandler
import com.br.gabrielmartins.syntri.scoreboard.ScoreboardManager
import org.bukkit.Bukkit

class ScoreboardModule(
    loader: ModuleLoader,
    plugin: SyntriPlugin
) : ModuleHandler(loader, plugin) {

    override fun register() {
        val config = loader.getConfig("scoreboard", "config")
        if (config == null) {
            return
        }
        if (!config.getBoolean("scoreboard.enabled", true)) {
            return
        }


        val manager = ScoreboardManager(config, plugin)

        Bukkit.getOnlinePlayers().forEach { player ->
            manager.apply(player)
        }

        manager.startTitleAnimation(Bukkit.getOnlinePlayers())

        Bukkit.getPluginManager().registerEvents(ScoreboardListener(manager, plugin), plugin)

    }
}