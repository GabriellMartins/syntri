package com.br.gabrielmartins.syntri.optimization;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

public class ChunkOptimizer {

    private final Plugin plugin;
    private final int chunkViewDistance = 8;     // área segura ao redor do jogador
    private final int unloadRadius = 12;         // distância a partir da qual chunk será descartado
    private final int maxChunksPerTick = 10;     // quantidade máxima de chunks por tick

    private final Queue<Chunk> unloadQueue = new ArrayDeque<>();

    public ChunkOptimizer(Plugin plugin) {
        this.plugin = plugin;
        startChunkCollectionTask();
        startChunkUnloadTask();
    }

    private void startChunkCollectionTask() {
        new BukkitRunnable() {
            @Override
            public void run() {
                for (World world : Bukkit.getWorlds()) {
                    Set<Long> protectedChunks = new HashSet<>();

                    // Coleta chunks ao redor de todos os jogadores
                    for (Player player : world.getPlayers()) {
                        int px = player.getLocation().getBlockX() >> 4;
                        int pz = player.getLocation().getBlockZ() >> 4;

                        for (int dx = -chunkViewDistance; dx <= chunkViewDistance; dx++) {
                            for (int dz = -chunkViewDistance; dz <= chunkViewDistance; dz++) {
                                protectedChunks.add(chunkKey(px + dx, pz + dz));
                            }
                        }
                    }

                    for (Chunk chunk : world.getLoadedChunks()) {
                        long key = chunkKey(chunk.getX(), chunk.getZ());
                        if (!protectedChunks.contains(key) && isFarFromAllPlayers(chunk, world)) {
                            unloadQueue.add(chunk);
                        }
                    }
                }
            }
        }.runTaskTimer(plugin, 20 * 10L, 20 * 30L); // Roda a cada 30s
    }

    private void startChunkUnloadTask() {
        new BukkitRunnable() {
            @Override
            public void run() {
                int count = 0;
                while (!unloadQueue.isEmpty() && count < maxChunksPerTick) {
                    Chunk chunk = unloadQueue.poll();
                    if (chunk != null && chunk.isLoaded()) {
                        chunk.unload(true);
                        count++;
                    }
                }
            }
        }.runTaskTimer(plugin, 1L, 1L);
    }

    private boolean isFarFromAllPlayers(Chunk chunk, World world) {
        Location center = chunk.getBlock(8, 64, 8).getLocation();

        for (Player player : world.getPlayers()) {
            if (player.getLocation().distanceSquared(center) < (unloadRadius * 16.0) * (unloadRadius * 16.0)) {
                return false;
            }
        }
        return true;
    }

    private long chunkKey(int x, int z) {
        return (((long) x) << 32) | (z & 0xffffffffL);
    }
}