package com.br.gabrielmartins.version3.impl.entity

import org.bukkit.Bukkit
import org.bukkit.entity.EntityType

object EntityCleaner {
    private val targetTypes = listOf(EntityType.DROPPED_ITEM, EntityType.ARROW, EntityType.EXPERIENCE_ORB)

    fun cleanAllWorlds() {
        for (world in Bukkit.getWorlds()) {
            world.entities.filter { it.type in targetTypes }.forEach { it.remove() }
        }
    }
}
