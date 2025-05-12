package com.br.gabrielmartins.syntri.utils.geral.blockers;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerBedEnterEvent;
import org.bukkit.plugin.Plugin;

import java.io.File;

public class BedEnterBlocker implements Listener {

    private final boolean enabled;
    private final String message;

    public BedEnterBlocker(Plugin plugin) {
        File configFile = new File(plugin.getDataFolder(), "modules/general/config.yml");
        FileConfiguration config = YamlConfiguration.loadConfiguration(configFile);

        this.enabled = config.getBoolean("bed-enter.enabled", true);
        this.message = config.getString("bed-enter.message", "&cYou are not allowed to sleep in beds.");
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onBedEnter(PlayerBedEnterEvent event) {
        if (!enabled) return;

        if (!event.getPlayer().hasPermission("syntri.bypass.bedenter")) {
            event.setCancelled(true);
            event.getPlayer().sendMessage(message.replace("&", "§"));
        }
    }
}
