package com.br.gabrielmartins.syntri.module.list.kits

import com.br.gabrielmartins.syntri.SyntriPlugin
import com.br.gabrielmartins.syntri.kit.manager.KitManager
import com.br.gabrielmartins.syntri.module.ModuleLoader
import com.br.gabrielmartins.syntri.module.handler.ModuleHandler
import java.io.File

class KitModule(
    loader: ModuleLoader,
    plugin: SyntriPlugin
) : ModuleHandler(loader, plugin) {

    override fun register() {

        val kitFile = File(plugin.dataFolder, "modules/kits/config.yml")

        if (!kitFile.exists()) {
            plugin.saveResource("modules/kits/config.yml", false)
        }

        try {
            KitManager.loadKits(kitFile)
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
    }
}
