package com.br.gabrielmartins.engine.data.controller;

import com.br.gabrielmartins.engine.data.service.DataService;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Player;

public class DataHandler {

   @Getter @Setter private static DataService service;


    public static void createTables() {
        service.createTables();
    }

    public static void saveHome(Player player, String homeName) {
        service.saveHome(player, homeName);
    }

    public static void saveWarp(Player player, String warpName) {
        service.saveWarp(player, warpName);
    }

    public static void listHomes(Player player) {
        service.listHomes(player);
    }

    public static void listWarps(Player player) {
        service.listWarps(player);
    }

    public static void teleportToHome(Player player, String homeName) {
        service.teleportToHome(player, homeName);
    }

    public static void teleportToWarp(Player player, String warpName) {
        service.teleportToWarp(player, warpName);
    }
}
