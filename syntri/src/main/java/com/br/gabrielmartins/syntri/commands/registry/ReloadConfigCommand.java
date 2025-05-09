package com.br.gabrielmartins.syntri.commands.registry;

import com.br.gabrielmartins.engine.loader.command.info.CommandInfo;
import com.br.gabrielmartins.syntri.SyntriPlugin;
import com.br.gabrielmartins.syntri.MessagesManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

@CommandInfo(names = {"reloadconfig", "reload"}, permission = {"syntri.reload"})
public class ReloadConfigCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        MessagesManager mm = SyntriPlugin.getInstance().getMessagesManager();

        if (!sender.hasPermission("syntri.reload")) {
            sender.sendMessage(mm.getMessage("reload.no_permission"));
            return true;
        }

        SyntriPlugin.getInstance().reloadConfig();
        SyntriPlugin.getInstance().getMessagesManager().reloadMessages();
        sender.sendMessage(mm.getMessage("reload.success"));
        return true;
    }
}
