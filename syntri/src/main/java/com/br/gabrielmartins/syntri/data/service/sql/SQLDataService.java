package com.br.gabrielmartins.syntri.data.service.sql;

import com.br.gabrielmartins.engine.backend.Backend;
import com.br.gabrielmartins.engine.data.service.DataService;
import com.br.gabrielmartins.engine.data.table.DataTable;
import org.bukkit.entity.Player;

public class SQLDataService implements DataService {

    private final Backend backend;

    public SQLDataService(Backend backend) {
        this.backend = backend;
    }

    @Override
    public void saveHome(Player player, String homeName) {
        DataTable.HOME.saveHome(player, homeName);
    }

    @Override
    public void saveWarp(Player player, String warpName) {
        DataTable.WARP.saveWarp(player, warpName);
    }

    @Override
    public void listHomes(Player player) {
        DataTable.HOME.listHomes(player);
    }

    @Override
    public void listWarps(Player player) {
        DataTable.WARP.listWarp(player);
    }

    @Override
    public void teleportToHome(Player player, String homeName) {
        DataTable.HOME.teleportToHome(player, homeName);
    }

    @Override
    public void teleportToWarp(Player player, String warpName) {
        DataTable.WARP.teleportToWarp(player, warpName);
    }

    @Override
    public void createTables() {
        for (DataTable table : DataTable.values()) {
            table.createTable();
        }
    }
}
