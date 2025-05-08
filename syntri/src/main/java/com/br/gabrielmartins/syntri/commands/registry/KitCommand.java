package com.br.gabrielmartins.syntri.commands.registry;

import com.br.gabrielmartins.engine.kit.Kit;
import com.br.gabrielmartins.engine.kit.manager.KitManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class KitCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("§cApenas jogadores podem usar este comando.");
            return true;
        }

        Player player = (Player) sender;

        if (args.length == 0) {
            player.sendMessage("§8§m-----------------------------");
            player.sendMessage("     §e§lKITS DISPONÍVEIS");
            for (Kit kit : KitManager.getAllKits()) {
                if (kit.getPermission() == null || player.hasPermission(kit.getPermission())) {
                    player.sendMessage(" §a➤ §f" + kit.getName());
                }
            }
            player.sendMessage("§7Use §e/kit <nome>§7 para receber um kit.");
            player.sendMessage("§8§m-----------------------------");
            return true;
        }

        Kit kit = KitManager.getKit(args[0]);

        if (kit == null) {
            player.sendMessage("§cKit não encontrado.");
            return true;
        }

        if (kit.getPermission() != null && !player.hasPermission(kit.getPermission())) {
            player.sendMessage("§cVocê não tem permissão para este kit.");
            return true;
        }

        if (KitManager.isOnCooldown(kit.getName(), player.getUniqueId())) {
            long seconds = KitManager.getRemainingCooldown(kit.getName(), player.getUniqueId()) / 1000L;
            player.sendMessage("§cVocê precisa esperar §e" + formatSeconds(seconds) + "§c para pegar esse kit novamente.");
            return true;
        }

        player.getInventory().setContents(kit.getContents().toArray(new ItemStack[0]));
        player.getInventory().setArmorContents(kit.getArmor().toArray(new ItemStack[0]));

        KitManager.setCooldown(kit.getName(), player.getUniqueId(), kit.getCooldownMillis());
        player.sendMessage("§aVocê recebeu o kit §e" + kit.getName() + "§a!");
        return true;
    }

    private String formatSeconds(long totalSeconds) {
        long days = totalSeconds / 86400;
        long hours = (totalSeconds % 86400) / 3600;
        long minutes = (totalSeconds % 3600) / 60;
        long seconds = totalSeconds % 60;

        StringBuilder sb = new StringBuilder();
        if (days > 0) sb.append(days).append("d ");
        if (hours > 0) sb.append(hours).append("h ");
        if (minutes > 0) sb.append(minutes).append("m ");
        if (seconds > 0 || sb.length() == 0) sb.append(seconds).append("s");

        return sb.toString().trim();
    }
}