package com.br.gabrielmartins.syntri.utils.geral.blockers;

import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import java.io.File;

public class NameTagUseBlocker implements Listener {

    private final boolean enabled;
    private final String message;

    public NameTagUseBlocker(Plugin plugin) {
        File configFile = new File(plugin.getDataFolder(), "modules/general/config.yml");
        FileConfiguration config = YamlConfiguration.loadConfiguration(configFile);

        this.enabled = config.getBoolean("blockNameTag.enabled", true);
        this.message = config.getString("blockNameTag.message", "&cYou are not allowed to use name tags.");
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onInteract(PlayerInteractEntityEvent event) {
        if (!enabled) return;

        Player player = event.getPlayer();
        ItemStack item = player.getItemInHand();

        if (item != null && item.getType() == Material.NAME_TAG) {
            if (!player.hasPermission("syntri.bypass.usarnametag")) {
                event.setCancelled(true);
                player.sendMessage(message.replace("&", "§"));
            }
        }
    }
}
