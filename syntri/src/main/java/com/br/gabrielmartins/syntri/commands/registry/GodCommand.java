package com.br.gabrielmartins.syntri.commands.registry;

import com.br.gabrielmartins.engine.api.actionbar.ActionBarUtil;
import com.br.gabrielmartins.syntri.MessagesManager;
import com.br.gabrielmartins.engine.loader.command.info.CommandInfo;
import com.br.gabrielmartins.syntri.SyntriPlugin;
import org.bukkit.command.Command;

import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@CommandInfo(names = {"god", "deus"}, permission = {"syntri.god"})
public class GodCommand implements CommandExecutor {

    private static final Set<UUID> godPlayers = new HashSet<>();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        MessagesManager mm = SyntriPlugin.getInstance().getMessagesManager();

        if (!(sender instanceof Player)) {
            sender.sendMessage(mm.getMessage("god.only_players"));
            return true;
        }

        Player player = (Player) sender;

        if (!player.hasPermission("syntri.god")) {
            player.sendMessage(mm.getMessage("god.no_permission"));
            return true;
        }

        UUID uuid = player.getUniqueId();
        boolean enabled = godPlayers.add(uuid);
        if (!enabled) {
            godPlayers.remove(uuid);
        }

        player.sendMessage(enabled ? mm.getMessage("god.enabled") : mm.getMessage("god.disabled"));
        ActionBarUtil.INSTANCE.send(player, enabled ? mm.getMessage("god.actionbar_enabled") : mm.getMessage("god.actionbar_disabled"));

        return true;
    }

    public static boolean isGod(Player player) {
        return godPlayers.contains(player.getUniqueId());
    }
}
