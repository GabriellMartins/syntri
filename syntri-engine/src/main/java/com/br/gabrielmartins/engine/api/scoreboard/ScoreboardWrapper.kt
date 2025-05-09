package com.br.gabrielmartins.engine.api.scoreboard

import org.bukkit.entity.Player

interface ScoreboardWrapper {
    fun create(player: Player, title: String)
    fun setLine(player: Player, line: Int, text: String)
    fun remove(player: Player)
}
