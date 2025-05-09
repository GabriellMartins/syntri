package com.br.gabrielmartins.version3.impl.packet

import net.minecraft.server.v1_8_R3.Packet
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer
import org.bukkit.entity.Player

object PacketUtils {
    fun sendPacket(player: Player, packet: Packet<*>) {
        (player as CraftPlayer).handle.playerConnection.sendPacket(packet)
    }
}
