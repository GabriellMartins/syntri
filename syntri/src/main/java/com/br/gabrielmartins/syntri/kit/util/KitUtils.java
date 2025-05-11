package com.br.gabrielmartins.syntri.kit.util;

import com.br.gabrielmartins.syntri.kit.Kit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;

public class KitUtils {

    public static boolean giveKitItems(Player player, Kit kit) {
        for (ItemStack item : kit.getItems()) {
            if (item == null || item.getType() == Material.AIR) continue;
            HashMap<Integer, ItemStack> remaining = player.getInventory().addItem(item);
            if (!remaining.isEmpty()) {
                player.sendMessage("§cSeu inventário está cheio. Não foi possível receber todos os itens do kit.");
                return false;
            }
        }

        ItemStack[] currentArmor = player.getInventory().getArmorContents();
        for (int i = 0; i < kit.getArmor().size(); i++) {
            if (i < currentArmor.length && currentArmor[i] != null && currentArmor[i].getType() != Material.AIR) {
                player.sendMessage("§cVocê já está usando uma armadura. Remova-a antes de aplicar o kit.");
                return false;
            }
        }

        for (int i = 0; i < kit.getArmor().size(); i++) {
            if (i < currentArmor.length) {
                currentArmor[i] = kit.getArmor().get(i);
            }
        }

        player.getInventory().setArmorContents(currentArmor);
        return true;
    }
}
