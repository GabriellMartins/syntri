package com.br.gabrielmartins.syntri.commands.registry;

import com.br.gabrielmartins.syntri.MessagesManager;
import com.br.gabrielmartins.engine.data.controller.DataHandler;
import com.br.gabrielmartins.engine.loader.command.info.CommandInfo;
import com.br.gabrielmartins.syntri.SyntriPlugin;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@CommandInfo(names = {"sethome"}, permission = {"syntri.sethome"})
public class SetHomeCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        MessagesManager mm = SyntriPlugin.getInstance().getMessagesManager();

        if (!(sender instanceof Player)) {
            sender.sendMessage(mm.getMessage("sethome.only_players"));
            return true;
        }

        Player player = (Player) sender;

        if (!player.hasPermission("syntri.sethome")) {
            player.sendMessage(mm.getMessage("sethome.no_permission"));
            return true;
        }

        if (args.length == 0) {
            player.sendMessage(mm.getMessage("sethome.no_name"));
            return false;
        }

        String homeName = args[0];
        DataHandler.saveHome(player, homeName);

        player.sendMessage(mm.getMessage("sethome.success").replace("%home%", homeName));
        return true;
    }
}
