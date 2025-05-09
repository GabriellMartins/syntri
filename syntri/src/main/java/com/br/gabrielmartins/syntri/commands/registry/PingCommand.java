package com.br.gabrielmartins.syntri.commands.registry;

import com.br.gabrielmartins.engine.loader.command.info.CommandInfo;
import com.br.gabrielmartins.syntri.MessagesManager;
import com.br.gabrielmartins.syntri.SyntriPlugin;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@CommandInfo(names = {"ping"}, permission = {"syntri.ping"})
public class PingCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        com.br.gabrielmartins.syntri.MessagesManager mm = SyntriPlugin.getInstance().getMessagesManager();

        if (!(sender instanceof Player)) {
            sender.sendMessage(mm.getMessage("ping.only_players"));
            return true;
        }

        Player player = (Player) sender;
        int ping = getPing(player);

        String status, color;
        if (ping <= 80) {
            status = mm.getMessage("ping.status_levels.good");
            color = "§a";
        } else if (ping <= 160) {
            status = mm.getMessage("ping.status_levels.medium");
            color = "§e";
        } else {
            status = mm.getMessage("ping.status_levels.bad");
            color = "§c";
        }

        player.sendMessage(mm.getMessage("ping.header"));
        player.sendMessage(mm.getMessage("ping.value")
                .replace("%ping%", String.valueOf(ping))
                .replace("%color%", color));
        player.sendMessage(mm.getMessage("ping.status")
                .replace("%status%", status));
        player.sendMessage(mm.getMessage("ping.footer"));

        return true;
    }

    private int getPing(Player player) {
        try {
            Object handle = player.getClass().getMethod("getHandle").invoke(player);
            return (int) handle.getClass().getField("ping").get(handle);
        } catch (Exception e) {
            return -1;
        }
    }
}
