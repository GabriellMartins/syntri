package com.br.gabrielmartins.syntri.listener.server;

import com.br.gabrielmartins.syntri.SyntriPlugin;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.scheduler.BukkitRunnable;

public class ConfigListener {

    private final SyntriPlugin plugin = SyntriPlugin.getInstance();
    private int currentMessageIndex = 0;

    public ConfigListener() {
        new BukkitRunnable() {
            @Override
            public void run() {
                loadConfigurations();
            }
        }.runTaskTimer(plugin, 0, 1L); // 1L = 1 tick (aproximadamente 50ms)
    }

    // Método para carregar as configurações do config.yml
    private void loadConfigurations() {
        // Força o reload do arquivo config.yml
        plugin.reloadConfig();
        FileConfiguration config = plugin.getConfig();

        // Verifica a configuração do banco de dados
        String backendType = config.getString("backend.type", "mysql");
        String backendHost = config.getString("backend.host", "localhost");
        int backendPort = config.getInt("backend.port", 3306);
        String backendDatabase = config.getString("backend.database", "my_database");
        String backendUsername = config.getString("backend.username", "root");
        String backendPassword = config.getString("backend.password", "");

        // Carrega a configuração do idioma do servidor
        String language = config.getString("general.language", "us");

        // Mensagem de boas-vindas
        boolean welcomeMessageEnabled = config.getBoolean("general.welcome-message.enabled", true);
        String welcomeMessageText = config.getString("general.welcome-message.text", "&aBem-vindo ao servidor!");

        // Mensagem de saída
        boolean quitMessageEnabled = config.getBoolean("general.quit-message.enabled", true);
        String quitMessageText = config.getString("general.quit-message.text", "&e[Syntri] O jogador %player_name% saiu do servidor.");

        // Comportamentos automáticos
        boolean preventDamage = config.getBoolean("general.prevent-damage", false);
        boolean preventHunger = config.getBoolean("general.prevent-hunger", false);
        boolean preventBlockBreak = config.getBoolean("general.prevent-block-break", false);
        boolean preventBlockPlace = config.getBoolean("general.prevent-block-place", false);
        boolean preventInteract = config.getBoolean("general.prevent-interact", false);
        boolean preventItemDrop = config.getBoolean("general.prevent-item-drop", false);
        boolean preventItemPickup = config.getBoolean("general.prevent-item-pickup", false);
        boolean preventWeatherChange = config.getBoolean("general.prevent-weather-change", false);
        boolean preventGamemodeChange = config.getBoolean("general.prevent-gamemode-change", false);

        // Atualização do MOTD
        if (config.getBoolean("motd.enabled", true)) {
            String motd = config.getStringList("motd.messages").get(currentMessageIndex);
            motd = ChatColor.translateAlternateColorCodes('&', motd); // Aplica a formatação de cores

            // Atualiza o índice do MOTD
            currentMessageIndex = (currentMessageIndex + 1) % config.getStringList("motd.messages").size();
        }
    }
}
