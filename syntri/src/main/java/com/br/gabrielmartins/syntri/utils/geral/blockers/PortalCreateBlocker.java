package com.br.gabrielmartins.syntri.utils.geral.blockers;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.world.PortalCreateEvent;
import org.bukkit.plugin.Plugin;

import java.io.File;

public class PortalCreateBlocker implements Listener {

    private final boolean enabled;

    public PortalCreateBlocker(Plugin plugin) {
        File configFile = new File(plugin.getDataFolder(), "modules/general/config.yml");
        FileConfiguration config = YamlConfiguration.loadConfiguration(configFile);

        this.enabled = config.getBoolean("blockPortalCreate.enabled", true);
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onPortalCreate(PortalCreateEvent event) {
        if (!enabled) return;
        event.setCancelled(true);
    }
}
