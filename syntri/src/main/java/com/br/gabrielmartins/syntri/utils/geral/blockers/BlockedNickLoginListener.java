package com.br.gabrielmartins.syntri.utils.geral.blockers;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.util.List;

public class BlockedNickLoginListener implements Listener {

    private final boolean enabled;
    private final String message;
    private final List<String> blockedWords;

    public BlockedNickLoginListener(Plugin plugin) {
        File configFile = new File(plugin.getDataFolder(), "modules/general/config.yml");
        FileConfiguration config = YamlConfiguration.loadConfiguration(configFile);

        this.enabled = config.getBoolean("block-bad-nicks.enabled", true);
        this.message = config.getString("block-bad-nicks.message", "&cNick contains a forbidden word: %nick%");
        this.blockedWords = config.getStringList("block-bad-nicks.list");
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onLogin(PlayerLoginEvent event) {
        if (!enabled) return;

        String playerName = event.getPlayer().getName().toLowerCase();

        for (String blocked : blockedWords) {
            if (playerName.contains(blocked.toLowerCase())) {
                event.setResult(PlayerLoginEvent.Result.KICK_OTHER);
                event.setKickMessage(message.replace("%nick%", blocked).replace("&", "§"));
                return;
            }
        }
    }
}
