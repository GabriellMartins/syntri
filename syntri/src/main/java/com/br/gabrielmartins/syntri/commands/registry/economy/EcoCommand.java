package com.br.gabrielmartins.syntri.commands.registry.economy;

import com.br.gabrielmartins.syntri.MessagesManager;
import com.br.gabrielmartins.engine.loader.command.info.CommandInfo;
import com.br.gabrielmartins.syntri.SyntriPlugin;

import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

@CommandInfo(names = {"eco"}, permission = {"syntri.eco"})
public class EcoCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        MessagesManager mm = SyntriPlugin.getInstance().getMessagesManager();

        if (!sender.hasPermission("syntri.eco")) {
            sender.sendMessage(mm.getMessage("eco.no_permission"));
            return true;
        }

        if (args.length != 3) {
            sender.sendMessage(mm.getMessage("eco.usage"));
            return true;
        }

        String action = args[0].toLowerCase();
        OfflinePlayer target = Bukkit.getOfflinePlayer(args[1]);

        double amount;
        try {
            amount = Double.parseDouble(args[2]);
        } catch (NumberFormatException e) {
            sender.sendMessage(mm.getMessage("eco.invalid_value"));
            return true;
        }

        if (amount < 0) {
            sender.sendMessage(mm.getMessage("eco.negative_value"));
            return true;
        }

        Economy economy = SyntriPlugin.getInstance().getEconomy();
        if (economy == null) {
            sender.sendMessage(mm.getMessage("eco.vault_disabled"));
            return true;
        }

        String formattedAmount = String.format("%.2f", amount);
        String playerName = target.getName();

        switch (action) {
            case "set":
                double current = economy.getBalance(target);
                if (amount > current) {
                    economy.depositPlayer(target, amount - current);
                } else {
                    economy.withdrawPlayer(target, current - amount);
                }
                sender.sendMessage(mm.getMessage("eco.set_success")
                        .replace("%player%", playerName)
                        .replace("%amount%", formattedAmount));
                break;

            case "add":
                economy.depositPlayer(target, amount);
                sender.sendMessage(mm.getMessage("eco.add_success")
                        .replace("%player%", playerName)
                        .replace("%amount%", formattedAmount));
                break;

            case "remove":
                economy.withdrawPlayer(target, amount);
                sender.sendMessage(mm.getMessage("eco.remove_success")
                        .replace("%player%", playerName)
                        .replace("%amount%", formattedAmount));
                break;

            default:
                sender.sendMessage(mm.getMessage("eco.invalid_action"));
        }

        return true;
    }
}
