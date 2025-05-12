package com.br.gabrielmartins.syntri.utils.geral.blockers;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import java.io.File;

public class BorderPearlBlocker implements Listener {

    private final boolean enabled;
    private final String message;

    public BorderPearlBlocker(Plugin plugin) {
        File configFile = new File(plugin.getDataFolder(), "modules/general/config.yml");
        FileConfiguration config = YamlConfiguration.loadConfiguration(configFile);


        this.enabled = config.getBoolean("block-border-pearl.enabled", true);
        this.message = config.getString("block-border-pearl.message", "&c%player%, you cannot throw ender pearls beyond the world border.");
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onPearlTeleport(PlayerTeleportEvent event) {
        if (!enabled || event.getCause() != TeleportCause.ENDER_PEARL) return;

        Player player = event.getPlayer();
        Location to = event.getTo();
        if (to == null) return;

        Location center = player.getWorld().getWorldBorder().getCenter();
        double halfSize = player.getWorld().getWorldBorder().getSize() / 2.0;

        double maxX = center.getX() + halfSize;
        double minX = center.getX() - halfSize;
        double maxZ = center.getZ() + halfSize;
        double minZ = center.getZ() - halfSize;

        if (to.getX() > maxX || to.getX() < minX || to.getZ() > maxZ || to.getZ() < minZ) {
            event.setCancelled(true);
            player.getInventory().addItem(new ItemStack(Material.ENDER_PEARL, 1));
            player.sendMessage(message.replace("%player%", player.getName()).replace("&", "§"));
        }
    }
}
