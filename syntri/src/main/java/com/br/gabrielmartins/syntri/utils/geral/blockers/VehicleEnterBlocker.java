package com.br.gabrielmartins.syntri.utils.geral.blockers;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.vehicle.VehicleEnterEvent;
import org.bukkit.plugin.Plugin;

import java.io.File;

public class VehicleEnterBlocker implements Listener {

    private final boolean enabled;
    private final String message;

    public VehicleEnterBlocker(Plugin plugin) {
        File configFile = new File(plugin.getDataFolder(), "modules/general/config.yml");
        FileConfiguration config = YamlConfiguration.loadConfiguration(configFile);

        this.enabled = config.getBoolean("blockVehicleEnter.enabled", true);
        this.message = config.getString("blockVehicleEnter.message", "&cYou are not allowed to enter vehicles.");
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onVehicleEnter(VehicleEnterEvent event) {
        if (!enabled) return;

        if (event.getEntered() instanceof Player) {
            Player player = (Player) event.getEntered();
            if (!player.hasPermission("syntri.bypass.entraremveiculos")) {
                event.setCancelled(true);
                player.sendMessage(message.replace("&", "§"));
            }
        }
    }
}
