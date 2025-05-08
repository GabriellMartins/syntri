package com.br.gabrielmartins.syntri.commands.registry;

import com.br.gabrielmartins.engine.data.controller.DataHandler;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class WarpCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (!(sender instanceof Player)) {
            sender.sendMessage("§cApenas jogadores podem usar este comando.");
            return true;
        }

        var player = (Player) sender;

        if (!player.hasPermission("syntri.warp")) {
            player.sendMessage("§cVocê não tem permissão para usar este comando.");
            return true;
        }

        if (args.length == 0) {
            DataHandler.listWarps(player);
            return true;
        }

        var warpName = args[0];
        DataHandler.teleportToWarp(player, warpName);
        return true;
    }
}
