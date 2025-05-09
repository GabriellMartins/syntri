package com.br.gabrielmartins.syntri.commands.registry.economy;

import com.br.gabrielmartins.syntri.MessagesManager;
import com.br.gabrielmartins.engine.loader.command.info.CommandInfo;
import com.br.gabrielmartins.syntri.SyntriPlugin;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.*;
import java.util.List;
import java.util.UUID;

@CommandInfo(names = {"topmoney", "baltop", "moneytop"}, permission = {"syntri.topmoney"})
public class TopMoneyCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        MessagesManager mm = SyntriPlugin.getInstance().getMessagesManager();

        if (!sender.hasPermission("syntri.topmoney")) {
            sender.sendMessage(mm.getMessage("topmoney.no_permission"));
            return true;
        }

        List<UUID> topList = SyntriPlugin.getInstance().getTopMoneyCache().getTop10();

        sender.sendMessage(mm.getMessage("topmoney.header"));

        if (topList.isEmpty()) {
            sender.sendMessage(mm.getMessage("topmoney.empty"));
            sender.sendMessage(mm.getMessage("topmoney.footer"));
            return true;
        }

        int i = 1;
        for (UUID uuid : topList) {
            OfflinePlayer player = Bukkit.getOfflinePlayer(uuid);
            double balance = SyntriPlugin.getInstance().getTopMoneyCache().getBalanceMap().getOrDefault(uuid, 0.0);
            String msg = mm.getMessage("topmoney.entry")
                    .replace("%position%", String.valueOf(i++))
                    .replace("%player%", player.getName() != null ? player.getName() : "Unknown")
                    .replace("%balance%", String.format("%.2f", balance));
            sender.sendMessage(msg);
        }

        sender.sendMessage(mm.getMessage("topmoney.footer"));
        return true;
    }
}
