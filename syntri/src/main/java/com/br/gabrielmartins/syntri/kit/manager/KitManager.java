package com.br.gabrielmartins.syntri.kit.manager;

import com.br.gabrielmartins.syntri.kit.Kit;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class KitManager {

    private static final Map<String, Kit> kits = new HashMap<>();
    private static final Map<String, Map<UUID, Long>> cooldowns = new HashMap<>();
    private static File kitFile;

    public static void loadKits(File file) {
        kits.clear();
        cooldowns.clear();
        kitFile = file;

        if (!file.exists()) {
            try {
                file.getParentFile().mkdirs();
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        YamlConfiguration config = YamlConfiguration.loadConfiguration(file);

        for (String kitName : config.getKeys(false)) {
            String permission = config.getString(kitName + ".permission");
            String timeString = config.getString(kitName + ".time", "0s");
            long cooldown = parseTimeToMillis(timeString);

            List<ItemStack> items = loadItems(config, kitName + ".items");
            List<ItemStack> armor = loadItems(config, kitName + ".armor");

            kits.put(kitName.toLowerCase(), new Kit(kitName, permission, items, armor, cooldown));
        }
    }

    private static List<ItemStack> loadItems(YamlConfiguration config, String path) {
        List<ItemStack> items = new ArrayList<>();
        if (!config.isConfigurationSection(path)) return items;

        for (String key : config.getConfigurationSection(path).getKeys(false)) {
            String base64 = config.getString(path + "." + key + ".data");
            if (base64 != null) {
                ItemStack item = deserializeItem(base64);
                if (item != null) items.add(item);
            }
        }
        return items;
    }

    public static void createKit(Player player, String name, String permission, String time) {
        YamlConfiguration config = YamlConfiguration.loadConfiguration(kitFile);

        config.set(name + ".permission", permission);
        config.set(name + ".time", time);

        ItemStack[] contents = player.getInventory().getContents();
        ItemStack[] armor = player.getInventory().getArmorContents();

        int index = 0;
        for (ItemStack item : contents) {
            if (item != null && item.getType() != Material.AIR) {
                String path = name + ".items.item" + index++;
                String base64 = serializeItem(item);
                if (base64 != null) {
                    config.set(path + ".data", base64);
                    Bukkit.getLogger().info("[Syntri] Kit " + name + " - Item salvo: " + item.getType());
                } else {
                    Bukkit.getLogger().warning("[Syntri] Não foi possível serializar item: " + item.getType());
                }
            }
        }

        index = 0;
        for (ItemStack item : armor) {
            if (item != null && item.getType() != Material.AIR) {
                String path = name + ".armor.armor" + index++;
                String base64 = serializeItem(item);
                if (base64 != null) {
                    config.set(path + ".data", base64);
                    Bukkit.getLogger().info("[Syntri] Kit " + name + " - Armadura salva: " + item.getType());
                } else {
                    Bukkit.getLogger().warning("[Syntri] Não foi possível serializar armadura: " + item.getType());
                }
            }
        }

        try {
            config.save(kitFile);
            loadKits(kitFile);
            Bukkit.getLogger().info("[Syntri] Kit " + name + " salvo com sucesso.");
        } catch (IOException e) {
            Bukkit.getLogger().warning("[Syntri] Falha ao salvar kit " + name + ": " + e.getMessage());
        }
    }


    private static long parseTimeToMillis(String time) {
        time = time.trim().toLowerCase();
        if (time.endsWith("sa")) return Long.parseLong(time.replace("sa", "")) * 7 * 24 * 60 * 60 * 1000L;
        if (time.endsWith("m")) return Long.parseLong(time.replace("m", "")) * 30L * 24 * 60 * 60 * 1000L;
        if (time.endsWith("d")) return Long.parseLong(time.replace("d", "")) * 24 * 60 * 60 * 1000L;
        if (time.endsWith("h")) return Long.parseLong(time.replace("h", "")) * 60 * 60 * 1000L;
        if (time.endsWith("min")) return Long.parseLong(time.replace("min", "")) * 60 * 1000L;
        if (time.endsWith("s")) return Long.parseLong(time.replace("s", "")) * 1000L;
        return 0L;
    }

    public static Kit getKit(String name) {
        return kits.get(name.toLowerCase());
    }

    public static Collection<Kit> getAllKits() {
        return kits.values();
    }

    public static boolean isOnCooldown(String kit, UUID player) {
        return cooldowns.containsKey(kit) && cooldowns.get(kit).getOrDefault(player, 0L) > System.currentTimeMillis();
    }

    public static long getRemainingCooldown(String kit, UUID player) {
        return Math.max(0L, cooldowns.getOrDefault(kit, Collections.emptyMap()).getOrDefault(player, 0L) - System.currentTimeMillis());
    }

    public static void setCooldown(String kit, UUID player, long duration) {
        cooldowns.computeIfAbsent(kit, k -> new HashMap<>()).put(player, System.currentTimeMillis() + duration);
    }


    public static String serializeItem(ItemStack item) {
        if (item == null || item.getType() == Material.AIR) return null;
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            BukkitObjectOutputStream dataOutput = new BukkitObjectOutputStream(outputStream);
            dataOutput.writeObject(item);
            dataOutput.close();
            return Base64.getEncoder().encodeToString(outputStream.toByteArray());
        } catch (IOException e) {
            Bukkit.getLogger().warning("[Syntri] Erro ao serializar item: " + e.getMessage());
            return null;
        }
    }
    public static ItemStack deserializeItem(String base64) {
        try {
            byte[] data = Base64.getDecoder().decode(base64);
            ByteArrayInputStream inputStream = new ByteArrayInputStream(data);
            BukkitObjectInputStream dataInput = new BukkitObjectInputStream(inputStream);
            ItemStack item = (ItemStack) dataInput.readObject();
            dataInput.close();
            return item;
        } catch (IOException | ClassNotFoundException e) {
            Bukkit.getLogger().warning("[Syntri] Erro ao desserializar item: " + e.getMessage());
            return null;
        }
    }

}
