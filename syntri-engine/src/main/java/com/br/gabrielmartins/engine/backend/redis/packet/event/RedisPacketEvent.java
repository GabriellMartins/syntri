package com.br.gabrielmartins.engine.backend.redis.packet.event;

import com.br.gabrielmartins.engine.backend.redis.packet.Packet;

import com.br.gabrielmartins.engine.backend.redis.packet.handler.PacketHandler;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

public class RedisPacketEvent extends Thread {

    private final JedisPool pool;
    private final Class<? extends Packet> packetClass;
    private final String channel;

    public RedisPacketEvent(JedisPool pool, Class<? extends Packet> packetClass, String channel) {
        this.pool = pool;
        this.packetClass = packetClass;
        this.channel = channel;
    }

    @Override
    public void run() {
        try (Jedis jedis = pool.getResource()) {
            jedis.subscribe(new PacketHandler(packetClass), channel);
        }
    }
}
