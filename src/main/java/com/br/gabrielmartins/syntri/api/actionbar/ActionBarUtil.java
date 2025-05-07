package com.br.gabrielmartins.syntri.api.actionbar;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

public class ActionBarUtil {

    public static void send(Player player, String message) {
        try {
            Class<?> chatComponentText = getNMSClass("ChatComponentText");
            Class<?> iChatBaseComponent = getNMSClass("IChatBaseComponent");
            Class<?> packetPlayOutChat = getNMSClass("PacketPlayOutChat");

            Constructor<?> chatConstructor = chatComponentText.getConstructor(String.class);
            Object chatComponent = chatConstructor.newInstance(message);

            Object packet;
            try {
                packet = packetPlayOutChat.getConstructor(iChatBaseComponent, byte.class).newInstance(chatComponent, (byte) 2);
            } catch (NoSuchMethodException e) {
                Class<?> chatMessageType = getNMSClass("ChatMessageType");
                Object gameInfo = chatMessageType.getEnumConstants()[2];
                packet = packetPlayOutChat.getConstructor(iChatBaseComponent, chatMessageType).newInstance(chatComponent, gameInfo);
            }

            Object handle = player.getClass().getMethod("getHandle").invoke(player);
            Object connection = handle.getClass().getField("playerConnection").get(handle);
            Method sendPacket = connection.getClass().getMethod("sendPacket", getNMSClass("Packet"));
            sendPacket.invoke(connection, packet);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static Class<?> getNMSClass(String name) throws ClassNotFoundException {
        String version = Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];
        return Class.forName("net.minecraft.server." + version + "." + name);
    }
}
