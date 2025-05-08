package com.br.gabrielmartins.syntri.backend;

import com.mongodb.client.MongoDatabase;

public interface DocumentBackend {
    MongoDatabase getDatabase();
    void connect();
    void disconnect();
    boolean isConnected();
}
