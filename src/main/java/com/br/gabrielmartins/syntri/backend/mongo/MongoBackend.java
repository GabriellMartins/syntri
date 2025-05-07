package com.br.gabrielmartins.syntri.backend.mongo;

import com.br.gabrielmartins.syntri.backend.Backend;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;

import java.sql.Connection;
import java.sql.SQLException;

public class MongoBackend implements Backend {

    private final String uri;
    private final String databaseName;
    private MongoClient client;
    private MongoDatabase database;

    public MongoBackend(String uri, String databaseName) {
        this.uri = uri;
        this.databaseName = databaseName;
    }

    @Override
    public void connect() {
        this.client = MongoClients.create(
                MongoClientSettings.builder()
                        .applyConnectionString(new ConnectionString(uri))
                        .build()
        );
        this.database = client.getDatabase(databaseName);
        System.out.println("[MongoDB] Conectado com sucesso ao banco " + databaseName);
    }

    @Override
    public void disconnect() {
        if (client != null) {
            client.close();
            System.out.println("[MongoDB] Conexão encerrada.");
        }
    }

    @Override
    public boolean isConnected() {
        return client != null;
    }

    @Override
    public Connection getConnection() throws SQLException {
        throw new UnsupportedOperationException("MongoDB não usa JDBC.");
    }

    public MongoDatabase getDatabase() {
        return database;
    }
}
