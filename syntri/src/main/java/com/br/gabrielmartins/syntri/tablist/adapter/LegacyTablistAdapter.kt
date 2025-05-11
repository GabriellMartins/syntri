package com.br.gabrielmartins.syntri.tablist.adapter

import org.bukkit.Bukkit
import org.bukkit.entity.Player

class LegacyTablistAdapter : TablistAdapter {

    override fun sendTablist(player: Player, header: String, footer: String) {
        val version = Bukkit.getServer().javaClass.packageName.split(".").last()
        if (version == "v1_7_R4") {
            return
        }

        try {
            val chatComponentClass = Class.forName("net.minecraft.server.$version.IChatBaseComponent")
            val serializerClass = Class.forName("net.minecraft.server.$version.IChatBaseComponent\$ChatSerializer")

            val headerComponent = serializerClass.getMethod("a", String::class.java)
                .invoke(null, "{\"text\":\"$header\"}")
            val footerComponent = serializerClass.getMethod("a", String::class.java)
                .invoke(null, "{\"text\":\"$footer\"}")

            val packetClass = Class.forName("net.minecraft.server.$version.PacketPlayOutPlayerListHeaderFooter")
            val packet = packetClass.getConstructor(chatComponentClass).newInstance(headerComponent)

            val footerField = packetClass.getDeclaredField("b")
            footerField.isAccessible = true
            footerField.set(packet, footerComponent)

            val handle = player.javaClass.getMethod("getHandle").invoke(player)
            val playerConnection = handle.javaClass.getField("playerConnection").get(handle)
            val sendPacketMethod = playerConnection.javaClass.getMethod(
                "sendPacket",
                Class.forName("net.minecraft.server.$version.Packet")
            )
            sendPacketMethod.invoke(playerConnection, packet)
        } catch (e: ClassNotFoundException) {
            if (!version.contains("v1_7")) e.printStackTrace()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
