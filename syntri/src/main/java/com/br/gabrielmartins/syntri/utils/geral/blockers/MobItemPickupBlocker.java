package com.br.gabrielmartins.syntri.utils.geral.blockers;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.plugin.Plugin;

import java.io.File;

public class MobItemPickupBlocker implements Listener {

    private final boolean enabled;

    public MobItemPickupBlocker(Plugin plugin) {
        File configFile = new File(plugin.getDataFolder(), "modules/general/config.yml");
        FileConfiguration config = YamlConfiguration.loadConfiguration(configFile);

        this.enabled = config.getBoolean("blockMobItemPickup.enabled", true);
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    public void onMobSpawn(CreatureSpawnEvent event) {
        if (!enabled) return;
        event.getEntity().setCanPickupItems(false);
    }
}
