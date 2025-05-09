package com.br.gabrielmartins.syntri;

import com.br.gabrielmartins.engine.api.inventory.loader.InventoryLoader;
import com.br.gabrielmartins.engine.api.scoreboard.ScoreboardManager;
import com.br.gabrielmartins.engine.api.tablist.TablistManager;
import com.br.gabrielmartins.engine.backend.Backend;
import com.br.gabrielmartins.engine.backend.BackendType;
import com.br.gabrielmartins.engine.backend.mongo.MongoBackend;
import com.br.gabrielmartins.engine.data.controller.DataHandler;
import com.br.gabrielmartins.engine.data.factory.BackendFactory;
import com.br.gabrielmartins.engine.data.service.mongo.MongoDataService;
import com.br.gabrielmartins.engine.data.service.sql.SQLDataService;
import com.br.gabrielmartins.engine.loader.command.CommandLoader;
import com.br.gabrielmartins.engine.loader.listener.LoaderListener;
import com.br.gabrielmartins.syntri.cache.TopMoneyCache;
import com.br.gabrielmartins.syntri.enums.JarType;
import com.br.gabrielmartins.syntri.kit.manager.KitManager;
import com.br.gabrielmartins.syntri.listener.scoreboard.ScoreboardListener;
import lombok.Getter;
import lombok.Setter;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Getter
public final class SyntriPlugin extends JavaPlugin {

    public static final String BASE_URL_GENERATE = "http://localhost:8080/api/v1/keys/generate";
    public static final String PANEL_URL = "https://panel.localhost:3000/server/";

    @Getter
    private static SyntriPlugin instance;

    @Getter @Setter private MessagesManager messagesManager;

    private Backend backend;
    private MongoBackend mongoBackend;
    private static InventoryLoader inventoryLoader;
    private FileConfiguration configData;
    private JarType jarType;
    private Economy economy;
    private TopMoneyCache topMoneyCache;
    private TablistManager tablistManager;

    @Setter
    private LoaderListener listenerLoad;

    @Override
    public void onLoad() {
        instance = this;
        System.setProperty("file.encoding", "UTF-8");


    }

    @Override
    public void onEnable() {
        reloadConfig();
        saveDefaultConfig();
        saveConfig();
        new SyntriPlaceholder().canRegister();
        this.jarType = JarType.getJarType();
        loadConfig();
        ScoreboardManager scoreboardManager = new ScoreboardManager(getConfig(), this);

        for (Player player : Bukkit.getOnlinePlayers()) {
            scoreboardManager.apply(player);
        }
        tablistManager = new TablistManager(this, getConfig());
        tablistManager.start();
        scoreboardManager.startTitleAnimation(Bukkit.getOnlinePlayers());
        this.messagesManager = new MessagesManager(this);


        inventoryLoader = new InventoryLoader();

        if (getConfig().getBoolean("economy.enabled", true)) {
            getServer().getServicesManager().register(
                    Economy.class,
                    new com.br.gabrielmartins.syntri.economy.impl.SyntriEconomyImpl() {
                        @Override public boolean has(String s, double v) {return false;}
                        @Override public boolean has(String s, String s1, double v) {return false;}
                        @Override public EconomyResponse withdrawPlayer(String s, String s1, double v) {return null;}
                        @Override public EconomyResponse depositPlayer(String s, String s1, double v) {return null;}
                        @Override public EconomyResponse createBank(String s, String s1) {return null;}
                        @Override public EconomyResponse deleteBank(String s) {return null;}
                        @Override public boolean createPlayerAccount(String s) {return false;}
                        @Override public boolean createPlayerAccount(String s, String s1) {return false;}}, this, ServicePriority.Highest
            );
            setupVault();
        } else {
            broadcastToOps("Economy system disabled via config.yml (economy.enabled = false)");
        }

        topMoneyCache = new TopMoneyCache();
        topMoneyCache.startUpdater();

        com.br.gabrielmartins.syntri.kit.manager.KitManager.loadKits(getResourceFile("kits.yml"));
        new ScoreboardListener();

        listenerLoad = new LoaderListener(this, inventoryLoader);
        listenerLoad.listener("com.br.gabrielmartins.syntri.listener");

        initBackend();
        DataHandler.createTables();

        new CommandLoader(this).load("com.br.gabrielmartins.syntri.commands");

        if (Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")) {
            new SyntriPlaceholder().register();
        }
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

}