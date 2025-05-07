package com.br.gabrielmartins.syntri.commands.registry;

import com.br.gabrielmartins.syntri.api.actionbar.ActionBarUtil;
import com.br.gabrielmartins.syntri.commands.CommandInfo;
import org.bukkit.command.*;
import org.bukkit.entity.Player;

import java.util.*;

@CommandInfo(names = {"god"}, permission = {"syntri.god"})
public class GodCommand implements CommandExecutor {

    private static final Set<UUID> godPlayers = new HashSet<>();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("§cApenas jogadores podem usar este comando.");
            return true;
        }

        Player player = (Player) sender;

        if (!player.hasPermission("syntri.god")) {
            player.sendMessage("§cVocê não tem permissão para usar este comando.");
            return true;
        }

        UUID uuid = player.getUniqueId();
        boolean enabled = godPlayers.add(uuid);
        if (!enabled) {
            godPlayers.remove(uuid);
        }

        player.sendMessage("§eModo Deus " + (enabled ? "§aativado§e!" : "§cdesativado§e!"));

        ActionBarUtil.send(player, enabled ? "§aModo Deus ATIVADO!" : "§cModo Deus DESATIVADO!");

        return true;
    }

    public static boolean isGod(Player player) {
        return godPlayers.contains(player.getUniqueId());
    }
}
