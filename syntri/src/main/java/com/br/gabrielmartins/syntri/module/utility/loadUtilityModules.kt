package com.br.gabrielmartins.syntri.module.utility

import com.br.gabrielmartins.syntri.SyntriPlugin
import com.br.gabrielmartins.syntri.module.loader.Registrable
import org.bukkit.plugin.Plugin
import org.reflections.Reflections
import org.reflections.scanners.Scanners
import java.lang.reflect.Modifier

fun loadUtilityModules(basePackage: String) {
    val reflections = Reflections(basePackage, Scanners.SubTypes.filterResultsBy { true })
    val plugin: Plugin = SyntriPlugin.instance

    val found = reflections.getSubTypesOf(Any::class.java)

    for (clazz in found) {
        try {
            if (Modifier.isAbstract(clazz.modifiers) || clazz.isInterface) continue

            val instance = try {
                clazz.getDeclaredConstructor(Plugin::class.java).newInstance(plugin)
            } catch (_: NoSuchMethodException) {
                clazz.getDeclaredConstructor().newInstance()
            }

            if (instance is Registrable) {
                instance.register()
            }
        } catch (ex: Exception) {
            SyntriPlugin.instance.logger.warning("❌ Failed to load utility module: ${clazz.name}")
            ex.printStackTrace()
        }
    }
}
