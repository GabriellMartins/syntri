package com.br.gabrielmartins.syntri.commands.registry;

import com.br.gabrielmartins.engine.kit.manager.KitManager;
import com.br.gabrielmartins.engine.loader.command.info.CommandInfo;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@CommandInfo(names = {"createkit"}, permission = {"syntri.createkit"})
public class CreateKitCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("§cApenas jogadores podem usar este comando.");
            return true;
        }

        if (args.length < 3) {
            sender.sendMessage("§cUso correto: /createkit <nome> <permissao> <tempo>");
            return true;
        }

        String name = args[0];
        String permission = args[1];
        String time = args[2];

        Player player = (Player) sender;
        KitManager.createKit(player, name, permission, time);

        player.sendMessage("§aKit §e" + name + "§a criado com sucesso com permissão §e" + permission + "§a e cooldown de §e" + time + "§a.");
        return true;
    }
}
