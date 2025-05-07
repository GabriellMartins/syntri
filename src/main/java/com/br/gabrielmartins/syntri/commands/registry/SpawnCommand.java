package com.br.gabrielmartins.syntri.commands.registry;

import com.br.gabrielmartins.syntri.commands.CommandInfo;
import com.br.gabrielmartins.syntri.data.controller.DataHandler;
import com.br.gabrielmartins.syntri.data.table.DataTable;
import org.bukkit.command.*;
import org.bukkit.entity.Player;

import javax.xml.crypto.Data;

@CommandInfo(names = {"spawn", "setspawn"}, permission = {"syntri.spawn", "syntri.setspawn"})
public class SpawnCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (!(sender instanceof Player)) {
            sender.sendMessage("§cApenas jogadores podem usar este comando.");
            return true;
        }

        Player player = (Player) sender;

        switch (label.toLowerCase()) {
            case "setspawn":
                if (!player.hasPermission("syntri.setspawn")) {
                    player.sendMessage("§cVocê não tem permissão para definir o spawn.");
                    return true;
                }
                DataTable.saveSpawn(player);
                player.sendMessage("§aSpawn definido com sucesso.");
                return true;

            case "spawn":
                if (!player.hasPermission("syntri.spawn")) {
                    player.sendMessage("§cVocê não tem permissão para ir ao spawn.");
                    return true;
                }
                DataTable.teleportToSpawn(player);
                return true;

            default:
                return false;
        }
    }
}
