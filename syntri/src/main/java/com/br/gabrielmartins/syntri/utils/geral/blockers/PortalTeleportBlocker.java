package com.br.gabrielmartins.syntri.utils.geral.blockers;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerPortalEvent;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
import org.bukkit.plugin.Plugin;

import java.io.File;

public class PortalTeleportBlocker implements Listener {

    private final boolean enabled;
    private final boolean blockNether;
    private final boolean blockEnd;
    private final String message;

    public PortalTeleportBlocker(Plugin plugin) {
        File configFile = new File(plugin.getDataFolder(), "modules/general/config.yml");
        FileConfiguration config = YamlConfiguration.loadConfiguration(configFile);

        this.enabled = config.getBoolean("blockPortalTeleport.enabled", true);
        this.blockNether = config.getBoolean("blockPortalTeleport.nether", true);
        this.blockEnd = config.getBoolean("blockPortalTeleport.end", true);
        this.message = config.getString("blockPortalTeleport.message", "&cYou are not allowed to teleport through this portal.");
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onPortalTeleport(PlayerPortalEvent event) {
        if (!enabled || event.getPlayer().hasPermission("syntri.bypass.teleportarporportal")) return;

        TeleportCause cause = event.getCause();
        if ((cause == TeleportCause.NETHER_PORTAL && blockNether) ||
                (cause == TeleportCause.END_PORTAL && blockEnd)) {
            event.setCancelled(true);
            event.getPlayer().sendMessage(message.replace("&", "§"));
        }
    }
}
