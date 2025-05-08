package com.br.gabrielmartins.syntri.commands.registry;

import com.br.gabrielmartins.engine.api.cooldown.Cooldown;
import com.br.gabrielmartins.engine.utils.combat.CombatLogManager;
import com.br.gabrielmartins.syntri.SyntriPlugin;

import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class TeleportCommand implements CommandExecutor {

    private static final Map<UUID, UUID> pendingRequests = new HashMap<>();
    private static final Map<UUID, Long> requestTimestamps = new HashMap<>();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (!(sender instanceof Player)) {
            sender.sendMessage("§cApenas jogadores podem usar este comando.");
            return true;
        }

        Player player = (Player) sender;

        switch (label.toLowerCase()) {

            case "tp":
                if (!player.hasPermission("syntri.tp")) {
                    player.sendMessage("§cVocê não tem permissão para usar esse comando.");
                    return true;
                }
                if (args.length != 1) {
                    player.sendMessage("§cUso: /tp <jogador>");
                    return true;
                }
                Player target = Bukkit.getPlayerExact(args[0]);
                if (target == null || !target.isOnline()) {
                    player.sendMessage("§cJogador não encontrado.");
                    return true;
                }
                player.teleport(target);
                player.sendMessage("§aVocê foi teleportado para §e" + target.getName());
                return true;

            case "tpall":
                if (!player.hasPermission("syntri.tpall")) {
                    player.sendMessage("§cVocê não tem permissão para usar esse comando.");
                    return true;
                }
                for (Player online : Bukkit.getOnlinePlayers()) {
                    if (!online.equals(player)) {
                        online.teleport(player);
                        online.sendMessage("§eVocê foi teleportado para §6" + player.getName());
                    }
                }
                player.sendMessage("§aTodos os jogadores foram teleportados até você.");
                return true;

            case "tpa":
                if (args.length == 0) {
                    player.sendMessage("§cUso: /tpa <jogador> | /tpa aceitar | /tpa recusar");
                    return true;
                }

                String arg = args[0].toLowerCase();

                switch (arg) {
                    case "aceitar": {
                        UUID requester = null;
                        for (Map.Entry<UUID, UUID> entry : pendingRequests.entrySet()) {
                            if (entry.getValue().equals(player.getUniqueId())) {
                                requester = entry.getKey();
                                break;
                            }
                        }
                        if (requester == null) {
                            player.sendMessage("§cVocê não tem nenhum pedido de teleporte.");
                            return true;
                        }
                        Player from = Bukkit.getPlayer(requester);
                        if (from != null && from.isOnline()) {
                            from.teleport(player);
                            from.sendMessage("§aSeu pedido de teleporte foi aceito.");
                            player.sendMessage("§aVocê aceitou o pedido de §e" + from.getName());
                        }
                        pendingRequests.remove(requester);
                        requestTimestamps.remove(requester);
                        return true;
                    }

                    case "recusar": {
                        UUID senderId = null;
                        for (Map.Entry<UUID, UUID> entry : pendingRequests.entrySet()) {
                            if (entry.getValue().equals(player.getUniqueId())) {
                                senderId = entry.getKey();
                                break;
                            }
                        }
                        if (senderId == null) {
                            player.sendMessage("§cVocê não tem nenhum pedido de teleporte.");
                            return true;
                        }
                        Player senderPlayer = Bukkit.getPlayer(senderId);
                        if (senderPlayer != null && senderPlayer.isOnline()) {
                            senderPlayer.sendMessage("§cSeu pedido de teleporte foi recusado.");
                        }
                        pendingRequests.remove(senderId);
                        requestTimestamps.remove(senderId);
                        player.sendMessage("§cVocê recusou o pedido de teleporte.");
                        return true;
                    }

                    default: {
                        Player targetPlayer = Bukkit.getPlayerExact(arg);
                        if (targetPlayer == null || !targetPlayer.isOnline()) {
                            player.sendMessage("§cJogador não encontrado.");
                            return true;
                        }
                        if (targetPlayer.equals(player)) {
                            player.sendMessage("§cVocê não pode enviar tpa para si mesmo.");
                            return true;
                        }
                        if (CombatLogManager.isInCombat(player)) {
                            player.sendMessage("§cVocê está em combate! Espere para enviar pedidos de teleporte.");
                            return true;
                        }

                        if (Cooldown.INSTANCE.isCoolingDown(player)) {
                            Cooldown.INSTANCE.sendActionBar(player, "§cAguardando para reenviar tpa...");
                            return true;
                        }

                        pendingRequests.put(player.getUniqueId(), targetPlayer.getUniqueId());
                        requestTimestamps.put(player.getUniqueId(), System.currentTimeMillis());

                        player.sendMessage("§aPedido de tpa enviado para §e" + targetPlayer.getName());

                        TextComponent msg = new TextComponent("§e" + player.getName() + " quer teleportar até você. ");
                        TextComponent accept = new TextComponent("§a§l[ACEITAR]");
                        accept.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/tpa aceitar"));
                        TextComponent deny = new TextComponent(" §c§l[RECUSAR]");
                        deny.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/tpa recusar"));

                        msg.addExtra(accept);
                        msg.addExtra(deny);
                        targetPlayer.spigot().sendMessage(msg);

                        Cooldown.INSTANCE.start(SyntriPlugin.getInstance(), player, 15, "§c§lCooldown ");

                        new BukkitRunnable() {
                            @Override
                            public void run() {
                                if (pendingRequests.containsKey(player.getUniqueId())
                                        && System.currentTimeMillis() - requestTimestamps.get(player.getUniqueId()) >= 15000) {
                                    pendingRequests.remove(player.getUniqueId());
                                    requestTimestamps.remove(player.getUniqueId());
                                    player.sendMessage("§cSeu pedido de teleporte para §e" + targetPlayer.getName() + " §cexpirou.");
                                }
                            }
                        }.runTaskLater(SyntriPlugin.getInstance(), 20 * 15);

                        return true;
                    }
                }

            default:
                return false;
        }
    }
}
