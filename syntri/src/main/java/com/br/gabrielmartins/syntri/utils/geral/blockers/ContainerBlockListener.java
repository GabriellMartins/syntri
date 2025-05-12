package com.br.gabrielmartins.syntri.utils.geral.blockers;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ContainerBlockListener implements Listener {

    private final boolean enabled;
    private final String message;
    private final Set<InventoryType> blockedTypes = new HashSet<>();

    public ContainerBlockListener(Plugin plugin) {
        File configFile = new File(plugin.getDataFolder(), "modules/general/config.yml");
        FileConfiguration config = YamlConfiguration.loadConfiguration(configFile);

        this.enabled = config.getBoolean("block-containers.enabled", true);
        this.message = config.getString("block-containers.message", "&cYou don't have permission to open this container.");
        List<String> types = config.getStringList("block-containers.list");

        for (String type : types) {
            try {
                blockedTypes.add(InventoryType.valueOf(type.toUpperCase()));
            } catch (IllegalArgumentException ex) {
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onContainerOpen(InventoryOpenEvent event) {
        if (!enabled) return;

        if (!(event.getPlayer() instanceof Player)) return;
        Player player = (Player) event.getPlayer();

        InventoryType openedType = event.getInventory().getType();
        if (blockedTypes.contains(openedType)) {
            if (!player.hasPermission("syntri.bypass.containerblock")) {
                event.setCancelled(true);
                player.sendMessage(message.replace("&", "§"));
            }
        }
    }
}
