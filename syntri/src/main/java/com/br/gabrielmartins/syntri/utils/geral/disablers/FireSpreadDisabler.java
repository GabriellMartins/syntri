package com.br.gabrielmartins.syntri.utils.geral.disablers;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBurnEvent;
import org.bukkit.event.block.BlockIgniteEvent;
import org.bukkit.event.block.BlockIgniteEvent.IgniteCause;
import org.bukkit.plugin.Plugin;

import java.io.File;

public class FireSpreadDisabler implements Listener {

    private final boolean enabled;

    public FireSpreadDisabler(Plugin plugin) {
        File configFile = new File(plugin.getDataFolder(), "modules/general/config.yml");
        FileConfiguration config = YamlConfiguration.loadConfiguration(configFile);

        this.enabled = config.getBoolean("disableFireSpread.enabled", true);
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onBlockBurn(BlockBurnEvent event) {
        if (!enabled) return;
        event.setCancelled(true);
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onFireSpread(BlockIgniteEvent event) {
        if (!enabled) return;

        IgniteCause cause = event.getCause();
        if (cause == IgniteCause.LAVA || cause == IgniteCause.SPREAD) {
            event.setCancelled(true);
        }
    }
}
