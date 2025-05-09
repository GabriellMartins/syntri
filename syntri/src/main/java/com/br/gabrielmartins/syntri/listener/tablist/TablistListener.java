package com.br.gabrielmartins.syntri.listener.tablist;

import com.br.gabrielmartins.syntri.tablist.TablistManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class TablistListener implements Listener {

    private final TablistManager tablistManager;

    public TablistListener(TablistManager tablistManager) {
        this.tablistManager = tablistManager;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        tablistManager.updateTablist(event.getPlayer());
    }
}
