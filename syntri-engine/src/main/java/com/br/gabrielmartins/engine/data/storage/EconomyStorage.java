package com.br.gabrielmartins.engine.data.storage;

import java.util.UUID;

public interface EconomyStorage {
    void createAccount(UUID uuid);
    boolean hasAccount(UUID uuid);
    double getBalance(UUID uuid);
    void setBalance(UUID uuid, double amount);
}
