package com.br.gabrielmartins.syntri.utils.geral.general;

import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.UUID;

public class EnderPearlCooldownHandler implements Listener {

    private final boolean enabled;
    private final long cooldownMillis;
    private final String message;
    private static final HashMap<UUID, Timestamp> COOLDOWN = new HashMap<>();

    public EnderPearlCooldownHandler(Plugin plugin) {
        File configFile = new File(plugin.getDataFolder(), "modules/general/config.yml");
        FileConfiguration config = YamlConfiguration.loadConfiguration(configFile);

        this.enabled = config.getBoolean("enderpearlCooldown.enabled", true);
        long seconds = config.getLong("enderpearlCooldown.seconds", 10);
        this.cooldownMillis = seconds * 1000L;
        this.message = config.getString("enderpearlCooldown.message", "&cPlease wait before throwing another ender pearl.");
    }

    @EventHandler(ignoreCancelled = true)
    public void onEnderPearlThrow(ProjectileLaunchEvent event) {
        if (!enabled) return;
        if (event.getEntityType() != EntityType.ENDER_PEARL) return;
        if (!(event.getEntity().getShooter() instanceof Player)) return;

        Player player = (Player) event.getEntity().getShooter();
        UUID uuid = player.getUniqueId();

        Timestamp now = new Timestamp(System.currentTimeMillis());
        if (COOLDOWN.containsKey(uuid)) {
            Timestamp expire = COOLDOWN.get(uuid);
            if (expire.after(now)) {
                event.setCancelled(true);
                player.sendMessage(message.replace("&", "§"));
                player.getInventory().addItem(new ItemStack(Material.ENDER_PEARL));
                return;
            }
        }

        COOLDOWN.put(uuid, new Timestamp(now.getTime() + cooldownMillis));
    }
}
