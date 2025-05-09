package com.br.gabrielmartins.syntri.commands.registry;

import com.br.gabrielmartins.syntri.MessagesManager;
import com.br.gabrielmartins.engine.data.controller.DataHandler;
import com.br.gabrielmartins.engine.loader.command.info.CommandInfo;
import com.br.gabrielmartins.syntri.SyntriPlugin;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@CommandInfo(names = {"setwarp"}, permission = {"syntri.setwarp"})
public class SetWarpCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        MessagesManager mm = SyntriPlugin.getInstance().getMessagesManager();

        if (!(sender instanceof Player)) {
            sender.sendMessage(mm.getMessage("setwarp.only_players"));
            return true;
        }

        Player player = (Player) sender;

        if (!player.hasPermission("syntri.setwarp")) {
            player.sendMessage(mm.getMessage("setwarp.no_permission"));
            return true;
        }

        if (args.length == 0) {
            player.sendMessage(mm.getMessage("setwarp.usage"));
            return false;
        }

        String warpName = args[0];
        DataHandler.saveWarp(player, warpName);
        player.sendMessage(mm.getMessage("setwarp.success").replace("%warp%", warpName));
        return true;
    }
}
