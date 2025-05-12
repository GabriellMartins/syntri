package com.br.gabrielmartins.syntri.utils.geral.blockers;

import com.br.gabrielmartins.engine.data.table.DataTable;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.plugin.Plugin;

import java.io.File;

public class VoidFallBlocker implements Listener {

    private final boolean enabled;
    private final String message;

    public VoidFallBlocker(Plugin plugin) {
        File configFile = new File(plugin.getDataFolder(), "modules/general/config.yml");
        FileConfiguration config = YamlConfiguration.loadConfiguration(configFile);

        this.enabled = config.getBoolean("voidFall.enabled", true);
        this.message = config.getString("voidFall.message", "&cYou fell into the void and have been teleported to spawn.");
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onVoidDamage(EntityDamageEvent event) {
        if (!enabled) return;

        if (event.getCause() == DamageCause.VOID && event.getEntity() instanceof Player) {
            Player player = (Player) event.getEntity();

            if (player.getLocation().getBlockY() < 0) {
                event.setCancelled(true);
                player.setFallDistance(1);
                DataTable.teleportToSpawn(player);
                player.sendMessage(message.replace("&", "§"));
            }
        }
    }
}
