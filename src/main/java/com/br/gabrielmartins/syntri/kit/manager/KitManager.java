package com.br.gabrielmartins.syntri.kit.manager;

import com.br.gabrielmartins.syntri.kit.Kit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.entity.Player;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class KitManager {
    private static final Map<String, Kit> kits = new HashMap<>();
    private static final Map<String, Map<UUID, Long>> cooldowns = new HashMap<>();
    private static File kitFile;

    public static void loadKits(File file) {
        kits.clear();
        cooldowns.clear();
        kitFile = file;
        YamlConfiguration config = YamlConfiguration.loadConfiguration(file);

        for (String name : config.getKeys(false)) {
            String permission = config.getString(name + ".permission");
            String timeString = config.getString(name + ".time", "0s");
            long cooldown = parseTimeToMillis(timeString);

            List<ItemStack> contents = getItemList(config.getList(name + ".items"));
            List<ItemStack> armor = getItemList(config.getList(name + ".armor"));

            kits.put(name.toLowerCase(), new Kit(name, permission, contents, armor, cooldown));
        }
    }

    private static List<ItemStack> getItemList(List<?> rawItems) {
        if (rawItems == null) return Collections.emptyList();
        return rawItems.stream()
                .filter(Objects::nonNull)
                .filter(ItemStack.class::isInstance)
                .map(ItemStack.class::cast)
                .filter(item -> item.getType() != null)
                .collect(Collectors.toList());
    }

    public static void createKit(Player player, String name, String permission, String time) {
        YamlConfiguration config = YamlConfiguration.loadConfiguration(kitFile);

        List<ItemStack> contents = Arrays.stream(player.getInventory().getContents())
                .filter(item -> item != null && item.getType() != null)
                .collect(Collectors.toList());

        List<ItemStack> armor = Arrays.stream(player.getInventory().getArmorContents())
                .filter(item -> item != null && item.getType() != null)
                .collect(Collectors.toList());


        config.set(name + ".permission", permission);
        config.set(name + ".time", time);
        config.set(name + ".items", contents);
        config.set(name + ".armor", armor);

        try {
            config.save(kitFile);
            loadKits(kitFile);
        } catch (IOException e) {
            e.printStackTrace();
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
}
