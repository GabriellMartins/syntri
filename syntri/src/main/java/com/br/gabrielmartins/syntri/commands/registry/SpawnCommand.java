package com.br.gabrielmartins.syntri.commands.registry;

import com.br.gabrielmartins.syntri.MessagesManager;
import com.br.gabrielmartins.engine.data.table.DataTable;
import com.br.gabrielmartins.engine.loader.command.info.CommandInfo;
import com.br.gabrielmartins.syntri.SyntriPlugin;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@CommandInfo(names = {"spawn", "setspawn"}, permission = {"syntri.spawn"})
public class SpawnCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        MessagesManager mm = SyntriPlugin.getInstance().getMessagesManager();

        if (!(sender instanceof Player)) {
            sender.sendMessage(mm.getMessage("spawn.only_players"));
            return true;
        }

        Player player = (Player) sender;

        switch (label.toLowerCase()) {
            case "setspawn":
                if (!player.hasPermission("syntri.setspawn")) {
                    player.sendMessage(mm.getMessage("spawn.no_permission_set"));
                    return true;
                }
                DataTable.saveSpawn(player);
                player.sendMessage(mm.getMessage("spawn.set_success"));
                return true;

            case "spawn":
                if (!player.hasPermission("syntri.spawn")) {
                    player.sendMessage(mm.getMessage("spawn.no_permission_teleport"));
                    return true;
                }
                DataTable.teleportToSpawn(player);
                return true;

            default:
                return false;
        }
    }
}
