package com.br.gabrielmartins.engine.backend.mongo;

import com.br.gabrielmartins.commons.backend.DocumentBackend;
import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;

public class MongoBackend implements DocumentBackend {

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
    public MongoDatabase getDatabase() {
        return database;
    }
}
