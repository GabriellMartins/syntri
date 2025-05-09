package com.br.gabrielmartins.engine.backend.redis.impl;

import com.br.gabrielmartins.engine.backend.redis.packet.Packet;

public class BroadcastMessagePacket extends Packet {

    private String message;

    public BroadcastMessagePacket() {}

    public BroadcastMessagePacket(String message) {
        this.message = message;
    }

    @Override
    public String getChannel() {
        return "broadcast_channel";
    }

    @Override
    public void handle() {
        System.out.println("[Broadcast] Mensagem recebida: " + message);
    }
}
