package com.br.gabrielmartins.syntri.module.list.general

import com.br.gabrielmartins.syntri.module.ModuleLoader
import com.br.gabrielmartins.syntri.module.loader.Registrable
import org.bukkit.Bukkit
import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.event.Listener
import org.bukkit.plugin.Plugin
import org.reflections.Reflections
import org.reflections.scanners.SubTypesScanner
import java.io.File

class GeneralModule(loadModules: ModuleLoader, private val plugin: Plugin) : Registrable {

    private val packages = listOf(
        "com.br.gabrielmartins.syntri.utils.geral.blockers",
        "com.br.gabrielmartins.syntri.utils.geral.disablers",
        "com.br.gabrielmartins.syntri.utils.geral.general"
    )

    override fun register() {
        val configFile = File(plugin.dataFolder, "modules/general/config.yml")
        if (!configFile.exists()) {
            plugin.saveResource("modules/general/config.yml", false)
        }

        val config = YamlConfiguration.loadConfiguration(configFile)

        for (pkg in packages) {
            val reflections = Reflections(pkg, SubTypesScanner(false))
            val classes = reflections.getSubTypesOf(Any::class.java)

            for (clazz in classes) {
                val className = clazz.simpleName
                val configKey = className.replace("Handler", "")
                    .replace("Blocker", "")
                    .replace("Disabler", "")
                    .replace("Listener", "")
                    .replaceFirstChar { it.lowercaseChar() }

                if (!config.getBoolean("$configKey.enabled", false)) continue

                try {
                    val instance = try {
                        clazz.getConstructor(Plugin::class.java).newInstance(plugin)
                    } catch (_: NoSuchMethodException) {
                        clazz.getDeclaredConstructor().newInstance()
                    }

                    if (instance is Listener) {
                        Bukkit.getPluginManager().registerEvents(instance, plugin)
                    }

                    plugin.logger.info("[Syntri] ✅ Carregado: $className")
                } catch (ex: Exception) {
                    plugin.logger.warning("[Syntri] ❌ Falha ao carregar: $className")
                    ex.printStackTrace()
                }
            }
        }

        ensureReadmeExists("general")
    }

    private fun ensureReadmeExists(moduleName: String) {
        val moduleDir = File(plugin.dataFolder, "modules/$moduleName")
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
