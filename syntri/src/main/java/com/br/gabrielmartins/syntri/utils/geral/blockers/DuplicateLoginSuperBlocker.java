package com.br.gabrielmartins.syntri.utils.geral.blockers;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.plugin.Plugin;

import java.io.File;

public class DuplicateLoginSuperBlocker implements Listener {

    private final boolean enabled;
    private final String message;

    public DuplicateLoginSuperBlocker(Plugin plugin) {
        File configFile = new File(plugin.getDataFolder(), "modules/general/config.yml");
        FileConfiguration config = YamlConfiguration.loadConfiguration(configFile);

        this.enabled = config.getBoolean("blockDuplicateLoginSuper.enabled", true);
        this.message = config.getString("blockDuplicateLoginSuper.message", "&cThe nickname %nome% is already logged in.");
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onLogin(PlayerLoginEvent event) {
        if (!enabled) return;

        Player existing = Bukkit.getPlayerExact(event.getPlayer().getName());
        if (existing != null && existing.isOnline()) {
            event.setResult(PlayerLoginEvent.Result.KICK_OTHER);
            event.setKickMessage(message.replace("%nome%", existing.getName()).replace("&", "§"));
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onKick(PlayerKickEvent event) {
        if (!enabled) return;

        if (event.getReason() != null && event.getReason().contains("You logged in from another location")) {
            event.setCancelled(true);
        }
    }
}
