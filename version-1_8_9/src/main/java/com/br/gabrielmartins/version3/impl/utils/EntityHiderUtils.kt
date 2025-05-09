package com.br.gabrielmartins.version3.impl.utils

import net.minecraft.server.v1_8_R3.PacketPlayOutEntityDestroy
import org.bukkit.Bukkit
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer
import org.bukkit.entity.Entity
import org.bukkit.entity.Player

object EntityHiderUtils {
    fun hideEntityFromAll(entity: Entity) {
        val packet = PacketPlayOutEntityDestroy(entity.entityId)
        Bukkit.getOnlinePlayers().forEach {
            (it as CraftPlayer).handle.playerConnection.sendPacket(packet)
        }
    }

    fun hideEntityFrom(entity: Entity, player: Player) {
        val packet = PacketPlayOutEntityDestroy(entity.entityId)
        (player as CraftPlayer).handle.playerConnection.sendPacket(packet)
    }
}
