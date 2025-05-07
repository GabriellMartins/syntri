package com.br.gabrielmartins.syntri.commands.registry;

import com.br.gabrielmartins.syntri.commands.CommandInfo;
import com.br.gabrielmartins.syntri.data.controller.DataHandler;
import lombok.var;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@CommandInfo(names = {"setwarp"}, permission = {"syntri.setwarp"})
public class SetWarpCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("§cApenas jogadores podem usar este comando.");
            return true;
        }

        var player = (Player) sender;

        if (!player.hasPermission("syntri.setwarp")) {
            player.sendMessage("§cVocê não tem permissão para usar este comando.");
            return true;
        }

        if (args.length == 0) {
            player.sendMessage("§cUse: /setwarp <nome>");
            return false;
        }

        var warpName = args[0];
        DataHandler.saveWarp(player, warpName);
        player.sendMessage("§aWarp '" + warpName + "' salva com sucesso.");
        return true;
    }
}
