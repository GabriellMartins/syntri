package com.br.gabrielmartins.syntri.optimization;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.data.Powerable;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

public class RedstoneOptimizer {

    private final Plugin plugin;

    public RedstoneOptimizer(Plugin plugin) {
        this.plugin = plugin;
        startRedstoneCheck();
    }

    private void startRedstoneCheck() {
        new BukkitRunnable() {
            @Override
            public void run() {
                for (World world : Bukkit.getWorlds()) {
                    world.getPlayers().forEach(player -> {
                        for (int dx = -10; dx <= 10; dx++) {
                            for (int dy = -5; dy <= 5; dy++) {
                                for (int dz = -10; dz <= 10; dz++) {
                                    Block block = player.getLocation().add(dx, dy, dz).getBlock();
                                    if (block.getBlockData() instanceof Powerable) {
                                        Powerable power = (Powerable) block.getBlockData();
                                        if (power.isPowered()) {
                                            power.setPowered(false);
                                            block.setBlockData(power);
                                        }
                                    }
                                }
                            }
                        }
                    });
                }
            }
        }.runTaskTimer(plugin, 20 * 60L, 20 * 180L); // A cada 3 minutos
    }
}
