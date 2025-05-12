package com.br.gabrielmartins.syntri.module.list.optimizer

import com.br.gabrielmartins.syntri.SyntriPlugin
import com.br.gabrielmartins.syntri.module.ModuleLoader
import com.br.gabrielmartins.syntri.module.handler.ModuleHandler
import com.br.gabrielmartins.syntri.optimization.ChunkOptimizer
import com.br.gabrielmartins.syntri.optimization.EntityOptimizer
import com.br.gabrielmartins.syntri.optimization.EntityLimitPerChunkOptimizer
import com.br.gabrielmartins.syntri.optimization.RedstoneOptimizer
import org.bukkit.Bukkit

class OptimizerModule(
    loader: ModuleLoader,
    plugin: SyntriPlugin
) : ModuleHandler(loader, plugin) {

    override fun register() {
        if (!loader.hasConfig("optimizer", "config")) return

        val config = loader.getConfig("optimizer", "config")
        if (!config.getBoolean("optimizer.enabled", false)) return

        if (config.getBoolean("optimizer.chunk", false)) {
            ChunkOptimizer(plugin)
            Bukkit.getLogger().info("[Optimizer] Chunk optimizer ativado.")
        }

        if (config.getBoolean("optimizer.entity", false)) {
            EntityOptimizer(plugin)
            Bukkit.getLogger().info("[Optimizer] Entity cleaner ativado.")
        }

        if (config.getBoolean("optimizer.chunk-entity-limit", false)) {
            EntityLimitPerChunkOptimizer(plugin)
            Bukkit.getLogger().info("[Optimizer] Limite de entidades por chunk ativado.")
        }

        if (config.getBoolean("optimizer.redstone", false) && Bukkit.getName().lowercase().contains("paper")) {
            RedstoneOptimizer(plugin)
            Bukkit.getLogger().info("[Optimizer] Redstone optimizer ativado.")
        }
    }

    companion object {
        fun registerTo(plugin: SyntriPlugin, loader: ModuleLoader) {
            OptimizerModule(loader, plugin).register()
        }
    }
}
