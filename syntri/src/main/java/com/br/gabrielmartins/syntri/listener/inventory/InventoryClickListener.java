package com.br.gabrielmartins.syntri.listener.inventory;

import com.br.gabrielmartins.engine.api.inventory.custom.CustomInventory;
import com.br.gabrielmartins.syntri.SyntriPlugin;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

public class InventoryClickListener implements Listener {

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player)) return;

        Player player = (Player) event.getWhoClicked();
        String title = event.getView().getTitle();

        CustomInventory custom = SyntriPlugin.getInstance().getInventoryLoader().getByTitle(title);
        if (custom == null) return;

        event.setCancelled(true);
        custom.handleClick(player, event.getSlot());
    }
}
