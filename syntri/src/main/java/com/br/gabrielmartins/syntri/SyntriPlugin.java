package com.br.gabrielmartins.syntri;

import com.br.gabrielmartins.engine.api.inventory.loader.InventoryLoader;

import com.br.gabrielmartins.engine.backend.Backend;
import com.br.gabrielmartins.engine.backend.BackendType;
import com.br.gabrielmartins.engine.backend.mongo.MongoBackend;
import com.br.gabrielmartins.engine.data.controller.DataHandler;
import com.br.gabrielmartins.engine.data.factory.BackendFactory;
import com.br.gabrielmartins.engine.data.service.mongo.MongoDataService;
import com.br.gabrielmartins.engine.data.service.sql.SQLDataService;
import com.br.gabrielmartins.engine.loader.command.CommandLoader;
import com.br.gabrielmartins.engine.loader.general.GeneralListener;
import com.br.gabrielmartins.engine.loader.listener.LoaderListener;
import com.br.gabrielmartins.syntri.api.reflection.ReflectionUtils;
import com.br.gabrielmartins.syntri.cache.TopMoneyCache;
import com.br.gabrielmartins.syntri.enums.Version;
import com.br.gabrielmartins.syntri.kit.manager.KitManager;
import com.br.gabrielmartins.syntri.module.list.general.GeneralModule;
import com.br.gabrielmartins.syntri.module.list.kits.KitModule;
import com.br.gabrielmartins.syntri.modules.MotdModule;
import com.br.gabrielmartins.syntri.module.ModuleLoader;
import com.br.gabrielmartins.syntri.module.list.optimizer.OptimizerModule;
import com.br.gabrielmartins.syntri.module.list.scoreboard.ScoreboardModule;
import com.br.gabrielmartins.syntri.module.list.tablist.TablistModule;
import com.br.gabrielmartins.syntri.module.loader.ModuleAutoLoader;
import com.br.gabrielmartins.syntri.module.utility.LoadUtilityModulesKt;
import com.br.gabrielmartins.syntri.optimization.ChunkOptimizer;
import com.br.gabrielmartins.syntri.tablist.TablistManager;
import lombok.Getter;
import lombok.Setter;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Getter
public final class SyntriPlugin extends JavaPlugin {

    @Getter @Setter public static SyntriPlugin instance;
    @Getter @Setter public MessagesManager messagesManager;
    @Getter @Setter public Backend backend;
    @Getter @Setter public MongoBackend mongoBackend;
    @Getter @Setter public static InventoryLoader inventoryLoader;
    @Getter @Setter public FileConfiguration configData;
    @Getter @Setter public Economy economy;
    @Getter @Setter public TopMoneyCache topMoneyCache;
    @Getter @Setter public TablistManager tablistManager;
    @Getter @Setter public ModuleLoader loadModules;
    @Getter @Setter public LoaderListener listenerLoad;
    @Getter @Setter public GeneralListener generalLoad;
    @Getter @Setter public static Version version;


    @Override
    public void onLoad() {
        instance = this;
        System.setProperty("file.encoding", "UTF-8");
    }

    @Override
    public void onEnable() {
        instance = this;

        new ChunkOptimizer(this);
        ReflectionUtils.loadUtils();
        version = Version.getServerVersion();
        reloadConfig();
        saveDefaultConfig();
        saveConfig();

        this.loadModules = new ModuleLoader(getDataFolder(), this);
        loadModules.loadModules();

        registerModules();

        ModuleAutoLoader.loadAll(loadModules, this);

        ModuleLoader loader = new ModuleLoader(getDataFolder(), this);

        loader.loadModules();
        new GeneralListener(this, inventoryLoader).listener("com.br.gabrielmartins.syntri.utils");

        if (Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")) {
            new SyntriPlaceholder().register();
        } else {
            new SyntriPlaceholder().register();
        }

        loadConfig();

        this.messagesManager = new MessagesManager(this);

        inventoryLoader = new InventoryLoader();

        setupVault();

        topMoneyCache = new TopMoneyCache();
        topMoneyCache.startUpdater();

        File kitFile = new File(Bukkit.getPluginManager().getPlugin("Syntri").getDataFolder(), "modules/kits/config.yml");

        if (!kitFile.exists()) {
            Bukkit.getPluginManager().getPlugin("Syntri").saveResource("modules/kits/config.yml", false);
        }

        KitManager.loadKits(kitFile);

        listenerLoad = new LoaderListener(this, inventoryLoader);
        listenerLoad.listener("com.br.gabrielmartins.syntri.listener");

        generalLoad = new GeneralListener(this, generalLoad);
        generalLoad.listener("com.br.gabrielmartins.syntri.utils");

        initBackend();

        DataHandler.createTables();

        new CommandLoader(this).load("com.br.gabrielmartins.syntri.commands");

        startAutoMessages();
    }

