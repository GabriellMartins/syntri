package com.br.gabrielmartins.syntri.utils.geral.blockers;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.plugin.Plugin;

import java.io.File;

public class DuplicateLoginBlocker implements Listener {

    private final boolean enabled;
    private final String message;

    public DuplicateLoginBlocker(Plugin plugin) {
        File configFile = new File(plugin.getDataFolder(), "modules/general/config.yml");
        FileConfiguration config = YamlConfiguration.loadConfiguration(configFile);

        this.enabled = config.getBoolean("blockDuplicateLogin.enabled", true);
        this.message = config.getString("blockDuplicateLogin.message", "&cThe nickname %nome% is already logged in.");
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPreLogin(AsyncPlayerPreLoginEvent event) {
        if (!enabled) return;

        Player onlinePlayer = Bukkit.getPlayerExact(event.getName());
        if (onlinePlayer != null && onlinePlayer.isOnline()) {
            event.setLoginResult(AsyncPlayerPreLoginEvent.Result.KICK_OTHER);
            event.setKickMessage(message.replace("%nome%", event.getName()).replace("&", "§"));
        }
    }
}
