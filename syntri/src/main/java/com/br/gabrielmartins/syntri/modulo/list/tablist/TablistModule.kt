package com.br.gabrielmartins.syntri.modulo.list.tablist

import com.br.gabrielmartins.syntri.SyntriPlugin
import com.br.gabrielmartins.syntri.modulo.ModuleLoader
import com.br.gabrielmartins.syntri.modulo.handler.ModuleHandler
import com.br.gabrielmartins.syntri.tablist.TablistManager
import org.bukkit.Bukkit

class TablistModule(
    loader: ModuleLoader,
    plugin: SyntriPlugin
) : ModuleHandler(loader, plugin) {

    override fun register() {

        val config = loader.getConfig("tablist", "config")
        if (config == null) {
            return
        }

        if (!config.getBoolean("tablist.enabled", true)) {
            return
        }

        val manager = TablistManager(plugin, config)
        manager.start()

        Bukkit.getOnlinePlayers().forEach { player ->
            manager.updateTablist(player)
        }

    }
}
