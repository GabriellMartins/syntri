package com.br.gabrielmartins.syntri.backend.sqllite;

import com.br.gabrielmartins.syntri.backend.Backend;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.Connection;
import java.sql.SQLException;

public class SQLiteBackend implements Backend {

    private final HikariDataSource dataSource;

    public SQLiteBackend(String databaseFileName) {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl("jdbc:sqlite:" + databaseFileName);
        config.setDriverClassName("org.sqlite.JDBC");
        config.setMaximumPoolSize(1);
        config.setConnectionTestQuery("SELECT 1");
        config.setPoolName("SQLitePool");
        // config.addDataSourceProperty("journal_mode", "WAL");

        this.dataSource = new HikariDataSource(config);
    }

    @Override
    public Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }

    @Override
    public void connect() {
        try (Connection conn = getConnection()) {
            System.out.println("[SQLite] Conectado com sucesso.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void disconnect() {
        if (dataSource != null && !dataSource.isClosed()) {
            dataSource.close();
            System.out.println("[SQLite] Conexão encerrada.");
        }
    }

    @Override
    public boolean isConnected() {
        return !dataSource.isClosed();
    }
}
