package com.br.gabrielmartins.syntri;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class MessagesManager {

    private final JavaPlugin plugin;
    private File messagesFile;
    private FileConfiguration messages;

    public MessagesManager(JavaPlugin plugin) {
        this.plugin = plugin;
        setup();
    }

    private void setup() {
        messagesFile = new File(plugin.getDataFolder(), "messages.yml");

        if (!messagesFile.exists()) {
            plugin.saveResource("messages.yml", false);
        }

        File permissionsFile = new File(plugin.getDataFolder(), "permissions.md");
        if (!permissionsFile.exists()) {
            plugin.saveResource("permissions.md", false);
        }

        messages = YamlConfiguration.loadConfiguration(messagesFile);
    }

    public FileConfiguration getMessages() {
        return messages;
    }

    public String getPermissionsMarkdown() {
        File permissionsFile = new File(plugin.getDataFolder(), "permissions.md");

        if (!permissionsFile.exists()) {
            plugin.saveResource("permissions.md", false);
            return "No permissions.md found. Default file created.";
        }

        try {
            return java.nio.file.Files.readString(permissionsFile.toPath());
        } catch (IOException e) {
            plugin.getLogger().severe("Não foi possível ler o permissions.md");
            e.printStackTrace();
            return "Erro ao carregar permissions.md";
        }
    }

    public String getMessage(String path) {
        if (messages.isList(path)) {
            List<String> lines = messages.getStringList(path);
            return lines.stream()
                    .map(line -> ChatColor.translateAlternateColorCodes('&', line))
                    .collect(Collectors.joining("\n"));
        }

        String message = messages.getString(path);
        return ChatColor.translateAlternateColorCodes('&',
                message != null ? message : "§cMensagem não encontrada: " + path);
    }

    public List<String> getMessageList(String path) {
        List<String> lines = messages.getStringList(path);
        return lines.stream()
                .map(line -> ChatColor.translateAlternateColorCodes('&', line))
                .collect(Collectors.toList());
    }

    public void reloadMessages() {
        messages = YamlConfiguration.loadConfiguration(messagesFile);
    }

    public void saveMessages() {
        try {
            messages.save(messagesFile);
        } catch (IOException e) {
            plugin.getLogger().severe("Não foi possível salvar o messages.yml");
            e.printStackTrace();
        }
    }

    public void saveDefaultMessages() {
        if (!messagesFile.exists()) {
            plugin.saveResource("messages.yml", false);
        }
    }
}
