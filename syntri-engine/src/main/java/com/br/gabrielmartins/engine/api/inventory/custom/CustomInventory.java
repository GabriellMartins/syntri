package com.br.gabrielmartins.engine.api.inventory.custom;

import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public interface CustomInventory {
    void open(Player player);
    void handleClick(Player player, int slot);
    Inventory getInventory();
}
