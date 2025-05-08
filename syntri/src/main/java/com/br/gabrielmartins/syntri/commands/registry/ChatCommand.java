package com.br.gabrielmartins.syntri.commands.registry;

import com.br.gabrielmartins.engine.api.actionbar.ActionBarUtil;
import com.br.gabrielmartins.engine.api.translate.Translate;
import com.br.gabrielmartins.engine.loader.command.info.CommandInfo;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

@CommandInfo(names = {"chat"}, permission = {"syntri.chat"})
public class ChatCommand implements CommandExecutor {

    private static boolean enabled = true;

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!sender.hasPermission("syntri.chat")) {
            sender.sendMessage(Translate.get("chat.no_permission"));
            return true;
        }

        if (args.length != 1) {
            sender.sendMessage("§8§m-------------------------");
            sender.sendMessage("       §b§l" + Translate.get("chat.title"));
            sender.sendMessage(" ");
            sender.sendMessage(" §f" + Translate.get("chat.usage_on"));
            sender.sendMessage(" §f" + Translate.get("chat.usage_off"));
            sender.sendMessage(" §f" + Translate.get("chat.usage_clear"));
            sender.sendMessage("§8§m-------------------------");
            return true;
        }

        String arg = args[0].toLowerCase();
        switch (arg) {
            case "on":
                enabled = true;
                broadcast("§a§l" + Translate.get("chat.activated"), "§f" + Translate.get("chat.enabled_by") + " §e" + getName(sender));
                sendActionBar("§a✔ " + Translate.get("chat.actionbar_on"));
                break;
            case "off":
                enabled = false;
                broadcast("§c§l" + Translate.get("chat.deactivated"), "§f" + Translate.get("chat.disabled_by") + " §e" + getName(sender));
                sendActionBar("§c✖ " + Translate.get("chat.actionbar_off"));
                break;
            case "clear":
                clearChat(sender);
                break;
            default:
                sender.sendMessage("§c" + Translate.get("chat.invalid_command"));
        }
        return true;
    }

    private void clearChat(CommandSender sender) {
        String name = getName(sender);
        for (Player p : Bukkit.getOnlinePlayers()) {
            for (int i = 0; i < 100; i++) p.sendMessage("");
            p.sendMessage("§8§m-------------------------");
            p.sendMessage("        §b§l" + Translate.get("chat.title"));
            p.sendMessage(" ");
            p.sendMessage(" §f" + Translate.get("chat.cleared_by") + " §a" + name);
            p.sendMessage("§8§m-------------------------");
        }
    }

    private void broadcast(String title, String message) {
        for (Player p : Bukkit.getOnlinePlayers()) {
            p.sendMessage("§8§m-------------------------");
            p.sendMessage("        §b§l" + Translate.get("chat.title"));
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
        }.runTaskTimer(Bukkit.getPluginManager().getPlugin("Syntri"), 0L, 20L);
    }

    private String getName(CommandSender sender) {
        return (sender instanceof Player) ? sender.getName() : "Console";
    }

    public static boolean isEnabled() {
        return enabled;
    }
}
