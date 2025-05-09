package com.br.syntri.version.impl

import com.br.syntri.version.VersionSupport
import org.bukkit.Bukkit
import org.bukkit.entity.Player

class V1_8_9 : VersionSupport {

    override fun sendActionBar(player: Player, message: String) {
        try {
            val version = Bukkit.getServer().javaClass.packageName.split(".")[3]
            val chatComponentText = Class.forName("net.minecraft.server.\$version.ChatComponentText")
            val iChatBaseComponent = Class.forName("net.minecraft.server.\$version.IChatBaseComponent")
            val component = chatComponentText.getConstructor(String::class.java).newInstance(message)

            val packetPlayOutChat = Class.forName("net.minecraft.server.\$version.PacketPlayOutChat")
            val constructor = packetPlayOutChat.getConstructor(iChatBaseComponent, Byte::class.java)
            val packet = constructor.newInstance(component, 2.toByte())

            val handle = player.javaClass.getMethod("getHandle").invoke(player)
            val connection = handle.javaClass.getField("playerConnection").get(handle)
            val sendPacket = connection.javaClass.getMethod("sendPacket",
                Class.forName("net.minecraft.server.\$version.Packet")
            )
            sendPacket.invoke(connection, packet)
        } catch (e: Exception) {
            player.sendMessage("§c[Erro] ActionBar não suportado nesta versão.")
            e.printStackTrace()
        }
    }
}
