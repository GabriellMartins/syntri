package com.br.gabrielmartins.syntri.commands.registry.economy;

import com.br.gabrielmartins.syntri.MessagesManager;
import com.br.gabrielmartins.engine.loader.command.info.CommandInfo;
import com.br.gabrielmartins.syntri.SyntriPlugin;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@CommandInfo(names = {"pay", "enviar", "pagar"}, permission = {"syntri.pay"})
public class PayCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        MessagesManager mm = SyntriPlugin.getInstance().getMessagesManager();

        if (!(sender instanceof Player)) {
            sender.sendMessage("§cOnly players can use this command.");
            return true;
        }

        Player from = (Player) sender;

        if (!from.hasPermission("syntri.pay")) {
            from.sendMessage(mm.getMessage("pay.no_permission"));
            return true;
        }

        if (args.length != 2) {
            from.sendMessage(mm.getMessage("pay.usage"));
            return true;
        }

        Player to = Bukkit.getPlayer(args[0]);
        if (to == null || !to.isOnline()) {
            from.sendMessage(mm.getMessage("pay.player_not_found"));
            return true;
        }

        if (from.equals(to)) {
            from.sendMessage(mm.getMessage("pay.self_payment"));
            return true;
        }

        double amount;
        try {
            amount = Double.parseDouble(args[1]);
        } catch (NumberFormatException e) {
            from.sendMessage(mm.getMessage("pay.invalid_value"));
            return true;
        }

        if (amount <= 0) {
            from.sendMessage(mm.getMessage("pay.must_be_positive"));
            return true;
        }

        Economy economy = SyntriPlugin.getInstance().getEconomy();
        if (economy == null) {
            from.sendMessage(mm.getMessage("pay.vault_disabled"));
            return true;
        }

        if (!economy.has(from, amount)) {
            from.sendMessage(mm.getMessage("pay.insufficient_balance"));
            return true;
        }

        economy.withdrawPlayer(from, amount);
        economy.depositPlayer(to, amount);

        String formattedAmount = String.format("%.2f", amount);

        from.sendMessage(mm.getMessage("pay.sent")
                .replace("%amount%", formattedAmount)
                .replace("%target%", to.getName()));

        to.sendMessage(mm.getMessage("pay.received")
                .replace("%amount%", formattedAmount)
                .replace("%sender%", from.getName()));

        return true;
    }
}
