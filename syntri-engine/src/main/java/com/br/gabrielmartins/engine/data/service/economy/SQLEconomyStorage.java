package com.br.gabrielmartins.engine.data.service.economy;

import com.br.gabrielmartins.engine.backend.Backend;
import com.br.gabrielmartins.engine.data.storage.EconomyStorage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class SQLEconomyStorage implements EconomyStorage {

    private final Backend backend;

    public SQLEconomyStorage(Backend backend) {
        this.backend = backend;
    }

    @Override
    public void createAccount(UUID uuid) {
        try (Connection conn = backend.getConnection();
             PreparedStatement ps = conn.prepareStatement(
                     "INSERT IGNORE INTO economy (uuid, balance) VALUES (?, 0.0)")) {
            ps.setString(1, uuid.toString());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean hasAccount(UUID uuid) {
        try (Connection conn = backend.getConnection();
             PreparedStatement ps = conn.prepareStatement(
                     "SELECT 1 FROM economy WHERE uuid = ?")) {
            ps.setString(1, uuid.toString());
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public double getBalance(UUID uuid) {
        try (Connection conn = backend.getConnection();
             PreparedStatement ps = conn.prepareStatement(
                     "SELECT balance FROM economy WHERE uuid = ?")) {
            ps.setString(1, uuid.toString());
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getDouble("balance");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0.0;
    }

    @Override
    public void setBalance(UUID uuid, double amount) {
        try (Connection conn = backend.getConnection();
             PreparedStatement ps = conn.prepareStatement(
                     "INSERT INTO economy (uuid, balance) VALUES (?, ?) " +
                             "ON DUPLICATE KEY UPDATE balance = VALUES(balance)")) {
            ps.setString(1, uuid.toString());
            ps.setDouble(2, amount);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
