package com.br.gabrielmartins.syntri.utils.geral.general;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.util.List;

public class InvalidMoneyCommandBlocker implements Listener {

    private final boolean enabled;
    private final List<String> blockedCommands;
    private final List<String> invalidValues;
    private final String invalidNumberMsg;
    private final String nullMoneyMsg;

    public InvalidMoneyCommandBlocker(Plugin plugin) {
        File configFile = new File(plugin.getDataFolder(), "modules/general/config.yml");
        FileConfiguration config = YamlConfiguration.loadConfiguration(configFile);

        this.enabled = config.getBoolean("blockInvalidMoney.enabled", true);
        this.blockedCommands = config.getStringList("blockInvalidMoney.blockedCommands");
        this.invalidValues = config.getStringList("blockInvalidMoney.invalidValues");
        this.invalidNumberMsg = config.getString("blockInvalidMoney.messageInvalidNumber", "&cInvalid number: %numero%");
        this.nullMoneyMsg = config.getString("blockInvalidMoney.messageInvalidMoney", "&cInvalid or corrupt money value.");
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onTrySellWithNegative(PlayerCommandPreprocessEvent event) {
        if (!enabled) return;
        String fullCmd = event.getMessage().toLowerCase();
        if (fullCmd.contains("mercado vender -") || fullCmd.contains("market vender -")) {
            event.getPlayer().sendMessage(invalidNumberMsg.replace("%numero%", "-").replace("&", "§"));
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onTryExploitNull(PlayerCommandPreprocessEvent event) {
        if (!enabled) return;
        String fullCmd = event.getMessage().toLowerCase();
        String baseCmd = fullCmd.split(" ")[0];

        for (String cmd : blockedCommands) {
            if (baseCmd.equalsIgnoreCase(cmd) || (baseCmd.contains(":") && baseCmd.split(":").length > 1 &&
                    ("/" + baseCmd.split(":")[1]).equalsIgnoreCase(cmd))) {

                for (String invalid : invalidValues) {
                    if (fullCmd.contains(" " + invalid)) {
                        event.getPlayer().sendMessage(nullMoneyMsg.replace("&", "§"));
                        event.setCancelled(true);
                        return;
                    }
                }
            }
        }
    }
}
