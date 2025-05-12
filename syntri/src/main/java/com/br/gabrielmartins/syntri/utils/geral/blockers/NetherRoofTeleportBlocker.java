package com.br.gabrielmartins.syntri.utils.geral.blockers;

import com.br.gabrielmartins.engine.data.table.DataTable;
import org.bukkit.World.Environment;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.plugin.Plugin;

import java.io.File;

public class NetherRoofTeleportBlocker implements Listener {

    private final boolean enabled;
    private final String message;

    public NetherRoofTeleportBlocker(Plugin plugin) {
        File configFile = new File(plugin.getDataFolder(), "modules/general/config.yml");
        FileConfiguration config = YamlConfiguration.loadConfiguration(configFile);

        this.enabled = config.getBoolean("blockNetherRoof.enabled", true);
        this.message = config.getString("blockNetherRoof.message", "&cYou cannot go above the Nether roof.");
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onTeleport(PlayerTeleportEvent event) {
        if (!enabled || event.getTo() == null) return;

        if (event.getTo().getWorld().getEnvironment() == Environment.NETHER && event.getTo().getY() > 124.0D) {
            Player player = event.getPlayer();
            event.setCancelled(true);
            DataTable.teleportToSpawn(player);
            player.sendMessage(message.replace("&", "§"));
        }
    }
}
