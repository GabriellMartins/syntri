package com.br.gabrielmartins.syntri.utils.geral.disablers;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.weather.WeatherChangeEvent;
import org.bukkit.plugin.Plugin;

import java.io.File;

public class WeatherDisabler implements Listener {

    private final boolean enabled;

    public WeatherDisabler(Plugin plugin) {
        File configFile = new File(plugin.getDataFolder(), "modules/general/config.yml");
        FileConfiguration config = YamlConfiguration.loadConfiguration(configFile);

        this.enabled = config.getBoolean("disableWeather.enabled", true);
    }

    @EventHandler(ignoreCancelled = true)
    public void onWeatherChange(WeatherChangeEvent event) {
        if (!enabled) return;

        if (event.toWeatherState()) {
            event.setCancelled(true);
        }
    }
}
