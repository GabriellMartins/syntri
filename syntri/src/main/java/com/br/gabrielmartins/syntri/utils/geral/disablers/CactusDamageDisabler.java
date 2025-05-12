package com.br.gabrielmartins.syntri.utils.geral.disablers;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByBlockEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.plugin.Plugin;

import java.io.File;

public class CactusDamageDisabler implements Listener {

    private final boolean enabled;

    public CactusDamageDisabler(Plugin plugin) {
        File configFile = new File(plugin.getDataFolder(), "modules/general/config.yml");
        FileConfiguration config = YamlConfiguration.loadConfiguration(configFile);

        this.enabled = config.getBoolean("disableCactusDamage.enabled", true);
    }

    @EventHandler(ignoreCancelled = true)
    public void onCactusDamage(EntityDamageByBlockEvent event) {
        if (!enabled) return;

        if (event.getCause() == DamageCause.CONTACT) {
            event.setCancelled(true);
        }
    }
}
