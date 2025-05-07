package com.br.gabrielmartins.syntri.data.controller;

import com.br.gabrielmartins.syntri.data.table.DataTable;
import org.bukkit.entity.Player;

public class DataHandler {

    public static void createTables() {
        for (DataTable table : DataTable.values()) {
            table.createTable();
        }
    }

    public static void saveHome(Player player, String homeName) {
        DataTable.HOME.saveHome(player, homeName);
    }

    public static void saveWarp(Player player, String warpName) {
        DataTable.WARP.saveWarp(player, warpName);
    }

    public static void listHomes(Player player) {
        DataTable.HOME.listHomes(player);
    }

    public static void listWarps(Player player) {
        DataTable.WARP.listWarp(player);
    }

    public static void teleportToHome(Player player, String homeName) {
        DataTable.HOME.teleportToHome(player, homeName);
    }

    public static void teleportToWarp(Player player, String warpName) {
        DataTable.WARP.teleportToWarp(player, warpName);
    }
}
