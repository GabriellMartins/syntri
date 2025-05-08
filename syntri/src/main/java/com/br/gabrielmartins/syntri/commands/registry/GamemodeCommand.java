package com.br.gabrielmartins.syntri.commands.registry;

import com.br.gabrielmartins.syntri.commands.CommandInfo;
import com.br.gabrielmartins.syntri.api.translate.Translate;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@CommandInfo(names = {"gm", "gamemode"}, permission = {"syntri.gamemode"})
public class GamemodeCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(Translate.get("commands.gamemode.only_players"));
            return true;
        }

        Player player = (Player) sender;

        if (!player.hasPermission("syntri.gamemode")) {
            player.sendMessage(Translate.get("commands.gamemode.no_permission"));
            return true;
        }

        if (args.length == 0) {
            player.sendMessage(Translate.get("commands.gamemode.provide_mode"));
            return false;
        }

        try {
            int mode = Integer.parseInt(args[0]);

            switch (mode) {
                case 1:
                    player.setGameMode(GameMode.CREATIVE);
                    player.sendMessage(Translate.get("commands.gamemode.creative"));
                    break;
                case 2:
                    player.setGameMode(GameMode.SURVIVAL);
                    player.sendMessage(Translate.get("commands.gamemode.survival"));
                    break;
                case 3:
                    player.setGameMode(GameMode.SPECTATOR);
                    player.sendMessage(Translate.get("commands.gamemode.spectator"));
                    break;
                default:
                    player.sendMessage(Translate.get("commands.gamemode.invalid_mode"));
                    break;
            }

        } catch (NumberFormatException e) {
            player.sendMessage(Translate.get("commands.gamemode.invalid_number"));
        }

        return true;
    }
}
