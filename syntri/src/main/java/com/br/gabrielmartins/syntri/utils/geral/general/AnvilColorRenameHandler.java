package com.br.gabrielmartins.syntri.utils.geral.general;

import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.inventory.InventoryType.SlotType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;

import java.io.File;

public class AnvilColorRenameHandler implements Listener {

    private final boolean enabled;

    public AnvilColorRenameHandler(Plugin plugin) {
        File configFile = new File(plugin.getDataFolder(), "modules/general/config.yml");
        FileConfiguration config = YamlConfiguration.loadConfiguration(configFile);

        this.enabled = config.getBoolean("anvilColorRename.enabled", true);
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onAnvilRename(InventoryClickEvent event) {
        if (!enabled) return;

        if (event.getInventory().getType() != InventoryType.ANVIL) return;
        if (event.getSlotType() != SlotType.RESULT) return;

        ItemStack item = event.getCurrentItem();
        if (item == null || item.getType() == Material.AIR) return;

        ItemMeta meta = item.getItemMeta();
        if (meta == null || !meta.hasDisplayName()) return;

        String newName = meta.getDisplayName();
        ItemStack originalItem = event.getInventory().getItem(0);

        if (originalItem != null && originalItem.hasItemMeta()) {
            ItemMeta originalMeta = originalItem.getItemMeta();
            if (originalMeta.hasDisplayName()) {
                String oldName = originalMeta.getDisplayName().replace("§", "");
                if (newName.equals(oldName)) {
                    meta.setDisplayName(originalMeta.getDisplayName());
                    item.setItemMeta(meta);
                    event.setCurrentItem(item);
                    return;
                }
            }
        }

        if (event.getWhoClicked().hasPermission("system.cornabigorna")) {
            meta.setDisplayName(newName.replace('&', '§'));
            item.setItemMeta(meta);
            event.setCurrentItem(item);
        }
    }
}
