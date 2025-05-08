package com.br.gabrielmartins.syntri.commands.registry;

import com.br.gabrielmartins.syntri.SyntriPlugin;
import com.br.gabrielmartins.syntri.commands.CommandInfo;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

@CommandInfo(names = {"syntriversion"}, permission = {"syntri.version"})
public class SyntriVersionCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (args.length == 1 && args[0].equalsIgnoreCase("version")) {
            if (!sender.hasPermission("syntri.version")) {
                sender.sendMessage("§cVocê não tem permissão para ver a versão.");
                return true;
            }

            String version = SyntriPlugin.getInstance().getDescription().getVersion();
            sender.sendMessage("§6Syntri §fv" + version + " §7- O essencial do seu servidor.");
            return true;
        }

        sender.sendMessage("§cUso correto: /syntri version");
        return true;
    }
}
