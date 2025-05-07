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

    private final SyntriPlugin plugin;
    private final ScoreboardManager scoreboard;

    public ScoreboardListener(SyntriPlugin plugin) {
        this.plugin = plugin;
        this.scoreboard = new ScoreboardManager(plugin.getConfig());

        Bukkit.getPluginManager().registerEvents(this, plugin);
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
        }.runTaskTimer(plugin, 20L, 40L);
    }

    private boolean isScoreboardEnabled() {
        return plugin.getConfig().getBoolean("scoreboard.enabled", true);
    }
}
