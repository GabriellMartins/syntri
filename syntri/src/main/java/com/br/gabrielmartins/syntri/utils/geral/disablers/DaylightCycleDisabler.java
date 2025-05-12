package com.br.gabrielmartins.syntri.utils.geral.disablers;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;

public class DaylightCycleDisabler {

    private final boolean enabled;
    private final boolean lockTime;
    private final long fixedTime;
    private final Plugin plugin;

    public DaylightCycleDisabler(Plugin plugin) {
        this.plugin = plugin;
        File configFile = new File(plugin.getDataFolder(), "modules/general/config.yml");
        FileConfiguration config = YamlConfiguration.loadConfiguration(configFile);

        this.enabled = config.getBoolean("disableDaylightCycle.enabled", true);
        this.lockTime = config.getBoolean("disableDaylightCycle.timeLock", true);
        this.fixedTime = config.getLong("disableDaylightCycle.fixedTime", 6000L);
    }

    public void apply() {
        if (!enabled) return;

        new BukkitRunnable() {
            @Override
            public void run() {
                for (World world : Bukkit.getWorlds()) {
                    world.setGameRuleValue("doDaylightCycle", "false");
                    if (lockTime) {
                        world.setTime(fixedTime);
                    }
                }
            }
        }.runTaskLater(plugin, 40L);
    }

    public void startRepeatingLock() {
        if (!enabled || !lockTime) return;

        new BukkitRunnable() {
            @Override
            public void run() {
                for (World world : Bukkit.getWorlds()) {
                    world.setTime(fixedTime);
                }
            }
        }.runTaskTimer(plugin, 600L, 600L);
    }
}
