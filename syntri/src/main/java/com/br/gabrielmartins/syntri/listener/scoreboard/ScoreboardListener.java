package com.br.gabrielmartins.syntri.listener.scoreboard;

import com.br.gabrielmartins.syntri.scoreboard.ScoreboardManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.plugin.Plugin;

public class ScoreboardListener implements Listener {

    private final ScoreboardManager scoreboard;
    private final Plugin plugin;

    public ScoreboardListener(ScoreboardManager scoreboard, Plugin plugin) {
        this.scoreboard = scoreboard;
        this.plugin = plugin;

        scoreboard.startTitleAnimation(Bukkit.getOnlinePlayers());
        startAutoUpdate();
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        Bukkit.getScheduler().runTaskLater(plugin, () -> {
            scoreboard.apply(player);
        }, 20L);
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        scoreboard.remove(event.getPlayer());
    }

    private void startAutoUpdate() {
        new BukkitRunnable() {
            @Override
            public void run() {
                for (Player player : Bukkit.getOnlinePlayers()) {
                    scoreboard.apply(player);
                }
            }
        }.runTaskTimer(plugin, 20L, 40L);
    }
}
