package com.br.gabrielmartins.syntri.commands.registry;

import com.br.gabrielmartins.syntri.MessagesManager;
import com.br.gabrielmartins.engine.loader.command.info.CommandInfo;
import com.br.gabrielmartins.syntri.SyntriPlugin;

import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@CommandInfo(names = {"gamemode", "gm"}, permission = {"syntri.gm"})
public class GamemodeCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        MessagesManager mm = SyntriPlugin.getInstance().getMessagesManager();

        if (!(sender instanceof Player)) {
            sender.sendMessage(mm.getMessage("gamemode.only_players"));
            return true;
        }

        Player player = (Player) sender;

        if (!player.hasPermission("syntri.gamemode")) {
            player.sendMessage(mm.getMessage("gamemode.no_permission"));
            return true;
        }

        if (args.length == 0) {
            for (String line : mm.getMessages().getStringList("gamemode.usage")) {
                player.sendMessage(line);
            }
            return false;
        }

        try {
            int mode = Integer.parseInt(args[0]);

            switch (mode) {
                case 0:
                    player.setGameMode(GameMode.ADVENTURE);
                    player.sendMessage(mm.getMessage("gamemode.adventure"));
                    break;
                case 1:
                    player.setGameMode(GameMode.CREATIVE);
                    player.sendMessage(mm.getMessage("gamemode.creative"));
                    break;
                case 2:
                    player.setGameMode(GameMode.SURVIVAL);
                    player.sendMessage(mm.getMessage("gamemode.survival"));
                    break;
                case 3:
                    player.setGameMode(GameMode.SPECTATOR);
                    player.sendMessage(mm.getMessage("gamemode.spectator"));
                    break;
                default:
                    player.sendMessage(mm.getMessage("gamemode.invalid_mode"));
                    break;
            }

        } catch (NumberFormatException e) {
            player.sendMessage(mm.getMessage("gamemode.invalid_number"));
        }

        return true;
    }
}
