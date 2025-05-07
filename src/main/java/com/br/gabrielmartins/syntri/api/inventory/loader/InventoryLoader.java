package com.br.gabrielmartins.syntri.api.inventory.loader;

import com.br.gabrielmartins.syntri.api.inventory.InventoryInfo;
import com.br.gabrielmartins.syntri.api.inventory.custom.CustomInventory;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.*;

public class InventoryLoader {

    private final Map<String, CustomInventory> inventories = new HashMap<>();
    private final Map<UUID, CustomInventory> openInventories = new HashMap<>();

    public void load(List<Class<?>> classes) {
        for (Class<?> clazz : classes) {
            if (!clazz.isAnnotationPresent(InventoryInfo.class)) continue;
            if (!CustomInventory.class.isAssignableFrom(clazz)) continue;

            try {
                CustomInventory inventory = (CustomInventory) clazz.newInstance();
                InventoryInfo info = clazz.getAnnotation(InventoryInfo.class);
                inventories.put(info.title(), inventory);
                Bukkit.getLogger().info("[Syntri] Inventário '" + info.title() + "' registrado.");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public CustomInventory getByTitle(String title) {
        return inventories.get(title);
    }

    public void track(Player player, CustomInventory inv) {
        openInventories.put(player.getUniqueId(), inv);
    }

    public void untrack(Player player) {
        openInventories.remove(player.getUniqueId());
    }

    public CustomInventory getOpen(Player player) {
        return openInventories.get(player.getUniqueId());
    }
}
