package com.br.gabrielmartins.syntri.commands.registry;

import com.br.gabrielmartins.engine.api.cooldown.Cooldown;
import com.br.gabrielmartins.syntri.SyntriPlugin;
;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class SyntriGerarTokenCommand implements CommandExecutor {

    private static final Map<UUID, CachedToken> tokenCache = new HashMap<>();
    private static final long TOKEN_VALIDITY_MILLIS = 60 * 60 * 1000;

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (!(sender instanceof Player)) {
            sender.sendMessage("§cApenas jogadores podem usar este comando.");
            return true;
        }

        Player player = (Player) sender;
        UUID uuid = player.getUniqueId();

        if (args.length == 1 && args[0].equalsIgnoreCase("gerar")) {

            if (!player.hasPermission("syntri.gerar")) {
                player.sendMessage("§cVocê não tem permissão para isso.");
                return true;
            }

            CachedToken cached = tokenCache.get(uuid);
            if (cached != null && System.currentTimeMillis() < cached.expirationTime) {
                long remainingSeconds = (cached.expirationTime - System.currentTimeMillis()) / 1000;
                player.sendTitle("§eToken já gerado", "§7Aguarde para gerar novamente");

                Cooldown.INSTANCE.start(com.br.gabrielmartins.syntri.SyntriPlugin.getInstance(), player, (int) remainingSeconds, "§bRecriar em: ");
                player.sendMessage("§e✔ Você já possui um token válido!");
                player.sendMessage("§7Token atual: §f" + cached.token);
                player.sendMessage("§7Use: §f/syntri vincular §e" + cached.token);
                player.sendMessage("§7Ou acesse: §f" + SyntriPlugin.getInstance().PAINEL_URL + cached.token);
                return true;
            }

            player.sendTitle("§bGerando Token...", "§7Por favor, aguarde");
            player.sendMessage("§7Solicitando token ao servidor...");

            new Thread(() -> {
                try {
                    Thread.sleep(1000);

                    String ip = getServerIP();
                    if (ip == null || ip.isEmpty()) ip = "localhost";

                    URL url = new URL(SyntriPlugin.getInstance().BASE_URL_GENERATE + "?origin=" + ip);
                    HttpURLConnection con = (HttpURLConnection) url.openConnection();
                    con.setRequestMethod("POST");
                    con.setConnectTimeout(5000);
                    con.setReadTimeout(5000);
                    con.setDoOutput(true);

                    int responseCode = con.getResponseCode();
                    if (responseCode != 200) {
                        throw new RuntimeException("Código HTTP inválido: " + responseCode);
                    }

                    BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
                    StringBuilder response = new StringBuilder();
                    String line;
                    while ((line = in.readLine()) != null) {
                        response.append(line);
                    }
                    in.close();

                    String token = extractToken(response.toString());

                    Bukkit.getScheduler().runTask(SyntriPlugin.getInstance(), () -> {
                        if (!token.equals("ERRO_TOKEN")) {
                            tokenCache.put(uuid, new CachedToken(token, System.currentTimeMillis() + TOKEN_VALIDITY_MILLIS));
                            player.sendTitle("§aToken Gerado!", "§7Vincule com /syntri vincular <token>");
                            player.sendMessage("§a✔ Token gerado com sucesso!");
                            player.sendMessage("§7Token: §f" + token);
                            player.sendMessage("§7Use: §f/syntri vincular §e" + token);
                            player.sendMessage("§7Ou acesse: §f" + SyntriPlugin.getInstance().PAINEL_URL + token);
                        } else {
                            player.sendMessage("§cErro ao interpretar token retornado pela API.");
                        }
                    });

                } catch (Exception e) {
                    Bukkit.getScheduler().runTask(SyntriPlugin.getInstance(), () -> {
                        player.sendMessage("§cErro ao gerar token: §7" + e.getMessage());
                        player.sendMessage("§7Verifique se a API está online e acessível.");
                    });
                    e.printStackTrace();
                }
            }).start();

            return true;
        }

        player.sendMessage("§cUso correto: §f/syntri gerar");
        return true;
    }

    private String getServerIP() {
        try {
            String ip = Bukkit.getServer().getIp();
            if (ip == null || ip.isEmpty()) {
                return InetAddress.getLocalHost().getHostAddress();
            }
            return ip;
        } catch (Exception e) {
            return "localhost";
        }
    }

    private String extractToken(String json) {
        try {
            JSONObject obj = new JSONObject(json);
            return obj.getString("token");
        } catch (Exception e) {
            return "ERRO_TOKEN";
        }
    }

    private static class CachedToken {
        String token;
        long expirationTime;

        CachedToken(String token, long expirationTime) {
            this.token = token;
            this.expirationTime = expirationTime;
        }
    }
}
