package com.br.gabrielmartins.syntri.api.reflection;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import com.br.gabrielmartins.syntri.SyntriPlugin;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class ReflectionUtils {

    private static final String version;
    private static Method getHandle;
    private static Method sendPacket;
    private static Field playerConnectionField;

    static {
        String v;
        try {
            String[] parts = Bukkit.getServer().getClass().getPackage().getName().split("\\.");
            v = parts.length > 3 ? parts[3] : "UNKNOWN";
        } catch (Exception ex) {
            v = "UNKNOWN";
        }
        version = v;
    }

    public static void loadUtils() {
        try {
            if (!version.equals("UNKNOWN")) {
                getHandle = getOBClass("entity.CraftPlayer").getMethod("getHandle");
                playerConnectionField = getNMSClass("EntityPlayer").getField("playerConnection");
                sendPacket = getNMSClass("PlayerConnection").getMethod("sendPacket", getNMSClass("Packet"));
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    public static Class<?> getNMSClass(String name) throws ClassNotFoundException {
        return Class.forName("net.minecraft.server." + version + "." + name);
    }

    public static Class<?> getOBClass(String name) throws ClassNotFoundException {
        return Class.forName("org.bukkit.craftbukkit." + version + "." + name);
    }

    public static void sendPacket(Player player, Object packet) {
        try {
            if (SyntriPlugin.getVersion().value < 17) {
                Object entityPlayer = getHandle.invoke(player);
                Object playerConnection = playerConnectionField.get(entityPlayer);
                sendPacket.invoke(playerConnection, packet);
            } else {
                Object handle = player.getClass().getMethod("getHandle").invoke(player);
                Object playerConnection = handle.getClass().getField("b").get(handle);
                Class<?> packetClass = Class.forName("net.minecraft.network.protocol.Packet");
                playerConnection.getClass().getMethod("sendPacket", packetClass).invoke(playerConnection, packet);
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    public static List<Class<?>> getProjectClasses(String pckg) {
        try {
            List<Class<?>> classes = new ArrayList<>();
            File directory = new File(Thread.currentThread().getContextClassLoader().getResource(pckg).getFile());
            for (File file : directory.listFiles()) {
                if (file.getName().endsWith(".class")) {
                    classes.add(Class.forName(pckg.replace('/', '.') + '.' + file.getName().replace(".class", "")));
                } else if (file.isDirectory()) {
                    classes.addAll(getProjectClasses(pckg + "/" + file.getName()));
                }
            }
            return classes;
        } catch (Throwable e) {
            return null;
        }
    }
}
