package com.br.gabrielmartins.syntri.commands.registry;

import com.br.gabrielmartins.syntri.MessagesManager;
import com.br.gabrielmartins.engine.data.controller.DataHandler;
import com.br.gabrielmartins.engine.loader.command.info.CommandInfo;
import com.br.gabrielmartins.syntri.SyntriPlugin;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@CommandInfo(names = {"warp"}, permission = {"syntri.warp"})
public class WarpCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        MessagesManager mm = SyntriPlugin.getInstance().getMessagesManager();

        if (!(sender instanceof Player)) {
            sender.sendMessage(mm.getMessage("warp.only_players"));
            return true;
        }

        Player player = (Player) sender;

        if (!player.hasPermission("syntri.warp")) {
            player.sendMessage(mm.getMessage("warp.no_permission"));
            return true;
        }

        if (args.length == 0) {
            DataHandler.listWarps(player);
            return true;
        }

        String warpName = args[0];
        DataHandler.teleportToWarp(player, warpName);
        return true;
    }
}
