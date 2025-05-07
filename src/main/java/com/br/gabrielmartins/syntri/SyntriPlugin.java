package com.br.gabrielmartins.syntri;

import com.br.gabrielmartins.syntri.api.translate.Translate;
import com.br.gabrielmartins.syntri.backend.*;
import com.br.gabrielmartins.syntri.backend.firebird.FirebirdBackend;
import com.br.gabrielmartins.syntri.backend.hikari.Hikari;
import com.br.gabrielmartins.syntri.backend.mariadb.MariaDBBackend;
import com.br.gabrielmartins.syntri.backend.mongo.MongoBackend;
import com.br.gabrielmartins.syntri.backend.oracle.OracleBackend;
import com.br.gabrielmartins.syntri.backend.postgresql.PostgreSQLBackend;
import com.br.gabrielmartins.syntri.backend.sqllite.SQLiteBackend;
import com.br.gabrielmartins.syntri.backend.sqlserver.SQLServerBackend;
import com.br.gabrielmartins.syntri.data.controller.DataHandler;

import com.br.gabrielmartins.syntri.kit.manager.KitManager;
import com.br.gabrielmartins.syntri.listener.server.ConfigListener;
import com.br.gabrielmartins.syntri.loader.command.CommandLoader;
import com.br.gabrielmartins.syntri.loader.listener.LoaderListener;
import com.br.gabrielmartins.syntri.utils.placeholder.SyntriPlaceholder;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.logging.Filter;
import java.util.logging.LogRecord;


public final class SyntriPlugin extends JavaPlugin {

    @Getter private static SyntriPlugin instance;
    @Getter private Backend backend;
    @Getter private final LoaderListener loader = new LoaderListener();
    @Getter private FileConfiguration config;

    @Override
    public void onLoad() {
        instance = this;

        getLogger().setFilter(new Filter() {
            @Override
            public boolean isLoggable(LogRecord record) {
                return record.getLevel().intValue() < java.util.logging.Level.WARNING.intValue();
            }
        });
    }

    @Override
    public void onEnable() {

        File kitFile = new File(getDataFolder(), "kits.yml");
        if (!kitFile.exists()) {
            saveResource("kits.yml", false);
        }
        KitManager.loadKits(kitFile);

        File configFile = new File(getDataFolder(), "config.yml");
        if (!configFile.exists()) {
            saveResource("config.yml", false);
        }
        loadConfigWithUTF8(configFile);

        initBackend();
        DataHandler.createTables();

        new ConfigListener();
        new CommandLoader(this).load("com.br.gabrielmartins.syntri.commands");
        loader.listener("com.br.gabrielmartins.syntri.listener");

        String language = getConfig().getString("general.language", "br");
        Translate.setLanguage(language);
        Translate.load();

        if (Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")) {
            boolean registered = new SyntriPlaceholder().register();
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
            this.config = YamlConfiguration.loadConfiguration(reader);
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

        BackendType type = BackendType.fromString(cfg.getString("type", "sqlite"));
        if (type == null) {
            getLogger().severe("§cTipo de backend inválido!");
            return;
        }

        switch (type) {
            case MYSQL:
                backend = new Hikari(cfg.getString("host"), cfg.getInt("port"), cfg.getString("database"), cfg.getString("username"), cfg.getString("password"));
                break;
            case SQLITE:
                backend = new SQLiteBackend(getDataFolder() + "/" + cfg.getString("file", "database.db"));
                break;
            case MONGODB:
                backend = new MongoBackend(cfg.getString("uri", "mongodb://localhost:27017"), cfg.getString("database", "syntri"));
                break;
            case POSTGRESQL:
                backend = new PostgreSQLBackend(cfg.getString("host"), cfg.getInt("port"), cfg.getString("database"), cfg.getString("username"), cfg.getString("password"));
                break;
            case MARIADB:
                backend = new MariaDBBackend(cfg.getString("host"), cfg.getInt("port"), cfg.getString("database"), cfg.getString("username"), cfg.getString("password"));
                break;
            case ORACLE:
                backend = new OracleBackend(cfg.getString("host"), cfg.getInt("port"), cfg.getString("service"), cfg.getString("username"), cfg.getString("password"));
                break;
            case FIREBIRD:
                backend = new FirebirdBackend(cfg.getString("host"), cfg.getInt("port"), cfg.getString("database"), cfg.getString("username"), cfg.getString("password"));
                break;
            case SQLSERVER:
                backend = new SQLServerBackend(cfg.getString("host"), cfg.getInt("port"), cfg.getString("database"), cfg.getString("username"), cfg.getString("password"));
                break;
        }

        backend.connect();
    }
    @Override
    public FileConfiguration getConfig() {
        return this.config;
    }

}
