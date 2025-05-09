package com.br.gabrielmartins.engine.backend.redis.packet;

import com.google.gson.Gson;

public abstract class Packet {
    private static final Gson gson = new Gson();

    public abstract String getChannel();

    public String toJson() {
        return gson.toJson(this);
    }

    public static <T extends Packet> T fromJson(String json, Class<T> clazz) {
        return gson.fromJson(json, clazz);
    }

    public abstract void handle();
}
