package com.br.gabrielmartins.syntri.utils.geral.disablers;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Wither;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.projectiles.ProjectileSource;

import java.io.File;

public class WitherDamageDisabler implements Listener {

    private final boolean enabled;

    public WitherDamageDisabler(Plugin plugin) {
        File configFile = new File(plugin.getDataFolder(), "modules/general/config.yml");
        FileConfiguration config = YamlConfiguration.loadConfiguration(configFile);

        this.enabled = config.getBoolean("disableWitherDamage.enabled", true);
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onWitherBreakBlock(EntityChangeBlockEvent event) {
        if (!enabled) return;

        if (event.getEntityType() == EntityType.WITHER) {
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onWitherSkullLaunch(ProjectileLaunchEvent event) {
        if (!enabled) return;

        if (event.getEntityType() == EntityType.WITHER_SKULL) {
            ProjectileSource shooter = event.getEntity().getShooter();
            if (shooter instanceof Wither) {
                event.setCancelled(true);
            }
        }
    }
}
