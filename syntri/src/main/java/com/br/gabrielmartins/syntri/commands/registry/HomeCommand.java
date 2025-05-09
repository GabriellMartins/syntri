package com.br.gabrielmartins.syntri.commands.registry;

import com.br.gabrielmartins.syntri.MessagesManager;
import com.br.gabrielmartins.engine.data.table.DataTable;
import com.br.gabrielmartins.engine.loader.command.info.CommandInfo;
import com.br.gabrielmartins.syntri.SyntriPlugin;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@CommandInfo(names = {"home", "casa"}, permission = {"syntri.home"})
public class HomeCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        MessagesManager mm = SyntriPlugin.getInstance().getMessagesManager();

        if (!(sender instanceof Player)) {
            sender.sendMessage(mm.getMessage("home.only_players"));
            return true;
        }

        Player player = (Player) sender;

        if (!player.hasPermission("syntri.home")) {
            player.sendMessage(mm.getMessage("home.no_permission"));
            return true;
        }

        if (args.length == 0) {
            DataTable.HOME.listHomes(player);
            return true;
        }

        String homeName = args[0];
        DataTable.HOME.teleportToHome(player, homeName);
        return true;
    }
}
