package com.br.gabrielmartins.version3.impl.monitor

import net.minecraft.server.v1_8_R3.MinecraftServer
import kotlin.collections.get

object TpsMonitor {
    fun getTps(): Double {
        return MinecraftServer.getServer().recentTps[0].coerceAtMost(20.0)
    }
}
