    package com.br.gabrielmartins.syntri.commands.registry;

    import com.br.gabrielmartins.syntri.MessagesManager;
    import com.br.gabrielmartins.engine.api.cooldown.Cooldown;
    import com.br.gabrielmartins.engine.loader.command.info.CommandInfo;
    import com.br.gabrielmartins.engine.utils.combat.CombatLogManager;
    import com.br.gabrielmartins.syntri.SyntriPlugin;

    import net.md_5.bungee.api.chat.ClickEvent;
    import net.md_5.bungee.api.chat.TextComponent;
    import org.bukkit.Bukkit;
    import org.bukkit.command.*;
    import org.bukkit.entity.Player;
    import org.bukkit.scheduler.BukkitRunnable;
    import java.util.*;

    @CommandInfo(names = {"tp", "tpall", "tpa"}, permission = {"syntri.tp"})
    public class TeleportCommand implements CommandExecutor {

        private static final Map<UUID, UUID> pendingRequests = new HashMap<>();
        private static final Map<UUID, Long> requestTimestamps = new HashMap<>();

        @Override
        public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
            MessagesManager mm = SyntriPlugin.getInstance().getMessagesManager();

            if (!(sender instanceof Player)) {
                sender.sendMessage(mm.getMessage("teleport.only_players"));
                return true;
            }

            Player player = (Player) sender;
            String cmd = label.toLowerCase();

            switch (cmd) {
                case "tp": {
                    if (!player.hasPermission("syntri.tp")) {
                        player.sendMessage(mm.getMessage("teleport.no_permission"));
                        return true;
                    }
                    if (args.length != 1) {
                        player.sendMessage(mm.getMessage("teleport.usage_tp"));
                        return true;
                    }
                    Player target = Bukkit.getPlayerExact(args[0]);
                    if (target == null || !target.isOnline()) {
                        player.sendMessage(mm.getMessage("teleport.player_not_found"));
                        return true;
                    }
                    player.teleport(target);
                    player.sendMessage(mm.getMessage("teleport.teleported").replace("%target%", target.getName()));
                    return true;
                }
                case "tpall": {
                    if (!player.hasPermission("syntri.tpall")) {
                        player.sendMessage(mm.getMessage("teleport.no_permission"));
                        return true;
                    }
                    for (Player online : Bukkit.getOnlinePlayers()) {
                        if (!online.equals(player)) {
                            online.teleport(player);
                            online.sendMessage(mm.getMessage("teleport.teleported_to_you").replace("%player%", player.getName()));
                        }
                    }
                    player.sendMessage(mm.getMessage("teleport.all_teleported"));
                    return true;
                }
                case "tpa": {
                    if (args.length == 0) {
                        player.sendMessage(mm.getMessage("teleport.usage_tpa"));
                        return true;
                    }
                    String arg = args[0].toLowerCase();

                    switch (arg) {
                        case "aceitar":
                            UUID requester = pendingRequests.entrySet().stream()
                                    .filter(e -> e.getValue().equals(player.getUniqueId()))
                                    .map(Map.Entry::getKey).findFirst().orElse(null);

                            if (requester == null) {
                                player.sendMessage(mm.getMessage("teleport.no_request"));
                                return true;
                            }
                            Player from = Bukkit.getPlayer(requester);
                            if (from != null) {
                                from.teleport(player);
                                from.sendMessage(mm.getMessage("teleport.accepted_by_target"));
                                player.sendMessage(mm.getMessage("teleport.accepted").replace("%player%", from.getName()));
                            }
                            pendingRequests.remove(requester);
                            requestTimestamps.remove(requester);
                            return true;

                        case "recusar":
                            UUID senderId = pendingRequests.entrySet().stream()
                                    .filter(e -> e.getValue().equals(player.getUniqueId()))
                                    .map(Map.Entry::getKey).findFirst().orElse(null);

                            if (senderId == null) {
                                player.sendMessage(mm.getMessage("teleport.no_request"));
                                return true;
                            }
                            Player senderPlayer = Bukkit.getPlayer(senderId);
                            if (senderPlayer != null) {
                                senderPlayer.sendMessage(mm.getMessage("teleport.denied_by_target"));
                            }
                            pendingRequests.remove(senderId);
                            requestTimestamps.remove(senderId);
                            player.sendMessage(mm.getMessage("teleport.denied"));
                            return true;

                        default:
                            Player targetPlayer = Bukkit.getPlayerExact(arg);
                            if (targetPlayer == null || !targetPlayer.isOnline()) {
                                player.sendMessage(mm.getMessage("teleport.player_not_found"));
                                return true;
                            }
                            if (targetPlayer.equals(player)) {
                                player.sendMessage(mm.getMessage("teleport.cannot_self_request"));
                                return true;
                            }
                            if (CombatLogManager.isInCombat(player)) {
                                player.sendMessage(mm.getMessage("teleport.in_combat"));
                                return true;
                            }
                            if (Cooldown.INSTANCE.isCoolingDown(player)) {
                                Cooldown.INSTANCE.sendActionBar(player, mm.getMessage("teleport.cooldown"));
                                return true;
                            }

                            pendingRequests.put(player.getUniqueId(), targetPlayer.getUniqueId());
                            requestTimestamps.put(player.getUniqueId(), System.currentTimeMillis());

                            player.sendMessage(mm.getMessage("teleport.request_sent").replace("%target%", targetPlayer.getName()));

                            TextComponent msg = new TextComponent(mm.getMessage("teleport.tpa_message")
                                    .replace("%player%", player.getName()));
                            TextComponent accept = new TextComponent(mm.getMessage("teleport.tpa_accept"));
                            accept.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/tpa aceitar"));
                            TextComponent deny = new TextComponent(mm.getMessage("teleport.tpa_deny"));
                            deny.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/tpa recusar"));

                            msg.addExtra(accept);
                            msg.addExtra(deny);
                            targetPlayer.spigot().sendMessage(msg);

                            Cooldown.INSTANCE.start(SyntriPlugin.getInstance(), player, 15, "§c§lCooldown ");

                            new BukkitRunnable() {
                                @Override
                                public void run() {
                                    if (pendingRequests.containsKey(player.getUniqueId()) &&
                                            System.currentTimeMillis() - requestTimestamps.get(player.getUniqueId()) >= 15000) {
                                        pendingRequests.remove(player.getUniqueId());
                                        requestTimestamps.remove(player.getUniqueId());
                                        player.sendMessage(mm.getMessage("teleport.request_expired")
                                                .replace("%target%", targetPlayer.getName()));
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
