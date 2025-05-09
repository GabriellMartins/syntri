package com.br.gabrielmartins.syntri.commands.registry;

import com.br.gabrielmartins.syntri.MessagesManager;
import com.br.gabrielmartins.engine.loader.command.info.CommandInfo;
import com.br.gabrielmartins.syntri.SyntriPlugin;

import com.br.gabrielmartins.syntri.kit.manager.KitManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@CommandInfo(names = {"createkit"}, permission = {"syntri.createkit"})
public class CreateKitCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        MessagesManager mm = SyntriPlugin.getInstance().getMessagesManager();

        if (!(sender instanceof Player)) {
            sender.sendMessage(mm.getMessage("createkit.only_players"));
            return true;
        }

        if (args.length < 3) {
            sender.sendMessage(mm.getMessage("createkit.usage"));
            return true;
        }

        String name = args[0];
        String permission = args[1];
        String time = args[2];

        Player player = (Player) sender;
        KitManager.createKit(player, name, permission, time);

        player.sendMessage(
                mm.getMessage("createkit.success")
                        .replace("%kit%", name)
                        .replace("%permission%", permission)
                        .replace("%cooldown%", time)
        );

        return true;
    }
}
