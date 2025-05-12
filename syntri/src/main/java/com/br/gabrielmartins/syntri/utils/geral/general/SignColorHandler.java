package com.br.gabrielmartins.syntri.utils.geral.general;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.plugin.Plugin;

import java.io.File;

public class SignColorHandler implements Listener {

    private final boolean enabled;

    public SignColorHandler(Plugin plugin) {
        File configFile = new File(plugin.getDataFolder(), "modules/general/config.yml");
        FileConfiguration config = YamlConfiguration.loadConfiguration(configFile);

        this.enabled = config.getBoolean("signColor.enabled", true);
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onSignWrite(SignChangeEvent event) {
        if (!enabled) return;
        if (!event.getPlayer().hasPermission("system.cornaplaca")) return;

        for (int i = 0; i < event.getLines().length; i++) {
            String line = event.getLine(i);
            if (line != null && !line.isEmpty()) {
                event.setLine(i, line.replace('&', '§'));
            }
        }
    }
}
