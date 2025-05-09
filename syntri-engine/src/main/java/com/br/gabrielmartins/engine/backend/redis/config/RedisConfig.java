package com.br.gabrielmartins.engine.backend.redis.config;

public class RedisConfig {
    private final String host;
    private final int port;
    private final String password;

    public RedisConfig(String host, int port, String password) {
        this.host = host;
        this.port = port;
        this.password = password;
    }

    public String getHost() { return host; }
    public int getPort() { return port; }
    public String getPassword() { return password; }
}
