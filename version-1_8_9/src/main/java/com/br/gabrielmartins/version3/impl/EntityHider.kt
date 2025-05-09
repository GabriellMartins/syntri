package com.br.gabrielmartins.version3.impl

import net.minecraft.server.v1_8_R3.PacketPlayOutEntityDestroy
import net.minecraft.server.v1_8_R3.PacketPlayOutSpawnEntityLiving
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer


import org.bukkit.entity.Entity
import org.bukkit.entity.Player

object EntityHider {

    fun hideEntity(player: Player, entity: Entity) {
        val connection = (player as CraftPlayer).handle.playerConnection
        connection.sendPacket(PacketPlayOutEntityDestroy(entity.entityId))
    }

    fun showEntity(player: Player, entity: Entity) {
        val connection = (player as CraftPlayer).handle.playerConnection
        val packet = PacketPlayOutSpawnEntityLiving((entity as org.bukkit.craftbukkit.v1_8_R3.entity.CraftLivingEntity).handle)
        connection.sendPacket(packet)
    }

    fun hideAllEntities(player: Player) {
        for (entity in player.world.entities) {
            if (entity != player) hideEntity(player, entity)
        }
    }
}
