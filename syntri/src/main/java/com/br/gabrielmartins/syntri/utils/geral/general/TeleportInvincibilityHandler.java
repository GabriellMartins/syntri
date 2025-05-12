package com.br.gabrielmartins.syntri.utils.geral.general;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class TeleportInvincibilityHandler implements Listener {

    private final Set<UUID> protectedPlayers = new HashSet<>();
    private final boolean enabled;
    private final long durationTicks;
    private final Plugin plugin;

    public TeleportInvincibilityHandler(Plugin plugin) {
        this.plugin = plugin;
        File configFile = new File(plugin.getDataFolder(), "modules/general/config.yml");
        FileConfiguration config = YamlConfiguration.loadConfiguration(configFile);

        this.enabled = config.getBoolean("teleportInvincibility.enabled", true);
        long seconds = config.getLong("teleportInvincibility.duration", 5);
        this.durationTicks = seconds * 20L;
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onTeleport(PlayerTeleportEvent event) {
        if (!enabled) return;

        TeleportCause cause = event.getCause();
        if (cause == TeleportCause.COMMAND || cause == TeleportCause.PLUGIN) {
            Player player = event.getPlayer();
            UUID uuid = player.getUniqueId();
            protectedPlayers.add(uuid);

            new BukkitRunnable() {
                @Override
                public void run() {
                    protectedPlayers.remove(uuid);
                }
            }.runTaskLater(plugin, durationTicks);
        }
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onDamage(EntityDamageEvent event) {
        if (!enabled || !(event.getEntity() instanceof Player)) return;

        Player player = (Player) event.getEntity();
        if (protectedPlayers.contains(player.getUniqueId())) {
            event.setCancelled(true);
        }
    }
}
