package com.br.gabrielmartins.syntri.commands.registry;

import com.br.gabrielmartins.engine.loader.command.info.CommandInfo;
import com.br.gabrielmartins.engine.utils.server.ServerStats;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@CommandInfo(names = {"dev"}, permission = {"syntri.dev"})
public class DebugCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("§cApenas jogadores podem usar este comando.");
            return true;
        }

        Player player = (Player) sender;

        if (!player.hasPermission("syntri.debug")) {
            player.sendMessage("§cVocê não tem permissão para este comando.");
            return true;
        }

        double tps = ServerStats.getTPS();

        player.sendMessage("§8§m--------------------------------------------------");
        player.sendMessage("§e[§6Syntri Debug§e]");
        player.sendMessage("§fTPS: " + ServerStats.formatTPS(tps));
        player.sendMessage("§fMemória: §a" + ServerStats.getMemoryUsage());
        player.sendMessage("§fJogadores: §a" + ServerStats.getOnlinePlayers() + "§7/§f" + ServerStats.getMaxPlayers());
        player.sendMessage("§fServidor: §7" + Bukkit.getServer().getVersion());
        player.sendMessage("§fModo: " + ServerStats.getOnlineMode());
        player.sendMessage("§8§m--------------------------------------------------");
        return true;
    }

}
