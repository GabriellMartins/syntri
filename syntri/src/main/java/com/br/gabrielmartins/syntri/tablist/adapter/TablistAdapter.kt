package com.br.gabrielmartins.syntri.tablist.adapter

import org.bukkit.entity.Player

interface TablistAdapter {
    fun sendTablist(player: Player, header: String, footer: String)
}
