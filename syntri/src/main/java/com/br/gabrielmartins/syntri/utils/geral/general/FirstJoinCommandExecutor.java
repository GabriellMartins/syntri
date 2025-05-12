package com.br.gabrielmartins.syntri.utils.geral.general;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.util.List;

public class FirstJoinCommandExecutor implements Listener {

    private final boolean enabled;
    private final List<String> commandList;

    public FirstJoinCommandExecutor(Plugin plugin) {
        File configFile = new File(plugin.getDataFolder(), "modules/general/config.yml");
        FileConfiguration config = YamlConfiguration.loadConfiguration(configFile);

        this.enabled = config.getBoolean("firstJoinCommands.enabled", true);
        this.commandList = config.getStringList("firstJoinCommands.list");
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onFirstJoin(PlayerJoinEvent event) {
        if (!enabled) return;

        Player player = event.getPlayer();
        if (!player.hasPlayedBefore()) {
            for (String command : commandList) {
                String parsed = command.replace("%player%", player.getName());
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), parsed);
            }
        }
    }
}
