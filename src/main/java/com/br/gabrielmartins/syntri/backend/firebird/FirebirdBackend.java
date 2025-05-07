package com.br.gabrielmartins.syntri.backend.firebird;

import com.br.gabrielmartins.syntri.backend.Backend;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.Connection;
import java.sql.SQLException;

public class FirebirdBackend implements Backend {

    private final HikariDataSource dataSource;

    public FirebirdBackend(String host, int port, String databasePath, String username, String password) {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl("jdbc:firebirdsql://" + host + ":" + port + "/" + databasePath);
        config.setUsername(username);
        config.setPassword(password);

        config.setMaximumPoolSize(5);
        config.setConnectionTimeout(30000);

        this.dataSource = new HikariDataSource(config);
    }

    @Override
    public void connect() {
        try (Connection conn = getConnection()) {
            System.out.println("[Firebird] Conectado com sucesso.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void disconnect() {
        if (!dataSource.isClosed()) {
            dataSource.close();
            System.out.println("[Firebird] Conexão encerrada.");
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
