package com.br.gabrielmartins.syntri.utils.geral.blockers;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.plugin.Plugin;

import java.io.File;

public class CropTrampleBlocker implements Listener {

    private final boolean enabled;
    private final Material soilMaterial;

    public CropTrampleBlocker(Plugin plugin) {
        File configFile = new File(plugin.getDataFolder(), "modules/general/config.yml");
        FileConfiguration config = YamlConfiguration.loadConfiguration(configFile);

        this.enabled = config.getBoolean("block-crop-trample.enabled", true);

        Material material = Material.matchMaterial("FARMLAND");
        this.soilMaterial = material;

        if (soilMaterial == null) {
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onCropTrample(EntityChangeBlockEvent event) {
        if (!enabled || soilMaterial == null) return;

        if (event.getBlock().getType() == soilMaterial) {
            event.setCancelled(true);
            Bukkit.getLogger().info("[Syntri] 🌾 Pisoteio de plantação bloqueado em " +
                    event.getBlock().getX() + "," +
                    event.getBlock().getY() + "," +
                    event.getBlock().getZ());
        }
    }
}
