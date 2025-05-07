package com.br.gabrielmartins.syntri.api.scoreboard;

import org.bukkit.entity.Player;

public interface ScoreboardWrapper {

    void create(Player player, String title);
    void setLine(Player player, int line, String text);
    void remove(Player player);
}
