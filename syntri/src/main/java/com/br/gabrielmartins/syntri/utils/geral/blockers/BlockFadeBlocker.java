package com.br.gabrielmartins.syntri.utils.geral.blockers;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockFadeEvent;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class BlockFadeBlocker implements Listener {

    private final boolean enabled;
    private final Set<Material> blockedMaterials = new HashSet<>();

    public BlockFadeBlocker(Plugin plugin) {
        File configFile = new File(plugin.getDataFolder(), "modules/general/config.yml");
        FileConfiguration config = YamlConfiguration.loadConfiguration(configFile);

        this.enabled = config.getBoolean("block-fade.enabled", true);

        List<String> materialNames = config.getStringList("block-fade.materials");
        for (String name : materialNames) {
            Material mat = Material.matchMaterial(name.toUpperCase());
            if (mat != null) {
                blockedMaterials.add(mat);
            } else {
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onBlockFade(BlockFadeEvent event) {
        if (!enabled) return;

        Material blockType = event.getBlock().getType();
        if (blockedMaterials.contains(blockType)) {
            event.setCancelled(true);
        }
    }
}
