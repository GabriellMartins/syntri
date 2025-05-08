package com.br.syntri.version

import org.bukkit.entity.Player

interface VersionSupport {
    fun sendActionBar(player: Player, message: String)
}
