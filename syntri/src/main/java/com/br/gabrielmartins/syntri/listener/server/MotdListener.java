package com.br.gabrielmartins.syntri.listener.server;

import com.br.gabrielmartins.syntri.SyntriPlugin;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.ServerListPingEvent;

public class MotdListener implements Listener {

    private final SyntriPlugin plugin = SyntriPlugin.getInstance();
    private int currentMessageIndex = 0;

    @EventHandler
    public void onServerPing(ServerListPingEvent event) {
        if (plugin.getConfig().getBoolean("motd.enabled", true)) {
            if (plugin.getConfig().getStringList("motd.messages").isEmpty()) {
                return;
            }

            String motd = plugin.getConfig().getStringList("motd.messages").get(currentMessageIndex);
            motd = ChatColor.translateAlternateColorCodes('&', motd);
            event.setMotd(motd);
            currentMessageIndex = (currentMessageIndex + 1) % plugin.getConfig().getStringList("motd.messages").size();
        }
    }
}
