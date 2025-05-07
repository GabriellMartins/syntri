package com.br.gabrielmartins.syntri.utils.combat;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

public class CombatLogManager {

    private static final Set<UUID> combatPlayers = new HashSet<>();
    private static final int COMBAT_DURATION_SECONDS = 10;
    public static void tag(Player player) {
        UUID uuid = player.getUniqueId();
        combatPlayers.add(uuid);

        new BukkitRunnable() {
            @Override
            public void run() {
                combatPlayers.remove(uuid);
            }
        }.runTaskLaterAsynchronously(Bukkit.getPluginManager().getPlugin("Syntri"), COMBAT_DURATION_SECONDS * 20L);
    }

    public static boolean isInCombat(Player player) {
        return combatPlayers.contains(player.getUniqueId());
    }
}
