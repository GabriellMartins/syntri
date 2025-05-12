package com.br.gabrielmartins.syntri.utils.geral.general;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPortalEnterEvent;
import org.bukkit.event.entity.EntityPortalExitEvent;
import org.bukkit.plugin.Plugin;

import java.io.File;

public class DeadEntityPortalCleanupHandler implements Listener {

    private final boolean enabled;

    public DeadEntityPortalCleanupHandler(Plugin plugin) {
        File configFile = new File(plugin.getDataFolder(), "modules/general/config.yml");
        FileConfiguration config = YamlConfiguration.loadConfiguration(configFile);

        this.enabled = config.getBoolean("removeDeadEntitiesOnPortal.enabled", true);
    }

    @EventHandler
    public void onPortalEnter(EntityPortalEnterEvent event) {
        if (!enabled) return;
        if (event.getEntity().isDead()) {
            event.getEntity().remove();
        }
    }

    @EventHandler
    public void onPortalExit(EntityPortalExitEvent event) {
        if (!enabled) return;
        if (event.getEntity().isDead()) {
            event.getEntity().remove();
        }
    }
}
