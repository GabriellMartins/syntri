package com.br.gabrielmartins.syntri.backend.postgresql;

import com.br.gabrielmartins.syntri.backend.Backend;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.Connection;
import java.sql.SQLException;

public class PostgreSQLBackend implements Backend {

    private final HikariDataSource dataSource;

    public PostgreSQLBackend(String host, int port, String database, String username, String password) {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl("jdbc:postgresql://" + host + ":" + port + "/" + database);
        config.setUsername(username);
        config.setPassword(password);

        config.setMaximumPoolSize(10);
        config.setMinimumIdle(2);
        config.setIdleTimeout(60000);
        config.setMaxLifetime(1800000);
        config.setConnectionTimeout(30000);

        this.dataSource = new HikariDataSource(config);
    }

    @Override
    public void connect() {
        try (Connection conn = getConnection()) {
            System.out.println("[PostgreSQL] Conectado com sucesso.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void disconnect() {
        if (!dataSource.isClosed()) {
            dataSource.close();
            System.out.println("[PostgreSQL] Conexão encerrada.");
        }
    }

    @Override
    public boolean isConnected() {
        return !dataSource.isClosed();
    }

    @Override
    public Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }
}
