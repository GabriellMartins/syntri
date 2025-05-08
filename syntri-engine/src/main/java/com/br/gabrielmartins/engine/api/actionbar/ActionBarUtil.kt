package com.br.gabrielmartins.commons.api.actionbar

import org.bukkit.Bukkit
import org.bukkit.entity.Player

object ActionBarUtil {

    fun send(player: Player, message: String) {
        try {
            val chatComponentText = getNMSClass("ChatComponentText")
            val iChatBaseComponent = getNMSClass("IChatBaseComponent")
            val packetPlayOutChat = getNMSClass("PacketPlayOutChat")

            val chatConstructor = chatComponentText.getConstructor(String::class.java)
            val chatComponent = chatConstructor.newInstance(message)

            val packet = try {
                packetPlayOutChat.getConstructor(iChatBaseComponent, Byte::class.java)
                    .newInstance(chatComponent, 2.toByte())
            } catch (e: NoSuchMethodException) {
                val chatMessageType = getNMSClass("ChatMessageType")
                val gameInfo = chatMessageType.enumConstants[2]
                packetPlayOutChat.getConstructor(iChatBaseComponent, chatMessageType)
                    .newInstance(chatComponent, gameInfo)
            }

            val handle = player.javaClass.getMethod("getHandle").invoke(player)
            val connection = handle.javaClass.getField("playerConnection").get(handle)
            val sendPacket = connection.javaClass.getMethod("sendPacket", getNMSClass("Packet"))
            sendPacket.invoke(connection, packet)

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun getNMSClass(name: String): Class<*> {
        val version = Bukkit.getServer().javaClass.packageName.split(".")[3]
        return Class.forName("net.minecraft.server.\$version.\$name")
    }
}