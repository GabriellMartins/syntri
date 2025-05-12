package com.br.gabrielmartins.syntri.utils.geral.general;

import com.br.gabrielmartins.syntri.api.anvil.AnvilAPI;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.plugin.Plugin;

import java.io.File;

public class InfiniteAnvilHandler implements Listener {

    private final boolean enabled;

    public InfiniteAnvilHandler(Plugin plugin) {
        File configFile = new File(plugin.getDataFolder(), "modules/general/config.yml");
        FileConfiguration config = YamlConfiguration.loadConfiguration(configFile);

        this.enabled = config.getBoolean("infiniteAnvil.enabled", true);
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onAnvilUse(PlayerInteractEvent event) {
        if (!enabled) return;
        if (event.getAction() != Action.RIGHT_CLICK_BLOCK) return;
        if (event.getClickedBlock() == null || event.getClickedBlock().getType() != Material.ANVIL) return;
        if (event.getPlayer().isSneaking()) return;

        AnvilAPI.openAnvil(event.getPlayer());
        event.setCancelled(true);
    }
}
