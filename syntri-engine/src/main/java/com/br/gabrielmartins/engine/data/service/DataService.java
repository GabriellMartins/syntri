package com.br.gabrielmartins.engine.data.service;

import org.bukkit.entity.Player;

public interface DataService {

    void saveHome(Player player, String homeName);
    void saveWarp(Player player, String warpName);
    void listHomes(Player player);
    void listWarps(Player player);
    void teleportToHome(Player player, String homeName);
    void teleportToWarp(Player player, String warpName);
    void createTables();
}
