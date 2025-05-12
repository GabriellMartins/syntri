package com.br.gabrielmartins.syntri.utils.geral.disablers;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Ghast;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.projectiles.ProjectileSource;

import java.io.File;

public class GhastFireballDisabler implements Listener {

    private final boolean enabled;

    public GhastFireballDisabler(Plugin plugin) {
        File configFile = new File(plugin.getDataFolder(), "modules/general/config.yml");
        FileConfiguration config = YamlConfiguration.loadConfiguration(configFile);

        this.enabled = config.getBoolean("disableGhastFireball.enabled", true);
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onFireballLaunch(ProjectileLaunchEvent event) {
        if (!enabled) return;

        if (event.getEntityType() == EntityType.FIREBALL) {
            ProjectileSource shooter = event.getEntity().getShooter();
            if (shooter instanceof Ghast) {
                event.setCancelled(true);
            }
        }
    }
}
