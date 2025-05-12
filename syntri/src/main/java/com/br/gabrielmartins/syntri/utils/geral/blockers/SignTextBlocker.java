package com.br.gabrielmartins.syntri.utils.geral.blockers;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.util.List;

public class SignTextBlocker implements Listener {

    private final boolean enabled;
    private final boolean blockAll;
    private final boolean blockSpecific;
    private final List<String> blockedWords;
    private final String messageAll;
    private final String messageWord;

    public SignTextBlocker(Plugin plugin) {
        File configFile = new File(plugin.getDataFolder(), "modules/general/config.yml");
        FileConfiguration config = YamlConfiguration.loadConfiguration(configFile);

        this.enabled = config.getBoolean("blockSigns.enabled", true);
        this.blockAll = config.getBoolean("blockSigns.blockAll", false);
        this.blockSpecific = config.getBoolean("blockSigns.blockSpecific", true);
        this.blockedWords = config.getStringList("blockSigns.blockedWords");
        this.messageAll = config.getString("blockSigns.messageAll", "&cYou are not allowed to place signs.");
        this.messageWord = config.getString("blockSigns.messageWord", "&cThe word \"%palavra%\" is not allowed on signs.");
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onSignChange(SignChangeEvent event) {
        if (!enabled || event.getPlayer().hasPermission("syntri.bypass.blockedsign")) return;

        if (blockAll) {
            event.setCancelled(true);
            event.getPlayer().sendMessage(messageAll.replace("&", "§"));
            return;
        }

        if (blockSpecific) {
            for (String line : event.getLines()) {
                String content = line.toLowerCase();
                for (String blocked : blockedWords) {
                    if (content.contains(blocked.toLowerCase())) {
                        event.setCancelled(true);
                        event.getBlock().breakNaturally();
                        event.getPlayer().sendMessage(messageWord.replace("%palavra%", blocked).replace("&", "§"));
                        return;
                    }
                }
            }
        }
    }
}
