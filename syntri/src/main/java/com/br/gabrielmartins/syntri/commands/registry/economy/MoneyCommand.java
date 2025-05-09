package com.br.gabrielmartins.syntri.commands.registry.economy;

import com.br.gabrielmartins.engine.loader.command.info.CommandInfo;
import com.br.gabrielmartins.syntri.SyntriPlugin;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import net.milkbowl.vault.economy.Economy;
import java.util.List;

@CommandInfo(names = {"money", "bal", "saldo"}, permission = {"syntri.money"})
public class MoneyCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (!(sender instanceof Player)) {
            sender.sendMessage("§cOnly players can use this command.");
            return true;
        }

        Player player = (Player) sender;

        if (!player.hasPermission("syntri.money")) {
            player.sendMessage(SyntriPlugin.getInstance().getMessagesManager().getMessage("money.no_permission"));
            return true;
        }

        Economy economy = SyntriPlugin.getInstance().getEconomy();
        if (economy == null) {
            player.sendMessage(SyntriPlugin.getInstance().getMessagesManager().getMessage("money.vault_disabled"));
            return true;
        }

        double balance = economy.getBalance(player);
        String formatted = String.format("%.2f", balance);

        List<String> lines = SyntriPlugin.getInstance().getMessagesManager().getMessageList("money.balance_message");
        for (String line : lines) {
            player.sendMessage(line.replace("%balance%", formatted));
        }

        return true;
    }
}
