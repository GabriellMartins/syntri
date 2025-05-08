package com.br.gabrielmartins.syntri.kit.manager;

import com.br.gabrielmartins.syntri.kit.Kit;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class KitManager {

    private static final Map<String, Kit> kits = new HashMap<>();
    private static final Map<String, Map<UUID, Long>> cooldowns = new HashMap<>();
    private static File kitFile;

    public static void loadKits(File file) {
        kits.clear();
        cooldowns.clear();
        kitFile = file;
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
            int id = config.getInt(path + "." + key + ".id", 1);
            int amount = config.getInt(path + "." + key + ".amount", 1);

            Material material = getMaterialById(id);
            if (material != null) {
                items.add(new ItemStack(material, amount));
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
                config.set(path + ".id", item.getType().getId());
                config.set(path + ".amount", item.getAmount());
            }
        }

        index = 0;
        for (ItemStack item : armor) {
            if (item != null && item.getType() != Material.AIR) {
                String path = name + ".armor.armor" + index++;
                config.set(path + ".id", item.getType().getId());
                config.set(path + ".amount", item.getAmount());
            }
        }

        try {
            config.save(kitFile);
            loadKits(kitFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static Material getMaterialById(int id) {
        for (Material material : Material.values()) {
            if (material != null && material.getId() == id) {
                return material;
            }
        }
        return null;
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
}
