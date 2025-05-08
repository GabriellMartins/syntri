package com.br.gabrielmartins.syntri.commands.registry;

import com.br.gabrielmartins.engine.loader.command.info.CommandInfo;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@CommandInfo(names = {"ping"}, permission = {"syntri.tp"})
public class PingCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (!(sender instanceof Player)) {
            sender.sendMessage("§cApenas jogadores podem usar este comando.");
            return true;
        }

        Player player = (Player) sender;
        int ping = getPing(player);

        String status;
        String color;

        if (ping <= 80) {
            status = "§a§lPing Estável";
            color = "§a";
        } else if (ping <= 160) {
            status = "§e§lPing Instável";
            color = "§e";
        } else {
            status = "§c§lPing Ruim";
            color = "§c";
        }

        player.sendMessage("§8§m-------------------------");
        player.sendMessage("      §b§lSYNTRI PING");
        player.sendMessage(" ");
        player.sendMessage(" §fSeu ping: " + color + ping + "ms");
        player.sendMessage(" §fStatus: " + status);
        player.sendMessage("§8§m-------------------------");

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
