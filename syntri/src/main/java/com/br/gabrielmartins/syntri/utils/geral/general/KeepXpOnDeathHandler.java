package com.br.gabrielmartins.syntri.utils.geral.general;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.plugin.Plugin;

import java.io.File;

public class KeepXpOnDeathHandler implements Listener {

    private final boolean enabled;
    private final String permission;

    public KeepXpOnDeathHandler(Plugin plugin) {
        File configFile = new File(plugin.getDataFolder(), "modules/general/config.yml");
        FileConfiguration config = YamlConfiguration.loadConfiguration(configFile);

        this.enabled = config.getBoolean("keepXpOnDeath.enabled", true);
        this.permission = config.getString("keepXpOnDeath.permission", "system.manterxp");
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent event) {
        if (!enabled) return;

        Player player = event.getEntity();
        if (player.hasPermission(permission)) {
            event.setKeepLevel(true);
            event.setDroppedExp(0);
        }
    }
}
