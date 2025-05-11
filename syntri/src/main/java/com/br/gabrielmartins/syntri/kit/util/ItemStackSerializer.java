package com.br.gabrielmartins.syntri.kit.util;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;

import java.io.*;
import java.util.Base64;

public class ItemStackSerializer {

    public static String serialize(ItemStack item) {
        if (item == null || item.getType() == null || item.getType() == Material.AIR) return null;
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
             BukkitObjectOutputStream dataOutput = new BukkitObjectOutputStream(outputStream)) {
            dataOutput.writeObject(item);
            return Base64.getEncoder().encodeToString(outputStream.toByteArray());
        } catch (Exception e) {
            Bukkit.getLogger().warning("[Syntri] Erro ao serializar item: " + e.getMessage());
            return null;
        }
    }

    public static ItemStack deserialize(String base64) {
        if (base64 == null || base64.trim().isEmpty()) return null;

        try (ByteArrayInputStream inputStream = new ByteArrayInputStream(Base64.getDecoder().decode(base64));
             BukkitObjectInputStream dataInput = new BukkitObjectInputStream(inputStream)) {

            Object obj = dataInput.readObject();
            if (!(obj instanceof ItemStack)) {
                Bukkit.getLogger().warning("[Syntri] Objeto desserializado não é um ItemStack.");
                return null;
            }

            ItemStack item = (ItemStack) obj;
            if (item.getType() == null) {
                Bukkit.getLogger().warning("[Syntri] ItemStack com 'type = null' ignorado.");
                return null;
            }

            return item;
        } catch (Exception e) {
            Bukkit.getLogger().warning("[Syntri] Erro ao desserializar item: " + e.getMessage());
            return null;
        }
    }
}
