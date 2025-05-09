package com.br.gabrielmartins.version3.impl.utils

import org.bukkit.*
import org.bukkit.block.BlockState

object ChunkOptimizer {

    private val protectedChunks = mutableSetOf<Pair<World, Chunk>>()

    /**
     * Carrega um chunk de forma assíncrona com fallback seguro.
     * Garante que farms e entidades não sejam perdidas, mantendo o chunk ativo caso o jogador esteja por perto.
     */
    fun preloadChunkAsync(world: World, x: Int, z: Int, onComplete: ((Chunk) -> Unit)? = null) {
        Bukkit.getScheduler().runTaskAsynchronously(Bukkit.getPluginManager().getPlugin("Syntri")) {
            val chunk = world.getChunkAt(x, z)
            Bukkit.getScheduler().runTask(Bukkit.getPluginManager().getPlugin("Syntri")) {
                if (!chunk.isLoaded) {
                    world.loadChunk(x, z)
                }
                protectedChunks.add(world to chunk)
                onComplete?.invoke(chunk)
            }
        }
    }

    /**
     * Descarrega chunks inativos automaticamente, exceto aqueles com redstone, entidades ou farms sensíveis.
     */
    fun smartUnload(world: World, maxDistance: Int = 10, protectRadius: Int = 3, save: Boolean = true) {
        val players = world.players
        val activeChunks = mutableSetOf<Pair<Int, Int>>()

        for (player in players) {
            val chunkX = player.location.blockX shr 4
            val chunkZ = player.location.blockZ shr 4
            for (x in -protectRadius..protectRadius) {
                for (z in -protectRadius..protectRadius) {
                    activeChunks.add(chunkX + x to chunkZ + z)
                }
            }
        }

        for (chunk in world.loadedChunks) {
            val cx = chunk.x
            val cz = chunk.z

            val isProtected = protectedChunks.contains(world to chunk)
            val isPlayerNearby = activeChunks.contains(cx to cz)
            val hasRedstoneOrTile = containsSensitiveData(chunk)

            if (!isProtected && !isPlayerNearby && !hasRedstoneOrTile) {
                world.unloadChunk(cx, cz, save, false)
            }
        }
    }

    /**
     * Detecta se um chunk contém mecanismos sensíveis como redstone, pistões, fornalhas, hoppers etc.
     */
    private fun containsSensitiveData(chunk: Chunk): Boolean {
        for (state: BlockState in chunk.tileEntities) {
            val type = state.type
            if (type == Material.HOPPER ||
                type == Material.DISPENSER ||
                type == Material.DROPPER ||
                type == Material.FURNACE ||
                type == Material.REDSTONE_WIRE ||
                type.toString().contains("PISTON")
            ) {
                return true
            }
        }
        return false
    }

    /**
     * Força o descarregamento de todos os chunks não protegidos em todos os mundos
     */
    fun globalCleanup(save: Boolean = true) {
        for (world in Bukkit.getWorlds()) {
            smartUnload(world, save = save)
        }
    }

    /**
     * Protege um chunk contra descarregamento automático
     */
    fun protectChunk(chunk: Chunk) {
        protectedChunks.add(chunk.world to chunk)
    }

    /**
     * Remove a proteção de um chunk
     */
    fun unprotectChunk(chunk: Chunk) {
        protectedChunks.remove(chunk.world to chunk)
    }

    /**
     * Limpa todas as proteções aplicadas manualmente
     */
    fun clearProtections() {
        protectedChunks.clear()
    }

    /**
     * Lista os chunks carregados de forma protegida
     */
    fun getProtectedChunks(): Set<Pair<World, Chunk>> {
        return protectedChunks.toSet()
    }
}
