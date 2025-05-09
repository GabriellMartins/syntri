package com.br.gabrielmartins.engine.backend.redis.packet.handler;

import com.br.gabrielmartins.engine.backend.redis.packet.Packet;
import redis.clients.jedis.JedisPubSub;

public class PacketHandler extends JedisPubSub {
    private final Class<? extends Packet> packetClass;

    public PacketHandler(Class<? extends Packet> packetClass) {
        this.packetClass = packetClass;
    }

    @Override
    public void onMessage(String channel, String message) {
        try {
            Packet packet = Packet.fromJson(message, packetClass);
            if (packet != null) packet.handle();
        } catch (Exception e) {
            System.out.println("Erro ao processar packet do canal: " + channel);
            e.printStackTrace();
        }
    }
}
