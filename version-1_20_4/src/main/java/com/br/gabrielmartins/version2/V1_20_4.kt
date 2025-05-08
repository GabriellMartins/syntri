package com.br.syntri.version.impl

import com.br.syntri.version.VersionSupport
import net.md_5.bungee.api.ChatMessageType
import net.md_5.bungee.api.chat.TextComponent
import org.bukkit.entity.Player

class V1_20_4 : VersionSupport {
    override fun sendActionBar(player: Player, message: String) {
        player.spigot().sendMessage(
            ChatMessageType.ACTION_BAR,
            arrayOf(TextComponent(message))
        )
    }
}
