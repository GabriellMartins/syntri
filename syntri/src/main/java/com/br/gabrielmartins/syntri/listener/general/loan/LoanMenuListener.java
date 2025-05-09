package com.br.gabrielmartins.syntri.listener.general.loan;

import com.br.gabrielmartins.syntri.loan.manager.LoanManager;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class LoanMenuListener implements Listener {

    @EventHandler
    public void onClick(InventoryClickEvent e) {
        if (!e.getView().getTitle().equalsIgnoreCase("§aBank - Loans")) return;

        e.setCancelled(true);
        if (!(e.getWhoClicked() instanceof Player player)) return;

        ItemStack clicked = e.getCurrentItem();
        if (clicked == null || clicked.getType() != Material.EMERALD_BLOCK || !clicked.hasItemMeta()) return;

        ItemMeta meta = clicked.getItemMeta();
        if (meta == null || meta.getDisplayName() == null) return;

        double value;
        try {
            value = Double.parseDouble(meta.getDisplayName().replace("§a$", ""));
        } catch (NumberFormatException ex) {
            return;
        }

        double interest = 15.0;
        int days = 3;

        if (LoanManager.hasLoan(player)) {
            player.sendMessage("§cYou already have an active loan.");
            return;
        }

        LoanManager.createLoan(player, value, interest, days);
        player.closeInventory();
    }
}
