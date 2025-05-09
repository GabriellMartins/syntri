package com.br.gabrielmartins.syntri.commands.registry;

import com.br.gabrielmartins.engine.api.actionbar.ActionBarUtil;
import com.br.gabrielmartins.syntri.MessagesManager;
import com.br.gabrielmartins.engine.loader.command.info.CommandInfo;
import com.br.gabrielmartins.syntri.SyntriPlugin;

import org.bukkit.Bukkit;
import org.bukkit.command.*;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;

@CommandInfo(names = {"chat"}, permission = {"syntri.chat"})
public class ChatCommand implements CommandExecutor {

    private static boolean enabled = true;

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        MessagesManager mm = SyntriPlugin.getInstance().getMessagesManager();

        if (!sender.hasPermission("syntri.chat")) {
            sender.sendMessage(mm.getMessage("chat.no_permission"));
            return true;
        }

        if (args.length != 1) {
            List<String> usage = SyntriPlugin.getInstance().getMessagesManager().getMessageList("chat.usage");
            usage.forEach(sender::sendMessage);
            return true;
        }

        String name = getName(sender);
        String arg = args[0].toLowerCase();

        switch (arg) {
            case "on":
                enabled = true;
                broadcast(mm.getMessage("chat.enabled_title"), mm.getMessage("chat.enabled_by").replace("%player%", name));
                sendActionBar(mm.getMessage("chat.actionbar_enabled"));
                break;
            case "off":
                enabled = false;
                broadcast(mm.getMessage("chat.disabled_title"), mm.getMessage("chat.disabled_by").replace("%player%", name));
                sendActionBar(mm.getMessage("chat.actionbar_disabled"));
                break;
            case "clear":
                clearChat(name);
                break;
            default:
                sender.sendMessage(mm.getMessage("chat.invalid_command"));
        }
        return true;
    }

    private void clearChat(String senderName) {
        String clearedMsg = SyntriPlugin.getInstance().getMessagesManager().getMessage("chat.cleared_by").replace("%player%", senderName);
        for (Player p : Bukkit.getOnlinePlayers()) {
            for (int i = 0; i < 100; i++) p.sendMessage("");
            p.sendMessage("§8§m-------------------------");
            p.sendMessage("        §b§lChat Global");
            p.sendMessage(" ");
            p.sendMessage(" " + clearedMsg);
            p.sendMessage("§8§m-------------------------");
        }
    }

    private void broadcast(String title, String message) {
        for (Player p : Bukkit.getOnlinePlayers()) {
            p.sendMessage("§8§m-------------------------");
            p.sendMessage("        §b§lChat Global");
            p.sendMessage(" ");
            p.sendMessage(" " + title);
            p.sendMessage(" " + message);
            p.sendMessage("§8§m-------------------------");
        }
    }

    private void sendActionBar(String message) {
        new BukkitRunnable() {
            int ticks = 0;

            @Override
            public void run() {
                if (ticks >= 10 * 20) {
                    cancel();
                    return;
                }
                for (Player p : Bukkit.getOnlinePlayers()) {
                    ActionBarUtil.INSTANCE.send(p, message);
                }
                ticks += 20;
            }
        }.runTaskTimer(SyntriPlugin.getInstance(), 0L, 20L);
    }

    private String getName(CommandSender sender) {
        return (sender instanceof Player) ? sender.getName() : "Console";
    }

    public static boolean isEnabled() {
        return enabled;
    }
}
