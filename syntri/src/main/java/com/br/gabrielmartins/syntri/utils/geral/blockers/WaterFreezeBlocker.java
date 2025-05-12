package com.br.gabrielmartins.syntri.utils.geral.blockers;

import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockFormEvent;
import org.bukkit.plugin.Plugin;

import java.io.File;

public class WaterFreezeBlocker implements Listener {

    private final boolean enabled;
    private final Material waterMaterial;
    private final Material stationaryWaterMaterial;

    public WaterFreezeBlocker(Plugin plugin) {
        File configFile = new File(plugin.getDataFolder(), "modules/general/config.yml");
        FileConfiguration config = YamlConfiguration.loadConfiguration(configFile);

        this.enabled = config.getBoolean("blockWaterFreeze.enabled", true);
        this.waterMaterial = Material.matchMaterial("WATER");
        this.stationaryWaterMaterial = Material.matchMaterial("STATIONARY_WATER");
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onWaterFreeze(BlockFormEvent event) {
        if (!enabled) return;

        Material type = event.getBlock().getType();
        if (type == waterMaterial || (stationaryWaterMaterial != null && type == stationaryWaterMaterial)) {
            event.setCancelled(true);
        }
    }
}
