package com.br.gabrielmartins.syntri.commands.registry;

import com.br.gabrielmartins.engine.data.table.DataTable;
import com.br.gabrielmartins.engine.loader.command.info.CommandInfo;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@CommandInfo(names = {"setspawn"}, permission = {"syntri.tp"})
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
