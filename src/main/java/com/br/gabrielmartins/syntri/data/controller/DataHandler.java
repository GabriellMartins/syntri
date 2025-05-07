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

}
