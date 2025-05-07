package com.br.gabrielmartins.syntri.api.cooldown;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class CooldownActionBarAPI {

    private static final Map<UUID, Long> cooldowns = new HashMap<>();

    public static void start(JavaPlugin plugin, Player player, int seconds, String messagePrefix) {
        UUID uuid = player.getUniqueId();
        long endTime = System.currentTimeMillis() + (seconds * 1000L);
        cooldowns.put(uuid, endTime);

        new BukkitRunnable() {
            @Override
            public void run() {
                if (!player.isOnline()) {
                    cooldowns.remove(uuid);
                    cancel();
                    return;
                }

                long now = System.currentTimeMillis();
                long remaining = (endTime - now) / 1000;

                if (remaining <= 0) {
                    cooldowns.remove(uuid);
                    sendActionBar(player, "§aPronto!");
                    cancel();
                    return;
                }

                sendActionBar(player, messagePrefix + "§f" + remaining + "s");
            }
        }.runTaskTimer(plugin, 0L, 20L);
    }

    public static boolean isCoolingDown(Player player) {
        Long end = cooldowns.get(player.getUniqueId());
        return end != null && System.currentTimeMillis() < end;
    }

    public static void sendActionBar(Player player, String message) {
        try {
            String version = Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];

            Class<?> chatComponentText = Class.forName("net.minecraft.server." + version + ".ChatComponentText");
            Class<?> iChatBaseComponent = Class.forName("net.minecraft.server." + version + ".IChatBaseComponent");
            Object chatComponent = chatComponentText.getConstructor(String.class).newInstance(message);

            Class<?> packetPlayOutChat = Class.forName("net.minecraft.server." + version + ".PacketPlayOutChat");
            Object packet;

            try {
                packet = packetPlayOutChat.getConstructor(iChatBaseComponent, byte.class).newInstance(chatComponent, (byte) 2);
            } catch (NoSuchMethodException e) {
                Class<?> chatMessageType = Class.forName("net.minecraft.server." + version + ".ChatMessageType");
                Object actionBarEnum = chatMessageType.getEnumConstants()[2]; // ACTION_BAR
                packet = packetPlayOutChat.getConstructor(iChatBaseComponent, chatMessageType).newInstance(chatComponent, actionBarEnum);
            }

            Object handle = player.getClass().getMethod("getHandle").invoke(player);
            Object connection = handle.getClass().getField("playerConnection").get(handle);
            Method sendPacket = connection.getClass().getMethod("sendPacket", Class.forName("net.minecraft.server." + version + ".Packet"));
            sendPacket.invoke(connection, packet);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
