package com.br.gabrielmartins.engine.backend.redis.core;

import com.br.gabrielmartins.engine.backend.redis.config.RedisConfig;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

public class RedisConnection {
    private final JedisPool jedisPool;

    public RedisConnection(RedisConfig config) {
        JedisPoolConfig poolConfig = new JedisPoolConfig();
        poolConfig.setMaxTotal(10);
        this.jedisPool = new JedisPool(poolConfig, config.getHost(), config.getPort(), 2000, config.getPassword());
    }

    public JedisPool getPool() {
        return jedisPool;
    }

    public void close() {
        if (!jedisPool.isClosed()) jedisPool.close();
    }
}
