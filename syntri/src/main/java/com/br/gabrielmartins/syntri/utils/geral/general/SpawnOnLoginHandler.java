package com.br.gabrielmartins.syntri.utils.geral.general;

import com.br.gabrielmartins.syntri.data.controller.DataHandler;
import com.br.gabrielmartins.syntri.data.table.DataTable;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.plugin.Plugin;

import java.io.File;

public class SpawnOnLoginHandler implements Listener {

    private final boolean enabled;
    private final boolean teleportOnRespawn;
    private final String vipPermission;

    public SpawnOnLoginHandler(Plugin plugin) {
        File configFile = new File(plugin.getDataFolder(), "modules/general/config.yml");
        FileConfiguration config = YamlConfiguration.loadConfiguration(configFile);

        this.enabled = config.getBoolean("spawnOnLogin.enabled", true);
        this.teleportOnRespawn = config.getBoolean("spawnOnLogin.teleportRespawn", true);
        this.vipPermission = config.getString("spawnOnLogin.vipPermission", "system.spawn.vip");
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onJoin(PlayerJoinEvent event) {
        if (!enabled) return;

        Player player = event.getPlayer();
        if (player.hasPermission(vipPermission)) {
            DataTable.teleportToVipSpawn(player);
        } else {
            DataTable.teleportToSpawn(player);
        }
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onRespawn(PlayerRespawnEvent event) {
        if (!enabled || !teleportOnRespawn) return;

        Location spawnLocation = DataTable.getSpawnLocation();
        if (spawnLocation != null) {
            event.setRespawnLocation(spawnLocation);
        }
    }
}
