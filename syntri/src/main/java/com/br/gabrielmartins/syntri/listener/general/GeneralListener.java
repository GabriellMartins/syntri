package com.br.gabrielmartins.syntri.listener.general;

import com.br.gabrielmartins.engine.api.translate.Translate;
import com.br.gabrielmartins.engine.utils.color.ColorUtil;
import com.br.gabrielmartins.engine.utils.ip.IPUtils;
import com.br.gabrielmartins.syntri.SyntriPlugin;

import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockIgniteEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.enchantment.EnchantItemEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.hanging.HangingBreakEvent;
import org.bukkit.event.hanging.HangingPlaceEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.player.*;
import org.bukkit.event.vehicle.VehicleEnterEvent;
import org.bukkit.event.vehicle.VehicleExitEvent;
import org.bukkit.event.weather.WeatherChangeEvent;
import org.bukkit.event.world.WorldUnloadEvent;

public class GeneralListener implements Listener {

    private final SyntriPlugin plugin = SyntriPlugin.getInstance();

    private boolean cfg(String path) {
        return plugin.getConfig().getBoolean("general." + path, false);
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        e.setJoinMessage(null);
        if (!cfg("welcome-message.enabled")) return;

        Player p = e.getPlayer();
        String lang = IPUtils.getPlayerLanguage(p);
        String raw = plugin.getConfig().getString("general.welcome-message.text", "&aBem-vindo, %player_name%!");
        p.sendMessage(ColorUtil.color(PlaceholderAPI.setPlaceholders(p, Translate.get(raw, lang))));
    }

    @EventHandler public void onQuit(PlayerQuitEvent e) {
        e.setQuitMessage(null);
        if (!cfg("quit-message.enabled")) return;

        Player p = e.getPlayer();
        String lang = IPUtils.getPlayerLanguage(p);
        String raw = plugin.getConfig().getString("general.quit-message.text", "&e[Syntri] Jogador saiu: %player_name%");
        Bukkit.getConsoleSender().sendMessage(ColorUtil.color(PlaceholderAPI.setPlaceholders(p, Translate.get(raw, lang))));
    }

    @EventHandler public void onCommand(PlayerCommandPreprocessEvent e) { if (cfg("prevent-command")) e.setCancelled(true); }
    @EventHandler public void onDamage(EntityDamageEvent e) { if (cfg("prevent-damage")) e.setCancelled(true); }
    @EventHandler public void onInteract(PlayerInteractEvent e) { if (cfg("prevent-interact")) e.setCancelled(true); }
    @EventHandler public void onMove(PlayerMoveEvent e) { if (cfg("prevent-move")) e.setCancelled(true); }
    @EventHandler public void onDrop(PlayerDropItemEvent e) { if (cfg("prevent-item-drop")) e.setCancelled(true); }
    @EventHandler public void onBreak(BlockBreakEvent e) { if (cfg("prevent-block-break")) e.setCancelled(true); }
    @EventHandler public void onPlace(BlockPlaceEvent e) { if (cfg("prevent-block-place")) e.setCancelled(true); }
    @EventHandler public void onWeather(WeatherChangeEvent e) { if (cfg("prevent-weather-change")) e.setCancelled(true); }
    @EventHandler public void onInventoryClick(InventoryClickEvent e) { if (cfg("prevent-inventory-click")) e.setCancelled(true); }
    @EventHandler public void onInventoryOpen(InventoryOpenEvent e) { if (cfg("prevent-inventory-open")) e.setCancelled(true); }
    @EventHandler public void onInventoryDrag(InventoryDragEvent e) { if (cfg("prevent-inventory-drag")) e.setCancelled(true); }
    @EventHandler public void onFood(FoodLevelChangeEvent e) { if (cfg("prevent-hunger")) e.setCancelled(true); }
    @EventHandler public void onInteractEntity(PlayerInteractEntityEvent e) { if (cfg("prevent-entity-interact")) e.setCancelled(true); }
    @EventHandler public void onShear(PlayerShearEntityEvent e) { if (cfg("prevent-shear")) e.setCancelled(true); }
    @EventHandler public void onItemHeld(PlayerItemHeldEvent e) { if (cfg("prevent-item-swap")) e.setCancelled(true); }
    @EventHandler public void onBucketFill(PlayerBucketFillEvent e) { if (cfg("prevent-bucket-fill")) e.setCancelled(true); }
    @EventHandler public void onBucketEmpty(PlayerBucketEmptyEvent e) { if (cfg("prevent-bucket-empty")) e.setCancelled(true); }
    @EventHandler public void onIgnite(BlockIgniteEvent e) { if (cfg("prevent-ignite")) e.setCancelled(true); }
    @EventHandler public void onExplode(EntityExplodeEvent e) { if (cfg("prevent-explosions")) e.setCancelled(true); }
    @EventHandler public void onPortal(PlayerPortalEvent e) { if (cfg("prevent-portal")) e.setCancelled(true); }
    @EventHandler public void onTeleport(PlayerTeleportEvent e) { if (cfg("prevent-teleport")) e.setCancelled(true); }

    @EventHandler
    public void onRespawn(PlayerRespawnEvent e) {
        String type = plugin.getConfig().getString("general.respawn.type", "default");
        if (type.equalsIgnoreCase("spawn")) {
            e.setRespawnLocation(e.getPlayer().getWorld().getSpawnLocation());
        }
    }

    @EventHandler public void onEnchant(EnchantItemEvent e) { if (cfg("prevent-enchant")) e.setCancelled(true); }
    @EventHandler public void onBedEnter(PlayerBedEnterEvent e) { if (cfg("prevent-bed")) e.setCancelled(true); }
    @EventHandler public void onVehicleEnter(VehicleEnterEvent e) { if (cfg("prevent-vehicle-enter")) e.setCancelled(true); }
    @EventHandler public void onVehicleExit(VehicleExitEvent e) { if (cfg("prevent-vehicle-exit")) e.setCancelled(true); }
    @EventHandler public void onItemConsume(PlayerItemConsumeEvent e) { if (cfg("prevent-consume")) e.setCancelled(true); }

    @EventHandler
    public void onItemBreak(PlayerItemBreakEvent e) {
        String type = plugin.getConfig().getString("general.item-break.type", "ignore");
        switch (type.toLowerCase()) {
            case "alert":
                e.getPlayer().sendMessage(ColorUtil.color("&c[Syntri] Seu item quebrou: " + e.getBrokenItem().getType()));
                break;
            case "log":
                Bukkit.getLogger().info("[Syntri] " + e.getPlayer().getName() + " quebrou " + e.getBrokenItem().getType());
                break;
            default:
                break;
        }
    }

    @EventHandler public void onHangingPlace(HangingPlaceEvent e) { if (cfg("prevent-hanging-place")) e.setCancelled(true); }
    @EventHandler public void onHangingBreak(HangingBreakEvent e) { if (cfg("prevent-hanging-break")) e.setCancelled(true); }
    @EventHandler public void onWorldUnload(WorldUnloadEvent e) { if (cfg("prevent-world-unload")) e.setCancelled(true); }
}