package com.br.gabrielmartins.syntri.commands.registry;

import com.br.gabrielmartins.syntri.MessagesManager;
import com.br.gabrielmartins.engine.loader.command.info.CommandInfo;
import com.br.gabrielmartins.syntri.SyntriPlugin;
import com.br.gabrielmartins.syntri.loan.manager.LoanManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@CommandInfo(names = {"emprestimo", "loan"}, permission = {"syntri.loan"})
public class LoanCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        MessagesManager mm = SyntriPlugin.getInstance().getMessagesManager();

        if (!(sender instanceof Player player)) {
            sender.sendMessage(mm.getMessage("loan.only_players"));
            return true;
        }

        if (!player.hasPermission("syntri.loan")) {
            player.sendMessage(mm.getMessage("loan.no_permission"));
            return true;
        }

        LoanManager.openLoanMenu(player);
        return true;
    }
}
