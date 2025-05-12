package com.br.gabrielmartins.syntri.utils.geral.disablers;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;
import org.bukkit.plugin.Plugin;

import java.io.File;

public class NaturalMobSpawnDisablerOld implements Listener {

    private final boolean enabled;

    public NaturalMobSpawnDisablerOld(Plugin plugin) {
        File configFile = new File(plugin.getDataFolder(), "modules/general/config.yml");
        FileConfiguration config = YamlConfiguration.loadConfiguration(configFile);

        this.enabled = config.getBoolean("disableNaturalMobSpawnOld.enabled", true);
    }

    @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
    public void onMobSpawn(CreatureSpawnEvent event) {
        if (!enabled) return;

        SpawnReason reason = event.getSpawnReason();
        if (reason == SpawnReason.SPAWNER) return;

        if (reason == SpawnReason.NATURAL
                || reason == SpawnReason.CHUNK_GEN
                || reason == SpawnReason.JOCKEY) {
            event.setCancelled(true);
        }
    }
}
