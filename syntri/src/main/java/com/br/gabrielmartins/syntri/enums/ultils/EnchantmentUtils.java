package com.br.gabrielmartins.syntri.enums.ultils;

import com.br.gabrielmartins.syntri.enums.EnchantmentName;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Map;

public class EnchantmentUtils {

    public static void printEnchantments(ItemStack item) {
        if (!item.hasItemMeta()) return;

        ItemMeta meta = item.getItemMeta();
        if (meta == null || !meta.hasEnchants()) return;

        for (Map.Entry<Enchantment, Integer> entry : meta.getEnchants().entrySet()) {
            Enchantment enchant = entry.getKey();
            int level = entry.getValue();

            String translatedName = EnchantmentName.translate(enchant);
            System.out.println("Encantamento: " + translatedName + " Nível: " + level);
        }
    }
}