    @Override
    public void onDisable() {
        if (backend != null && backend.isConnected()) backend.disconnect();
        if (mongoBackend != null && mongoBackend.isConnected()) mongoBackend.disconnect();

        if (tablistManager != null) {
            tablistManager.stop();
        }
    }

    private boolean setupVault() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            broadcastToOps("Vault not found! Economy system will not be initialized.");
            return false;
        }

        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            broadcastToOps("Vault found but no economy provider is registered!");
            return false;
        }

        this.economy = rsp.getProvider();
        if (this.economy == null) {
            broadcastToOps("Invalid economy provider. Make sure a plugin like EssentialsX is installed.");
            return false;
        }

        return true;
    }

    private void broadcastToOps(String message) {
        getLogger().warning(message);
        Bukkit.getOnlinePlayers().stream()
                .filter(Player::isOp)
                .forEach(p -> p.sendMessage("§c[Syntri] " + message));
    }

    private void registerModules() {
        new ScoreboardModule(loadModules, this).register();
        new TablistModule(loadModules, this).register();
        new MotdModule(loadModules, this).register();
        new KitModule(loadModules, this).register();
        new OptimizerModule(loadModules, this).register();
        new GeneralModule(loadModules, this).register();

    }

    private void loadConfig() {
        File configFile = getResourceFile("config.yml");
        try (Reader reader = new InputStreamReader(new FileInputStream(configFile), StandardCharsets.UTF_8)) {
            this.configData = YamlConfiguration.loadConfiguration(reader);
        } catch (Exception ex) {
            getLogger().severe("Error loading config.yml: " + ex.getMessage());
        }
    }

    private void initBackend() {
        ConfigurationSection cfg = getConfig().getConfigurationSection("backend");
        if (cfg == null) return;

        BackendType type = BackendType.fromString(cfg.getString("type"));
        if (type == BackendType.MONGODB) {
            mongoBackend = BackendFactory.buildMongoBackend(cfg);
            mongoBackend.connect();
            if (mongoBackend.isConnected()) {
                DataHandler.setService(new MongoDataService(mongoBackend.getDatabase()));
            }
        } else {
            backend = BackendFactory.buildSQLBackend(cfg, getDataFolder());
            backend.connect();
            if (backend.isConnected()) {
                DataHandler.setService(new SQLDataService(backend));
            }
        }
    }

    private void startAutoMessages() {
        ConfigurationSection section = getConfig().getConfigurationSection("auto-messages");
        if (section == null || !section.getBoolean("enabled", false)) return;

        List<String> messages = section.getStringList("messages.texts");
        long interval = section.getLong("messages.interval", 300) * 20L;
        String prefix = color(section.getString("messages.prefix", "&8[&b&lSyntri&8]"));

        ConfigurationSection titleSection = section.getConfigurationSection("title");
        boolean useTitle = titleSection != null && titleSection.getBoolean("enabled", false);
        String title = color(titleSection.getString("main", "&bWelcome to &lSyntri"));
        String subtitle = color(titleSection.getString("subtitle", "&7Enjoy your stay!"));
        int fadeIn = titleSection.getInt("fade-in", 10);
        int stay = titleSection.getInt("stay", 60);
        int fadeOut = titleSection.getInt("fade-out", 10);
        long titleInterval = titleSection.getLong("interval", 600) * 20L;

        if (!messages.isEmpty()) {
            new BukkitRunnable() {
                int index = 0;

                @Override
                public void run() {
                    String raw = messages.get(index);
                    for (Player player : Bukkit.getOnlinePlayers()) {
                        if (raw.contains("<click:")) {
                            net.md_5.bungee.api.chat.TextComponent component = new net.md_5.bungee.api.chat.TextComponent("");

                            for (String part : raw.split("<click:")) {
                                if (!part.contains(">")) {
                                    component.addExtra(color(part));
                                    continue;
                                }

                                String[] clickParts = part.split(">", 2);
                                String actionTarget = clickParts[0];
                                String text = clickParts.length > 1 ? clickParts[1] : "";

                                net.md_5.bungee.api.chat.TextComponent clickable = new net.md_5.bungee.api.chat.TextComponent(color(text));

                                if (actionTarget.startsWith("http")) {
                                    clickable.setClickEvent(new net.md_5.bungee.api.chat.ClickEvent(
                                            net.md_5.bungee.api.chat.ClickEvent.Action.OPEN_URL, actionTarget));
                                } else {
                                    clickable.setClickEvent(new net.md_5.bungee.api.chat.ClickEvent(
                                            net.md_5.bungee.api.chat.ClickEvent.Action.RUN_COMMAND, actionTarget));
                                }

                                component.addExtra(clickable);
                            }

                            player.spigot().sendMessage(component);
                        } else {
                            player.sendMessage(prefix + " " + color(raw));
                        }
                    }
                    index = (index + 1) % messages.size();
                }
            }.runTaskTimer(this, interval, interval);
        }

        if (useTitle) {
            new BukkitRunnable() {
                @Override
                public void run() {
                    for (Player player : Bukkit.getOnlinePlayers()) {
                        player.sendTitle(title, subtitle, fadeIn, stay, fadeOut);
                    }
                }
            }.runTaskTimer(this, titleInterval, titleInterval);
        }
    }

    private String color(String text) {
        return text.replaceAll("&", "§");
    }

    private File getResourceFile(String name) {
        File file = new File(getDataFolder(), name);
        if (!file.exists()) saveResource(name, false);
        return file;
    }

    @Override
    public FileConfiguration getConfig() {
        if (this.configData == null) {
            this.configData = YamlConfiguration.loadConfiguration(getResourceFile("config.yml"));
        }
        return this.configData;
    }

    public static boolean isRecomendedVersion() {
        if (version == Version.v1_8)
            return true;
        if (version == Version.v1_9)
            return true;
        if (version == Version.v1_10)
            return true;
        if (version == Version.v1_11)
            return true;
        if (version == Version.v1_12)
            return true;
        return false;
    }

    public static boolean isOldVersion() {
        if (version == Version.v1_7)
            return true;
        if (version == Version.v1_6)
            return true;
        if (version == Version.v1_5)
            return true;
        return false;
    }

    public static boolean isVeryOldVersion() {
        if (version == Version.v1_6)
            return true;
        if (version == Version.v1_5)
            return true;
        return false;
    }

    public static boolean isNewVersion() {
        if (version == Version.v1_21)
            return true;
        if (version == Version.v1_20)
            return true;
        if (version == Version.v1_19)
            return true;
        if (version == Version.v1_18)
            return true;
        if (version == Version.v1_17)
            return true;
        if (version == Version.v1_16_5)
            return true;
        if (version == Version.v1_16_4)
            return true;
        if (version == Version.v1_16_3)
            return true;
        if (version == Version.v1_16_2)
            return true;
        if (version == Version.v1_16)
            return true;
        if (version == Version.v1_15)
            return true;
        if (version == Version.v1_14)
            return true;
        if (version == Version.v1_13)
            return true;
        if (version == Version.v1_12)
            return true;
        if (version == Version.v1_11)
            return true;
        return false;
    }

    public static boolean isVeryNewVersion() {
        if (version == Version.v1_21)
            return true;
        if (version == Version.v1_20)
            return true;
        if (version == Version.v1_19)
            return true;
        if (version == Version.v1_18)
            return true;
        if (version == Version.v1_17)
            return true;
        if (version == Version.v1_16_5)
            return true;
        if (version == Version.v1_16_4)
            return true;
        if (version == Version.v1_16_3)
            return true;
        if (version == Version.v1_16_2)
            return true;
        if (version == Version.v1_16)
            return true;
        if (version == Version.v1_15)
            return true;
        if (version == Version.v1_14)
            return true;
        if (version == Version.v1_13)
            return true;
        return false;
    }

    public static boolean isVeryFuckingNewVersion() {
        if (version == Version.v1_21)
            return true;
        if (version == Version.v1_20)
            return true;
        if (version == Version.v1_19)
            return true;
        if (version == Version.v1_18)
            return true;
        if (version == Version.v1_17)
            return true;
        if (version == Version.v1_16_5)
            return true;
        if (version == Version.v1_16_4)
            return true;
        if (version == Version.v1_16_3)
            return true;
        if (version == Version.v1_16_2)
            return true;
        if (version == Version.v1_16)
            return true;
        if (version == Version.v1_15)
            return true;
        if (version == Version.v1_14)
            return true;
        return false;
    }

    public static boolean isMotherFuckerVersion() {
        if (version == Version.v1_21)
            return true;
        if (version == Version.v1_20)
            return true;
        if (version == Version.v1_19)
            return true;
        if (version == Version.v1_18)
            return true;
        if (version == Version.v1_17)
            return true;
        if (version == Version.v1_16_5)
            return true;
        if (version == Version.v1_16_4)
            return true;
        if (version == Version.v1_16_3)
            return true;
        if (version == Version.v1_16_2)
            return true;
        return false;
    }
}