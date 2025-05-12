package com.br.gabrielmartins.syntri.utils.geral.disablers;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockFromToEvent;
import org.bukkit.plugin.Plugin;

import java.io.File;

public class WaterLavaVerticalFlowDisabler implements Listener {

    private final boolean enabled;
    private final Material water;
    private final Material stationaryWater;
    private final Material lava;
    private final Material stationaryLava;

    public WaterLavaVerticalFlowDisabler(Plugin plugin) {
        File configFile = new File(plugin.getDataFolder(), "modules/general/config.yml");
        FileConfiguration config = YamlConfiguration.loadConfiguration(configFile);

        this.enabled = config.getBoolean("disableWaterLavaFlow.enabled", true);
        this.water = Material.matchMaterial("WATER");
        this.stationaryWater = Material.matchMaterial("STATIONARY_WATER"); // Para versões antigas
        this.lava = Material.matchMaterial("LAVA");
        this.stationaryLava = Material.matchMaterial("STATIONARY_LAVA");
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onFlow(BlockFromToEvent event) {
        if (!enabled) return;

        Material type = event.getBlock().getType();
        if (type != water && type != stationaryWater && type != lava && type != stationaryLava) return;

        Location from = event.getBlock().getLocation();
        Location to = event.getToBlock().getLocation();

        if (from.getY() != to.getY()) {
            event.setCancelled(true);
        }
    }
}
