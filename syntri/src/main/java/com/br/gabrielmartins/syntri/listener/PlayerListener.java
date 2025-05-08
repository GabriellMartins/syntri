package com.br.gabrielmartins.syntri.listener;

import com.br.gabrielmartins.syntri.commands.registry.ChatCommand;
import com.br.gabrielmartins.syntri.commands.registry.GodCommand;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class PlayerListener implements Listener {

    @EventHandler
    public void onDamage(EntityDamageEvent event) {
        if (event.getEntity() instanceof Player) {
            Player player = (Player) event.getEntity();
            if (GodCommand.isGod(player)) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent e) {
        if (!ChatCommand.isEnabled() && !e.getPlayer().hasPermission("syntri.chat.bypass")) {
            e.setCancelled(true);
            e.getPlayer().sendMessage("§cChat desativado.");
        }
    }

    @EventHandler
    public void onTarget(EntityTargetEvent event) {
        if (event.getTarget() instanceof Player) {
            Player player = (Player) event.getTarget();
            if (GodCommand.isGod(player)) {
                event.setCancelled(true);
            }
        }
    }
}
