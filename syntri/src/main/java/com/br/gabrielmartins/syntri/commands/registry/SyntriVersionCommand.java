package com.br.gabrielmartins.syntri.commands.registry;

import com.br.gabrielmartins.syntri.MessagesManager;
import com.br.gabrielmartins.engine.loader.command.info.CommandInfo;
import com.br.gabrielmartins.syntri.SyntriPlugin;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

@CommandInfo(names = {"version"}, permission = {"syntri.version"})
public class SyntriVersionCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        MessagesManager mm = SyntriPlugin.getInstance().getMessagesManager();

        if (!sender.hasPermission("syntri.version")) {
            sender.sendMessage(mm.getMessage("version.no_permission"));
            return true;
        }

        String version = SyntriPlugin.getInstance().getDescription().getVersion();
        sender.sendMessage(mm.getMessage("version.display").replace("%version%", version));
        return true;
    }
}
