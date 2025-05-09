package com.br.gabrielmartins.syntri.modules

import com.br.gabrielmartins.syntri.SyntriPlugin
import com.br.gabrielmartins.syntri.listener.server.MotdListener
import com.br.gabrielmartins.syntri.modulo.ModuleLoader
import com.br.gabrielmartins.syntri.modulo.handler.ModuleHandler
import org.bukkit.Bukkit

class MotdModule(
    loader: ModuleLoader,
    plugin: SyntriPlugin
) : ModuleHandler(loader, plugin) {

    override fun register() {

        val config = loader.getConfig("motd", "config")
        if (config == null) {
            return
        }

        if (!config.getBoolean("motd.enabled", true)) {
            return
        }

        Bukkit.getPluginManager().registerEvents(MotdListener(config), plugin)
    }
}
