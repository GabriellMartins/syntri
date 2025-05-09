package com.br.gabrielmartins.syntri.commands.registry;

import com.br.gabrielmartins.syntri.MessagesManager;
import com.br.gabrielmartins.engine.loader.command.info.CommandInfo;
import com.br.gabrielmartins.engine.utils.server.ServerStats;
import com.br.gabrielmartins.syntri.SyntriPlugin;

import org.bukkit.Bukkit;
import org.bukkit.command.*;
import org.bukkit.entity.Player;

@CommandInfo(names = {"dev"}, permission = {"syntri.dev"})
public class DebugCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        MessagesManager mm = SyntriPlugin.getInstance().getMessagesManager();

        if (!(sender instanceof Player)) {
            sender.sendMessage(mm.getMessage("debug.only_players"));
            return true;
        }

        Player player = (Player) sender;

        if (!player.hasPermission("syntri.debug")) {
            player.sendMessage(mm.getMessage("debug.no_permission"));
            return true;
        }

        double tps = ServerStats.getTPS();
        String memory = ServerStats.getMemoryUsage();
        int online = ServerStats.getOnlinePlayers();
        int max = ServerStats.getMaxPlayers();
        String version = Bukkit.getServer().getVersion();
        String mode = ServerStats.getOnlineMode();

        player.sendMessage(mm.getMessage("debug.header"));
        player.sendMessage(mm.getMessage("debug.title"));
        player.sendMessage(mm.getMessage("debug.tps").replace("%tps%", ServerStats.formatTPS(tps)));
        player.sendMessage(mm.getMessage("debug.memory").replace("%memory%", memory));
        player.sendMessage(mm.getMessage("debug.players").replace("%online%", String.valueOf(online)).replace("%max%", String.valueOf(max)));
        player.sendMessage(mm.getMessage("debug.version").replace("%version%", version));
        player.sendMessage(mm.getMessage("debug.mode").replace("%mode%", mode));
        player.sendMessage(mm.getMessage("debug.footer"));

        return true;
    }
}
