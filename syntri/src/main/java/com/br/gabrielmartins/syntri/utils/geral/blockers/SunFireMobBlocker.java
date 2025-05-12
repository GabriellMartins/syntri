package com.br.gabrielmartins.syntri.utils.geral.blockers;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityCombustByBlockEvent;
import org.bukkit.event.entity.EntityCombustByEntityEvent;
import org.bukkit.event.entity.EntityCombustEvent;
import org.bukkit.plugin.Plugin;

import java.io.File;

public class SunFireMobBlocker implements Listener {

    private final boolean enabled;

    public SunFireMobBlocker(Plugin plugin) {
        File configFile = new File(plugin.getDataFolder(), "modules/general/config.yml");
        FileConfiguration config = YamlConfiguration.loadConfiguration(configFile);

        this.enabled = config.getBoolean("blockSunFire.enabled", true);
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onCombust(EntityCombustEvent event) {
        if (!enabled) return;

        if (!(event instanceof EntityCombustByEntityEvent) && !(event instanceof EntityCombustByBlockEvent)) {
            event.setCancelled(true);
        }
    }
}
