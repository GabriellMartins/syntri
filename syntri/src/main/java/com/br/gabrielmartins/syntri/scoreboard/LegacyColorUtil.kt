package com.br.gabrielmartins.syntri.scoreboard

import org.bukkit.Bukkit
import org.bukkit.ChatColor

object LegacyColorUtil {

    private val hexPattern = Regex("#[a-fA-F0-9]{6}")
    private val supportsRGB = isVersionAtLeast(16)

    fun translate(text: String): String {
        return if (supportsRGB) {
            applyHexColors(ChatColor.translateAlternateColorCodes('&', text))
        } else {
            ChatColor.translateAlternateColorCodes('&', text)
        }
    }

    private fun applyHexColors(text: String): String {
        var result = text
        hexPattern.findAll(text).forEach {
            val hex = it.value
            val rgbCode = toLegacyHex(hex)
            result = result.replace(hex, rgbCode)
        }
        return result
    }

    private fun toLegacyHex(hex: String): String {
        val cleaned = hex.replace("#", "")
        if (cleaned.length != 6) return hex
        return "§x" + cleaned.map { "§$it" }.joinToString("")
    }

    private fun isVersionAtLeast(target: Int): Boolean {
        val version = Bukkit.getBukkitVersion().split("-")[0]
        val major = version.split(".")[1].toIntOrNull() ?: return false
        return major >= target
    }
}
