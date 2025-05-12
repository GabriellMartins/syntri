package com.br.gabrielmartins.syntri.utils.geral.disablers;

import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.plugin.Plugin;

import java.io.File;

public class AnvilFallDisabler implements Listener {

    private final boolean enabled;

    public AnvilFallDisabler(Plugin plugin) {
        File configFile = new File(plugin.getDataFolder(), "modules/general/config.yml");
        FileConfiguration config = YamlConfiguration.loadConfiguration(configFile);

        this.enabled = config.getBoolean("disableAnvilFall.enabled", true);
    }

    @EventHandler(ignoreCancelled = true)
    public void onAnvilFall(EntityChangeBlockEvent event) {
        if (!enabled) return;

        if (event.getEntityType() == EntityType.FALLING_BLOCK && event.getTo() == Material.AIR) {
            if (event.getBlock().getType() == Material.ANVIL) {
                event.setCancelled(true);
                event.getBlock().getState().update(false, false);
            }
        }
    }
}
