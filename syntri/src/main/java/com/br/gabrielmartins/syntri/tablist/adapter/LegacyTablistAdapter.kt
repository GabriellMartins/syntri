package com.br.gabrielmartins.syntri.tablist.adapter

import org.bukkit.Bukkit
import org.bukkit.entity.Player

class LegacyTablistAdapter : TablistAdapter {

    override fun sendTablist(player: Player, header: String, footer: String) {
        try {
            val craftPlayerClass = Class.forName("org.bukkit.craftbukkit.${getVersion()}.entity.CraftPlayer")
            val entityPlayer = craftPlayerClass.getMethod("getHandle").invoke(player)

            val chatSerializerClass = Class.forName("net.minecraft.server.${getVersion()}.IChatBaseComponent\$ChatSerializer")
            val chatBaseComponentClass = Class.forName("net.minecraft.server.${getVersion()}.IChatBaseComponent")

            val headerComponent = chatSerializerClass.getMethod("a", String::class.java)
                .invoke(null, "{\"text\":\"$header\"}")
            val footerComponent = chatSerializerClass.getMethod("a", String::class.java)
                .invoke(null, "{\"text\":\"$footer\"}")

            val packetClass = Class.forName("net.minecraft.server.${getVersion()}.PacketPlayOutPlayerListHeaderFooter")
            val packet = packetClass.getConstructor(chatBaseComponentClass).newInstance(headerComponent)

            val field = packetClass.getDeclaredField("b")
            field.isAccessible = true
            field.set(packet, footerComponent)

            val playerConnection = entityPlayer.javaClass.getField("playerConnection").get(entityPlayer)
            val sendPacketMethod = playerConnection.javaClass.getMethod("sendPacket", Class.forName("net.minecraft.server.${getVersion()}.Packet"))
            sendPacketMethod.invoke(playerConnection, packet)

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun getVersion(): String {
        return Bukkit.getServer().javaClass.getPackage().name.split(".")[3]
    }
}
