package com.br.gabrielmartins.syntri.utils.geral.general;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerLoginEvent.Result;
import org.bukkit.event.server.ServerListPingEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.util.Random;

public class PlayerLimitHandler implements Listener {

    private final boolean enabled;
    private final int maxPlayers;
    private final boolean kickNonVip;
    private final long kickDelay;
    private final String vipPermission;
    private final String msgServerFull;
    private final String msgWarnKick;
    private final String msgKick;

    private final Plugin plugin;

    public PlayerLimitHandler(Plugin plugin) {
        this.plugin = plugin;
        File configFile = new File(plugin.getDataFolder(), "modules/general/config.yml");
        FileConfiguration config = YamlConfiguration.loadConfiguration(configFile);

        this.enabled = config.getBoolean("playerLimit.enabled", true);
        this.maxPlayers = config.getInt("playerLimit.max", 100);
        this.kickNonVip = config.getBoolean("playerLimit.kickNonVip", true);
        this.kickDelay = config.getLong("playerLimit.kickDelaySeconds", 5) * 20L;
        this.vipPermission = config.getString("playerLimit.vipPermission", "system.lotado.entrar");

        this.msgServerFull = config.getString("playerLimit.messages.serverFull", "&cServer is full.");
        this.msgWarnKick = config.getString("playerLimit.messages.warnKick", "&eYou're being removed to make space for a VIP.");
        this.msgKick = config.getString("playerLimit.messages.kickMessage", "&cYou were removed to make space for a VIP player.");
    }

    @EventHandler
    public void onPing(ServerListPingEvent event) {
        if (!enabled) return;
        event.setMaxPlayers(maxPlayers);
    }

    @EventHandler
    public void onLogin(PlayerLoginEvent event) {
        if (!enabled) return;

        int online = Bukkit.getOnlinePlayers().size();
        if (online < maxPlayers) return;

        Player incoming = event.getPlayer();

        if (incoming.hasPermission(vipPermission)) {
            if (!kickNonVip) return;

            for (int attempt = 0; attempt < 4; attempt++) {
                Player[] players = Bukkit.getOnlinePlayers().toArray(new Player[0]);
                Player target = players[new Random().nextInt(players.length)];

                if (!target.hasPermission(vipPermission)) {
                    kickPlayer(target);
                    return;
                }
            }
        } else {
            event.setResult(Result.KICK_FULL);
            event.setKickMessage(msgServerFull.replace("&", "§"));
        }
    }

    private void kickPlayer(Player player) {
        player.sendMessage(msgWarnKick.replace("&", "§"));
        new BukkitRunnable() {
            @Override
            public void run() {
                if (player.isOnline()) {
                    player.kickPlayer(msgKick.replace("&", "§"));
                }
            }
        }.runTaskLater(plugin, kickDelay);
    }
}
