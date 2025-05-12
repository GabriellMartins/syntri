package rush.recursos.bloqueadores;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Item;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.plugin.Plugin;

import java.io.File;

public class ItemExplosionBlocker implements Listener {

    private final boolean enabled;

    public ItemExplosionBlocker(Plugin plugin) {
        File configFile = new File(plugin.getDataFolder(), "modules/general/config.yml");
        FileConfiguration config = YamlConfiguration.loadConfiguration(configFile);

        this.enabled = config.getBoolean("blockItemExplosion.enabled", true);
    }

    @EventHandler(ignoreCancelled = true)
    public void onItemExplosion(EntityDamageByEntityEvent event) {
        if (!enabled) return;

        if (event.getEntity() instanceof Item) {
            event.setCancelled(true);
        }
    }
}
