package com.br.gabrielmartins.syntri.utils.geral.disablers;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.plugin.Plugin;

import java.io.File;

public class EnderDragonExplosionDisabler implements Listener {

    private final boolean enabled;

    public EnderDragonExplosionDisabler(Plugin plugin) {
        File configFile = new File(plugin.getDataFolder(), "modules/general/config.yml");
        FileConfiguration config = YamlConfiguration.loadConfiguration(configFile);

        this.enabled = config.getBoolean("disableEnderDragonExplosion.enabled", true);
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onEnderDragonExplode(EntityExplodeEvent event) {
        if (!enabled || event.getEntity() == null) return;

        if (event.getEntityType() == EntityType.ENDER_DRAGON) {
            event.setCancelled(true);
        }
    }
}
