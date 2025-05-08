package com.br.gabrielmartins.syntri.commands.registry;

import com.br.gabrielmartins.engine.data.controller.DataHandler;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SetHomeCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("§cApenas jogadores podem usar este comando.");
            return true;
        }

        Player player = (Player) sender;

        if (!player.hasPermission("syntri.sethome")) {
            player.sendMessage("§cVocê não tem permissão para usar este comando.");
            return true;
        }

        if (args.length == 0) {
            player.sendMessage("§cPor favor, forneça um nome para sua casa.");
            return false;
        }

        String homeName = args[0];

        DataHandler.saveHome(player, homeName);

        player.sendMessage("§aVocê salvou sua casa com o nome: " + homeName);
        return true;
    }
}
