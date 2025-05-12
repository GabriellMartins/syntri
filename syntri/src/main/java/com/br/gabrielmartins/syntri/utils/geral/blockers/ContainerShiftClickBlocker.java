package com.br.gabrielmartins.syntri.utils.geral.blockers;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ContainerShiftClickBlocker implements Listener {

    private final boolean enabled;
    private final String message;
    private final Set<InventoryType> blockedTypes = new HashSet<>();

    public ContainerShiftClickBlocker(Plugin plugin) {
        File configFile = new File(plugin.getDataFolder(), "modules/general/config.yml");
        FileConfiguration config = YamlConfiguration.loadConfiguration(configFile);

        this.enabled = config.getBoolean("block-shift-in-containers.enabled", true);
        this.message = config.getString("block-shift-in-containers.message", "&cYou are not allowed to use shift-click in %tipo%.");

        List<String> types = config.getStringList("block-shift-in-containers.containers");
        for (String typeName : types) {
            try {
                blockedTypes.add(InventoryType.valueOf(typeName.toUpperCase()));
            } catch (IllegalArgumentException ex) {
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onShiftClick(InventoryClickEvent event) {
        if (!enabled) return;
        if (event.getCurrentItem() == null || event.getCurrentItem().getType() == Material.AIR) return;
        if (!event.getClick().isShiftClick()) return;
        if (!blockedTypes.contains(event.getInventory().getType())) return;
        if (!(event.getWhoClicked() instanceof Player)) return;

        Player player = (Player) event.getWhoClicked();
        if (player.hasPermission("syntri.bypass.shiftemcontainer")) return;

        String typeName = formatInventoryType(event.getInventory().getType());
        player.sendMessage(message.replace("%tipo%", typeName).replace("&", "§"));
        event.setCancelled(true);
    }

    private String formatInventoryType(InventoryType type) {
        String name = type.name().toLowerCase().replace("_", " ");
        return Character.toUpperCase(name.charAt(0)) + name.substring(1);
    }
}
