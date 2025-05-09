package com.br.gabrielmartins.engine.api.tablist.adapter

import org.bukkit.entity.Player

interface TablistAdapter {
    fun sendTablist(player: Player, header: String, footer: String)
}
