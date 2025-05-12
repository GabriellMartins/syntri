package com.br.gabrielmartins.syntri.utils.geral.disablers;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.LeavesDecayEvent;
import org.bukkit.plugin.Plugin;

import java.io.File;

public class LeavesDecayDisabler implements Listener {

    private final boolean enabled;

    public LeavesDecayDisabler(Plugin plugin) {
        File configFile = new File(plugin.getDataFolder(), "modules/general/config.yml");
        FileConfiguration config = YamlConfiguration.loadConfiguration(configFile);

        this.enabled = config.getBoolean("disableLeavesDecay.enabled", true);
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onLeavesDecay(LeavesDecayEvent event) {
        if (!enabled) return;
        event.setCancelled(true);
    }
}
