package com.br.gabrielmartins.commons

import com.br.gabrielmartins.version2.V1_20_4
import com.br.syntri.version.VersionSupport
import com.br.syntri.version.impl.V1_8_9
import org.bukkit.Bukkit

object VersionLoader {
    fun load(): VersionSupport {
        val version = Bukkit.getServer().bukkitVersion

        return when {
            version.startsWith("1.8") -> V1_8_9()
            version.startsWith("1.20.4") -> V1_20_4()
            else -> throw UnsupportedOperationException("Versão não suportada: $version")
        }
    }
}
