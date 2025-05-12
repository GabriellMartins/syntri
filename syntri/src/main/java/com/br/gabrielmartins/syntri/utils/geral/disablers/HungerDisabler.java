package com.br.gabrielmartins.syntri.utils.geral.disablers;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.plugin.Plugin;

import java.io.File;

public class HungerDisabler implements Listener {

    private final boolean enabled;

    public HungerDisabler(Plugin plugin) {
        File configFile = new File(plugin.getDataFolder(), "modules/general/config.yml");
        FileConfiguration config = YamlConfiguration.loadConfiguration(configFile);

        this.enabled = config.getBoolean("disableHunger.enabled", true);
    }

    @EventHandler(ignoreCancelled = true)
    public void onHungerChange(FoodLevelChangeEvent event) {
        if (!enabled) return;

        event.setFoodLevel(20);
        event.setCancelled(true);
    }
}
