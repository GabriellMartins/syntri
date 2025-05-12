package com.br.gabrielmartins.syntri.utils.geral.blockers;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class BlockedCraftsListener implements Listener {

    private final boolean enabled;
    private final String message;
    private final Set<Material> blockedMaterials = new HashSet<>();

    public BlockedCraftsListener(Plugin plugin) {
        File configFile = new File(plugin.getDataFolder(), "modules/general/config.yml");
        FileConfiguration config = YamlConfiguration.loadConfiguration(configFile);

        this.enabled = config.getBoolean("blocked-crafts.enabled", true);
        this.message = config.getString("blocked-crafts.message", "&cYou are not allowed to craft this item.");

        List<String> materialList = config.getStringList("blocked-crafts.list");
        for (String mat : materialList) {
            Material material = Material.matchMaterial(mat.toUpperCase());
            if (material != null) {
                blockedMaterials.add(material);
            } else {
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onPrepareCraft(PrepareItemCraftEvent event) {
        if (!enabled || event.getRecipe() == null || event.getRecipe().getResult() == null) return;

        Material resultType = event.getRecipe().getResult().getType();
        if (blockedMaterials.contains(resultType)) {
            HumanEntity viewer = event.getView().getPlayer();
            if (viewer instanceof Player) {
                Player player = (Player) viewer;
                if (!player.hasPermission("syntri.bypass.blockedcraft")) {
                    event.getInventory().setResult(new ItemStack(Material.AIR));
                    player.sendMessage(message.replace("&", "§"));
                }
            }
        }
    }
}
