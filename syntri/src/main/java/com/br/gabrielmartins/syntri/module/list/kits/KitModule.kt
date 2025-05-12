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

        ensureReadmeExists("kits")

        try {
            KitManager.loadKits(kitFile)
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
    }

    private fun ensureReadmeExists(moduleName: String) {
        val moduleDir = File(plugin.dataFolder, "modules/$moduleName")
        if (!moduleDir.exists()) {
            moduleDir.mkdirs()
        }

        val readmeFile = File(moduleDir, "README.md")
        if (!readmeFile.exists()) {
            val resourcePath = "modules/$moduleName/README.md"
            val resource = plugin.getResource(resourcePath)
            if (resource != null) {
                plugin.saveResource(resourcePath, false)
            }
        }
    }
}
