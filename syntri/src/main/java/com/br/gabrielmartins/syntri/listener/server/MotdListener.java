package com.br.gabrielmartins.syntri.listener.server;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.ServerListPingEvent;

import java.util.List;

public class MotdListener implements Listener {

    private final FileConfiguration config;
    private int currentMessageIndex = 0;

    public MotdListener(FileConfiguration config) {
        this.config = config;
    }

    @EventHandler
    public void onServerPing(ServerListPingEvent event) {
        if (!config.getBoolean("motd.enabled", true)) return;

        List<String> messages = config.getStringList("motd.messages");
        if (messages.isEmpty()) return;

        String motd = ChatColor.translateAlternateColorCodes('&', messages.get(currentMessageIndex));
        event.setMotd(motd);

        currentMessageIndex = (currentMessageIndex + 1) % messages.size();
    }
}
