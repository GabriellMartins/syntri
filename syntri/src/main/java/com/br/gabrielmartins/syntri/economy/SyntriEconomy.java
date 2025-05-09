package com.br.gabrielmartins.syntri.economy;

import com.br.gabrielmartins.engine.backend.Backend;
import com.br.gabrielmartins.syntri.SyntriPlugin;
import net.milkbowl.vault.economy.AbstractEconomy;
import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.*;

public abstract class SyntriEconomy extends AbstractEconomy {

    private final Map<UUID, Double> balances = new HashMap<>();
    private final boolean enabled;

    public SyntriEconomy() {
        this.enabled = SyntriPlugin.getInstance().getConfig().getBoolean("economy.enabled", true);
        if (!enabled) return;
        createTableIfNotExists();
        loadBalances();
    }

    private void createTableIfNotExists() {
        Backend backend = SyntriPlugin.getInstance().getBackend();
        if (backend == null) return;
        try (Connection conn = backend.getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.executeUpdate("""
                    CREATE TABLE IF NOT EXISTS economy (
                        uuid VARCHAR(36) PRIMARY KEY,
                        balance DOUBLE NOT NULL DEFAULT 0
                    );
                    """);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadBalances() {
        Backend backend = SyntriPlugin.getInstance().getBackend();
        if (backend == null) return;
        try (Connection conn = backend.getConnection();
             PreparedStatement stmt = conn.prepareStatement("SELECT uuid, balance FROM economy")) {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                UUID uuid = UUID.fromString(rs.getString("uuid"));
                double balance = rs.getDouble("balance");
                balances.put(uuid, balance);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void saveBalance(UUID uuid, double balance) {
        Backend backend = SyntriPlugin.getInstance().getBackend();
        if (backend == null) return;
        try (Connection conn = backend.getConnection();
             PreparedStatement stmt = conn.prepareStatement("REPLACE INTO economy (uuid, balance) VALUES (?, ?);")) {
            stmt.setString(1, uuid.toString());
            stmt.setDouble(2, balance);
            stmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean hasAccount(OfflinePlayer player) {
        return enabled && balances.containsKey(player.getUniqueId());
    }

    @Override
    public boolean hasAccount(String playerName) {
        return hasAccount(Bukkit.getOfflinePlayer(playerName));
    }

    @Override
    public boolean hasAccount(String playerName, String world) {
        return hasAccount(playerName);
    }

    @Override
    public boolean hasAccount(OfflinePlayer player, String world) {
        return hasAccount(player);
    }

    @Override
    public double getBalance(OfflinePlayer player) {
        return enabled ? balances.getOrDefault(player.getUniqueId(), 0.0) : 0.0;
    }

    @Override
    public double getBalance(String playerName) {
        return getBalance(Bukkit.getOfflinePlayer(playerName));
    }

    @Override
    public double getBalance(String playerName, String world) {
        return getBalance(playerName);
    }

    @Override
    public EconomyResponse withdrawPlayer(OfflinePlayer player, double amount) {
        if (!enabled) return notImplemented();
        UUID id = player.getUniqueId();
        double balance = getBalance(player);
        if (balance < amount)
            return new EconomyResponse(0, balance, EconomyResponse.ResponseType.FAILURE, "Saldo insuficiente");
        balances.put(id, balance - amount);
        saveBalance(id, balance - amount);
        return new EconomyResponse(amount, balance - amount, EconomyResponse.ResponseType.SUCCESS, null);
    }

    @Override
    public EconomyResponse withdrawPlayer(String playerName, double amount) {
        return withdrawPlayer(Bukkit.getOfflinePlayer(playerName), amount);
    }

    @Override
    public EconomyResponse depositPlayer(OfflinePlayer player, double amount) {
        if (!enabled) return notImplemented();
        UUID id = player.getUniqueId();
        double balance = getBalance(player);
        balances.put(id, balance + amount);
        saveBalance(id, balance + amount);
        return new EconomyResponse(amount, balance + amount, EconomyResponse.ResponseType.SUCCESS, null);
    }

    @Override
    public EconomyResponse depositPlayer(String playerName, double amount) {
        return depositPlayer(Bukkit.getOfflinePlayer(playerName), amount);
    }

    @Override
    public String format(double amount) {
        return String.format("§2$%.2f", amount);
    }

    @Override public String getName() { return "SyntriEconomy"; }
    @Override public boolean isEnabled() { return enabled; }
    @Override public String currencyNamePlural() { return "coins"; }
    @Override public String currencyNameSingular() { return "coin"; }
    @Override public boolean hasBankSupport() { return false; }
    @Override public int fractionalDigits() { return 2; }

    @Override public EconomyResponse bankBalance(String name) { return notImplemented(); }
    @Override public EconomyResponse bankDeposit(String name, double amount) { return notImplemented(); }
    @Override public EconomyResponse bankWithdraw(String name, double amount) { return notImplemented(); }
    @Override public EconomyResponse bankHas(String name, double amount) { return notImplemented(); }
    @Override public EconomyResponse isBankOwner(String name, String player) { return notImplemented(); }
    @Override public EconomyResponse isBankMember(String name, String player) { return notImplemented(); }
    @Override public List<String> getBanks() { return List.of(); }

    private EconomyResponse notImplemented() {
        return new EconomyResponse(0, 0, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "Economy system disabled or unsupported");
    }
}
