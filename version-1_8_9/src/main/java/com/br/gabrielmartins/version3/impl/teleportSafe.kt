package com.br.gabrielmartins.version3.impl

import org.bukkit.Location
import org.bukkit.entity.Player
import org.bukkit.util.Vector

object PlayerUtils {
    fun teleportSafe(player: Player, location: Location) {
        if (location.world != null) {
            player.teleport(location.add(0.5, 0.0, 0.5))
        }
    }

    fun sendVelocity(player: Player, x: Double, y: Double, z: Double) {
        player.velocity = Vector(x, y, z)
    }

    fun resetTitle(player: Player) {
        player.resetTitle()
    }
}
