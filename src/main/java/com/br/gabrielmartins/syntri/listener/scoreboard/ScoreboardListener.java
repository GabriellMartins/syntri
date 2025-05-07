package com.br.gabrielmartins.syntri.listener.scoreboard;

import com.br.gabrielmartins.syntri.SyntriPlugin;
import com.br.gabrielmartins.syntri.api.scoreboard.ScoreboardManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.scheduler.BukkitRunnable;

public class ScoreboardListener implements Listener {

    private final ScoreboardManager scoreboard;

    public ScoreboardListener() {
        this.scoreboard = new ScoreboardManager(SyntriPlugin.getInstance().getConfig());

        Bukkit.getPluginManager().registerEvents(this, SyntriPlugin.getInstance());
        startAutoUpdate();
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        if (!isScoreboardEnabled()) return;
        scoreboard.apply(event.getPlayer());
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        scoreboard.remove(event.getPlayer());
    }

    private void startAutoUpdate() {
        new BukkitRunnable() {
            @Override
            public void run() {
                if (!isScoreboardEnabled()) return;

                for (Player player : Bukkit.getOnlinePlayers()) {
                    scoreboard.apply(player);
                }
            }
        }.runTaskTimer(SyntriPlugin.getInstance(), 20L, 40L);
    }

    private boolean isScoreboardEnabled() {
        return SyntriPlugin.getInstance().getConfig().getBoolean("scoreboard.enabled", true);
    }
}
