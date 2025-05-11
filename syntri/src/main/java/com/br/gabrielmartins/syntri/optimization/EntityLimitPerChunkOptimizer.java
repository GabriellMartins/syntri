package com.br.gabrielmartins.syntri.optimization;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.World;
import org.bukkit.entity.LivingEntity;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

public class EntityLimitPerChunkOptimizer {

    private final Plugin plugin;
    private final int maxEntitiesPerChunk = 30;

    public EntityLimitPerChunkOptimizer(Plugin plugin) {
        this.plugin = plugin;
        startLimitTask();
    }

    private void startLimitTask() {
        new BukkitRunnable() {
            @Override
            public void run() {
                for (World world : Bukkit.getWorlds()) {
                    for (Chunk chunk : world.getLoadedChunks()) {
                        int count = 0;
                        for (LivingEntity entity : (LivingEntity[]) chunk.getEntities()) {
                            if (++count > maxEntitiesPerChunk) {
                                entity.remove();
                            }
                        }
                    }
                }
            }
        }.runTaskTimer(plugin, 20 * 60L, 20 * 60L);
    }
}
