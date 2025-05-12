package com.br.gabrielmartins.syntri.utils.geral.blockers;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.util.List;

public class BlockedCommandsListener implements Listener {

    private final boolean enabled;
    private final String message;
    private final List<String> blockedCommands;

    public BlockedCommandsListener(Plugin plugin) {
        File configFile = new File(plugin.getDataFolder(), "modules/general/config.yml");
        FileConfiguration config = YamlConfiguration.loadConfiguration(configFile);

        this.enabled = config.getBoolean("blocked-commands.enabled", true);
        this.message = config.getString("blocked-commands.message", "&cYou are not allowed to use this command.");
        this.blockedCommands = config.getStringList("blocked-commands.list");
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onCommand(PlayerCommandPreprocessEvent event) {
        if (!enabled) return;

        String command = event.getMessage().toLowerCase().split(" ")[0];
        String cmdNoNamespace = command.contains(":") ? "/" + command.split(":")[1] : command;

        for (String blocked : blockedCommands) {
            if (blocked.equalsIgnoreCase(command) || blocked.equalsIgnoreCase(cmdNoNamespace)) {
                if (!event.getPlayer().hasPermission("syntri.bypass.blockedcommand")) {
                    event.setCancelled(true);
                    event.getPlayer().sendMessage(message.replace("&", "§"));
                    return;
                }
            }
        }
    }
}
