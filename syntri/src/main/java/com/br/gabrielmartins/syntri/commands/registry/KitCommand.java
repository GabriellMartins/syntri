package com.br.gabrielmartins.syntri.commands.registry;

import com.br.gabrielmartins.syntri.MessagesManager;
import com.br.gabrielmartins.engine.loader.command.info.CommandInfo;
import com.br.gabrielmartins.syntri.SyntriPlugin;
import com.br.gabrielmartins.syntri.kit.Kit;
import com.br.gabrielmartins.syntri.kit.manager.KitManager;
import com.br.gabrielmartins.syntri.kit.util.KitUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@CommandInfo(names = {"kit"}, permission = {"syntri.kit"})
public class KitCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        MessagesManager mm = SyntriPlugin.getInstance().getMessagesManager();

        if (!(sender instanceof Player)) {
            sender.sendMessage(mm.getMessage("kit.only_players"));
            return true;
        }

        Player player = (Player) sender;

        if (args.length == 0) {
            player.sendMessage(mm.getMessage("kit.available_title"));
            for (Kit kit : KitManager.getAllKits()) {
                if (kit.getPermission() == null || player.hasPermission(kit.getPermission())) {
                    player.sendMessage(mm.getMessage("kit.available_format").replace("%kit%", kit.getName()));
                }
            }
            player.sendMessage(mm.getMessage("kit.available_footer"));
            return true;
        }

        Kit kit = KitManager.getKit(args[0]);

        if (kit == null) {
            player.sendMessage(mm.getMessage("kit.not_found"));
            return true;
        }

        if (kit.getPermission() != null && !player.hasPermission(kit.getPermission())) {
            player.sendMessage(mm.getMessage("kit.no_permission"));
            return true;
        }

        if (KitManager.isOnCooldown(kit.getName(), player.getUniqueId())) {
            long seconds = KitManager.getRemainingCooldown(kit.getName(), player.getUniqueId()) / 1000L;
            player.sendMessage(mm.getMessage("kit.on_cooldown").replace("%time%", formatSeconds(seconds)));
            return true;
        }

        boolean success = KitUtils.giveKitItems(player, kit);
        if (!success) return true;

        KitManager.setCooldown(kit.getName(), player.getUniqueId(), kit.getCooldownMillis());
        player.sendMessage(mm.getMessage("kit.received").replace("%kit%", kit.getName()));
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
