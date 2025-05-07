package com.br.gabrielmartins.syntri.utils.server;

import org.bukkit.Bukkit;
import lombok.var;
public class ServerStats {

    public static double getTPS() {
        try {
            Object server = Bukkit.getServer();
            Object console = server.getClass().getMethod("getServer").invoke(server);
            var tpsField = console.getClass().getDeclaredField("recentTps");
            tpsField.setAccessible(true);
            double[] tps = (double[]) tpsField.get(console);
            return tps[0];
        } catch (Exception e) {
            return -1;
        }
    }

    public static String formatTPS(double tps) {
        if (tps < 0) return "§cN/A";
        if (tps >= 19.5) return "§a" + format(tps);
        else if (tps >= 18) return "§e" + format(tps);
        else return "§c" + format(tps);
    }

    public static String format(double tps) {
        return String.format("%.2f", Math.min(tps, 20.0));
    }

    public static String getMemoryUsage() {
        Runtime runtime = Runtime.getRuntime();
        long used = (runtime.totalMemory() - runtime.freeMemory()) / 1024 / 1024;
        long max = runtime.maxMemory() / 1024 / 1024;
        return used + "MB §7/ §f" + max + "MB";
    }

    public static String getOnlineMode() {
        return Bukkit.getOnlineMode() ? "§aOnline" : "§cOffline";
    }

    public static int getOnlinePlayers() {
        return Bukkit.getOnlinePlayers().size();
    }

    public static int getMaxPlayers() {
        try {
            Object server = Bukkit.getServer();
            Object playerList = server.getClass().getMethod("getServer").invoke(server);
            return (int) playerList.getClass().getMethod("getMaxPlayers").invoke(playerList);
        } catch (Exception e) {
            return Bukkit.getMaxPlayers();
        }
    }
}
