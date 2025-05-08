package com.br.gabrielmartins.engine.backend;

import java.sql.Connection;
import java.sql.SQLException;

public interface Backend {

    Connection getConnection() throws SQLException;
    void connect();
    void disconnect();
    boolean isConnected();
}
