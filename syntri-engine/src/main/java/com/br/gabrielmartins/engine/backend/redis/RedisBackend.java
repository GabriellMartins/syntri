package com.br.gabrielmartins.engine.backend.redis;

import com.br.gabrielmartins.engine.backend.Backend;
import com.br.gabrielmartins.engine.backend.redis.config.RedisConfig;
import com.br.gabrielmartins.engine.backend.redis.core.RedisConnection;
import com.br.gabrielmartins.engine.backend.redis.impl.BroadcastMessagePacket;
import com.br.gabrielmartins.engine.backend.redis.manager.RedisManager;
import com.br.gabrielmartins.engine.backend.redis.packet.event.RedisPacketEvent;


public class RedisBackend implements Backend {

    private final RedisConnection connection;
    private final RedisManager manager;

    public RedisBackend(String host, int port, String password) {
        RedisConfig config = new RedisConfig(host, port, password);
        this.connection = new RedisConnection(config);
        this.manager = new RedisManager(connection);

        new RedisPacketEvent(connection.getPool(), BroadcastMessagePacket.class, "broadcast_channel").start();
    }

    public RedisManager getManager() {
        return manager;
    }

    @Override
    public void connect() {
        System.out.println("[Redis] Conexão estabelecida.");
    }

    @Override
    public void disconnect() {
        connection.close();
        System.out.println("[Redis] Conexão finalizada.");
    }

    @Override
    public boolean isConnected() {
        try {
            return connection.getPool().getResource().isConnected();
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public java.sql.Connection getConnection() {
        throw new UnsupportedOperationException("Redis não utiliza JDBC.");
    }
}
