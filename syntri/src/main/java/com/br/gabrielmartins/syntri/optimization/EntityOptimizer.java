package com.br.gabrielmartins.syntri.optimization;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

public class EntityOptimizer {

    private final Plugin plugin;

    public EntityOptimizer(Plugin plugin) {
        this.plugin = plugin;
        startCleanupTask();
    }

    private void startCleanupTask() {
        new BukkitRunnable() {
            @Override
            public void run() {
                for (World world : Bukkit.getWorlds()) {
                    int removed = 0;
                    for (Entity entity : world.getEntities()) {
                        if (entity instanceof Item && entity.getTicksLived() > 1200) {
                            entity.remove();
                            removed++;
                        }
                    }
                    if (removed > 0) {
                        Bukkit.getLogger().info("[EntityOptimizer] Removidos " + removed + " itens dropados antigos em " + world.getName());
                    }
                }
            }
        }.runTaskTimer(plugin, 20 * 60L, 20 * 120L);
    }
}
