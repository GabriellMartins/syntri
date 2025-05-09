package com.br.gabrielmartins.syntri.cache;

import com.br.gabrielmartins.syntri.SyntriPlugin;
import lombok.Getter;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

@Getter
public class TopMoneyCache {

    private final Map<UUID, Double> balanceMap = new HashMap<>();
    private final List<UUID> top10 = new ArrayList<>();

    public void startUpdater() {
        new BukkitRunnable() {
            @Override
            public void run() {
                updateTopList();
            }
        }.runTaskTimerAsynchronously(SyntriPlugin.getInstance(), 0L, 20L * 60 * 5);
    }

    public void updateTopList() {
        Economy economy = SyntriPlugin.getInstance().getEconomy();
        if (economy == null) return;

        balanceMap.clear();

        for (OfflinePlayer player : Bukkit.getOfflinePlayers()) {
            balanceMap.put(player.getUniqueId(), economy.getBalance(player));
        }

        top10.clear();
        top10.addAll(balanceMap.entrySet().stream()
                .sorted(Map.Entry.<UUID, Double>comparingByValue().reversed())
                .limit(10)
                .map(Map.Entry::getKey)
                .toList());
    }
}
