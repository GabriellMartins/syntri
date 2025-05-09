package com.br.gabrielmartins.engine.backend.redis.manager;

import com.br.gabrielmartins.engine.backend.redis.core.RedisConnection;
import redis.clients.jedis.Jedis;

public class RedisManager {
    private final RedisConnection connection;

    public RedisManager(RedisConnection connection) {
        this.connection = connection;
    }

    public void set(String key, String value) {
        try (Jedis jedis = connection.getPool().getResource()) {
            jedis.set(key, value);
        }
    }

    public String get(String key) {
        try (Jedis jedis = connection.getPool().getResource()) {
            return jedis.get(key);
        }
    }

    public void publish(String channel, String message) {
        try (Jedis jedis = connection.getPool().getResource()) {
            jedis.publish(channel, message);
        }
    }
}
