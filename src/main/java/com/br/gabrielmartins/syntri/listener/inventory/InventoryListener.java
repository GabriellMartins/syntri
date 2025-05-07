package com.br.gabrielmartins.syntri.listener.inventory;

import com.br.gabrielmartins.syntri.SyntriPlugin;
import com.br.gabrielmartins.syntri.api.inventory.custom.CustomInventory;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;

public class InventoryListener implements Listener {

    public InventoryListener() {
        SyntriPlugin plugin = SyntriPlugin.getInstance();
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onClick(InventoryClickEvent e) {
        if (!(e.getWhoClicked() instanceof Player)) return;
        Player player = (Player) e.getWhoClicked();

        CustomInventory inv = SyntriPlugin.getInventoryLoader().getOpen(player);
        if (inv == null) return;

        e.setCancelled(true);
        e.setResult(org.bukkit.event.Event.Result.DENY);

        if (e.getClickedInventory() != null && e.getClickedInventory().equals(e.getInventory())) {
            inv.handleClick(player, e.getRawSlot());
        }
    }

    @EventHandler
    public void onDrag(InventoryDragEvent e) {
        if (!(e.getWhoClicked() instanceof Player)) return;
        Player player = (Player) e.getWhoClicked();

        CustomInventory inv = SyntriPlugin.getInventoryLoader().getOpen(player);
        if (inv == null) return;

        e.setCancelled(true);
    }

    @EventHandler
    public void onClose(InventoryCloseEvent e) {
        if (!(e.getPlayer() instanceof Player)) return;
        Player player = (Player) e.getPlayer();
        SyntriPlugin.getInventoryLoader().untrack(player);
    }
}
