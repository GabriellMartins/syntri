package com.br.gabrielmartins.syntri.tablist.adapter

import org.bukkit.entity.Player

class ModernTablistAdapter : TablistAdapter {
    override fun sendTablist(player: Player, header: String, footer: String) {
        player.setPlayerListHeaderFooter(header, footer)
    }
}
