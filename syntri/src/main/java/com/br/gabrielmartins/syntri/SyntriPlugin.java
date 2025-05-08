package com.br.gabrielmartins.syntri;

import com.br.gabrielmartins.engine.api.inventory.loader.InventoryLoader;
import com.br.gabrielmartins.engine.api.translate.Translate;
import com.br.gabrielmartins.engine.backend.Backend;
import com.br.gabrielmartins.engine.backend.BackendType;
import com.br.gabrielmartins.engine.backend.mongo.MongoBackend;
import com.br.gabrielmartins.engine.kit.manager.KitManager;
import com.br.gabrielmartins.engine.loader.command.CommandLoader;
import com.br.gabrielmartins.engine.loader.listener.LoaderListener;
import com.br.gabrielmartins.engine.data.controller.DataHandler;
import com.br.gabrielmartins.engine.data.factory.BackendFactory;
import com.br.gabrielmartins.engine.data.service.mongo.MongoDataService;
import com.br.gabrielmartins.engine.data.service.sql.SQLDataService;

import com.br.gabrielmartins.syntri.listener.scoreboard.ScoreboardListener;
import com.br.gabrielmartins.syntri.listener.server.ConfigListener;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.*;
import java.nio.charset.StandardCharsets;

public final class SyntriPlugin extends JavaPlugin {

    public static final String BASE_URL_GENERATE = "http://localhost:8080/api/v1/keys/generate";
    public static final String PAINEL_URL = "https://painel.localhost:3000/server/";

    @Getter
    private static SyntriPlugin instance;

    @Getter
    private Backend backend;

    @Getter
    private MongoBackend mongoBackend;

    @Getter @Setter
    private LoaderListener listenerLoad;

    @Getter
    private FileConfiguration configData;

    @Getter
    private static InventoryLoader inventoryLoader;

    @Override
    public void onLoad() {
        instance = this;

        System.setProperty("file.encoding", "UTF-8");
        System.setOut(new PrintStream(System.out) {
            @Override
            public void println(String x) {
                if (x != null && x.contains("Default system encoding may have misread config.yml")) return;
                super.println(x);
            }
        });

        saveDefaultConfig();
    }

    @Override
    public void onEnable() {
        inventoryLoader = new InventoryLoader();

        File kitFile = new File(getDataFolder(), "kits.yml");
        if (!kitFile.exists()) saveResource("kits.yml", false);
        KitManager.loadKits(kitFile);

        File configFile = new File(getDataFolder(), "config.yml");
        if (!configFile.exists()) saveResource("config.yml", false);
        loadConfigWithUTF8(configFile);

        com.br.gabrielmartins.syntri.listener.manager.ListenerRegistry.autoRegister(com.br.gabrielmartins.syntri.listener.general.GeneralListener.class);

        initBackend();
        DataHandler.createTables();

        new ScoreboardListener();
        new ConfigListener();
        new CommandLoader(this).load("com.br.gabrielmartins.syntri.commands");
        listenerLoad.listener("com.br.gabrielmartins.syntri.listener");

        String language = getConfig().getString("general.language", "br");
        Translate.setLanguage(language);
        Translate.load();

        if (Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")) {
            boolean registered = new SyntriPlaceholder().canRegister();
            if (registered) {
                getLogger().info("§aPlaceholderAPI detectado e SyntriPlaceholder registrado com sucesso.");
            } else {
                getLogger().warning("§cErro ao registrar SyntriPlaceholder.");
            }
        } else {
            getLogger().warning("§ePlaceholderAPI não encontrado. Placeholders Syntri não foram registrados.");
        }

        getLogger().info("§aSyntri iniciado com sucesso!");
    }

    @Override
    public void onDisable() {
        if (backend != null && backend.isConnected()) {
            backend.disconnect();
        }
        getLogger().info("§cSyntri finalizado com sucesso.");
    }

    private void loadConfigWithUTF8(File configFile) {
        try (Reader reader = new InputStreamReader(new FileInputStream(configFile), StandardCharsets.UTF_8)) {
            this.configData = YamlConfiguration.loadConfiguration(reader);
        } catch (Exception e) {
            getLogger().severe("Erro ao carregar o config.yml com UTF-8.");
            e.printStackTrace();
        }
    }

    private void initBackend() {
        ConfigurationSection cfg = getConfig().getConfigurationSection("backend");
        if (cfg == null) {
            getLogger().severe("§cSeção 'backend' não encontrada no config.yml!");
            return;
        }

        BackendType type = BackendType.fromString(cfg.getString("type"));
        if (type == null) {
            getLogger().severe("§cTipo de backend inválido no config.yml.");
            return;
        }

        try {
            if (type == BackendType.MONGODB) {
                this.mongoBackend = BackendFactory.buildMongoBackend(cfg);
                mongoBackend.connect();
                if (mongoBackend.isConnected()) {
                    DataHandler.setService(new MongoDataService(mongoBackend.getDatabase()));
                    getLogger().info("§7[Syntri] MongoDB conectado com sucesso.");
                }
            } else {
                this.backend = BackendFactory.buildSQLBackend(cfg, getDataFolder());
                backend.connect();
                if (backend.isConnected()) {
                    DataHandler.setService(new SQLDataService(backend));
                    DataHandler.createTables();
                    getLogger().info("§7[Syntri] Banco de dados SQL conectado com sucesso.");
                }
            }
        } catch (Exception e) {
            getLogger().severe("§cErro ao conectar com o banco de dados:");
            e.printStackTrace();
        }
    }

    @Override
    public FileConfiguration getConfig() {
        return this.configData;
    }
}
