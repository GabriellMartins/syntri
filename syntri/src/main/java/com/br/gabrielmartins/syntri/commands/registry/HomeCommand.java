package com.br.gabrielmartins.syntri.commands.registry;

import com.br.gabrielmartins.engine.data.table.DataTable;
import com.br.gabrielmartins.engine.loader.command.info.CommandInfo;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@CommandInfo(names = {"home", "casa"}, permission = {"syntri.home"})
public class HomeCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("§cApenas jogadores podem usar este comando.");
            return true;
        }

        Player player = (Player) sender;

        if (!player.hasPermission("syntri.home")) {
            player.sendMessage("§cVocê não tem permissão para usar este comando.");
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
