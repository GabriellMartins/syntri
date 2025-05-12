package com.br.gabrielmartins.syntri.module;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class ModuleLoader {

    private final File modulesFolder;
    private final JavaPlugin plugin;
    private final Map<String, Map<String, FileConfiguration>> modules = new HashMap<>();

    public ModuleLoader(File pluginDataFolder, JavaPlugin plugin) {
        this.plugin = plugin;
        this.modulesFolder = new File(pluginDataFolder, "modules");

        if (!modulesFolder.exists()) modulesFolder.mkdirs();
    }

    public void loadModules() {
        File[] moduleDirs = modulesFolder.listFiles(File::isDirectory);
        if (moduleDirs == null) return;

        for (File moduleDir : moduleDirs) {
            Map<String, FileConfiguration> configMap = new HashMap<>();

            File[] files = moduleDir.listFiles((dir, name) -> name.endsWith(".yml"));
            if (files == null) continue;

            for (File file : files) {
                String name = file.getName().replace(".yml", "").toLowerCase();
                FileConfiguration config = YamlConfiguration.loadConfiguration(file);
                configMap.put(name, config);
            }

            modules.put(moduleDir.getName().toLowerCase(), configMap);
        }
    }

    public FileConfiguration getConfig(String module, String configName) {
        ensureExists(module, configName);
        ensureReadmeExists(module);

        Map<String, FileConfiguration> configMap = modules.computeIfAbsent(module.toLowerCase(), k -> new HashMap<>());
        return configMap.computeIfAbsent(configName.toLowerCase(), k -> {
            File file = new File(modulesFolder, module + "/" + configName + ".yml");
            return YamlConfiguration.loadConfiguration(file);
        });
    }

    public boolean hasModule(String module) {
        return modules.containsKey(module.toLowerCase());
    }

    public boolean hasConfig(String module, String configName) {
        return getConfig(module, configName) != null;
    }

    private void ensureExists(String moduleName, String configName) {
        File moduleDir = new File(modulesFolder, moduleName);
        if (!moduleDir.exists()) moduleDir.mkdirs();

        File configFile = new File(moduleDir, configName + ".yml");
        if (!configFile.exists()) {
            String resourcePath = "modules/" + moduleName + "/" + configName + ".yml";
            InputStream resource = plugin.getResource(resourcePath);
            if (resource != null) {
                plugin.saveResource(resourcePath, false);
            } else {
            }
        }
    }

    private void ensureReadmeExists(String moduleName) {
        File moduleDir = new File(modulesFolder, moduleName);
        File readmeFile = new File(moduleDir, "README.md");

        if (!readmeFile.exists()) {
            String resourcePath = "modules/" + moduleName + "/README.md";
            InputStream resource = plugin.getResource(resourcePath);
            if (resource != null) {
                plugin.saveResource(resourcePath, false);
            }
        }
    }
}
