package com.br.gabrielmartins.engine.api.scoreboard

import com.br.gabrielmartins.engine.api.scoreboard.enum.TitleAnimation

import me.clip.placeholderapi.PlaceholderAPI
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.configuration.file.FileConfiguration
import org.bukkit.entity.Player
import org.bukkit.plugin.Plugin
import org.bukkit.scoreboard.DisplaySlot
import org.bukkit.scoreboard.Scoreboard
import java.util.*

class ScoreboardManager(
    private val config: FileConfiguration,
    private val plugin: Plugin
) {
    private val boards = mutableMapOf<UUID, Scoreboard>()
    private val gradientRegex = Regex("<gradient:(#[A-Fa-f0-9]{6}):(#[A-Fa-f0-9]{6})>(.*?)</gradient>")
    private val supportsRGB = isVersionAtLeast(16)
    private var animationIndex = 0
    private var animationTaskId: Int? = null

    fun create(player: Player, title: String) {
        val board = Bukkit.getScoreboardManager()?.newScoreboard ?: return
        val obj = board.registerNewObjective("syntri", "dummy")
        obj.displaySlot = DisplaySlot.SIDEBAR
        obj.displayName = formatText(title, player)
        boards[player.uniqueId] = board
        player.scoreboard = board
    }

    fun setLine(player: Player, line: Int, raw: String) {
        val board = boards[player.uniqueId] ?: return
        val obj = board.getObjective("syntri") ?: return

        val formatted = formatText(raw, player)
        val entry = if (formatted.length > 40) formatted.substring(0, 40) else formatted

        board.entries.forEach {
            if (obj.getScore(it).score == line && it != entry) {
                board.resetScores(it)
            }
        }

        obj.getScore(entry).score = line
    }

    fun remove(player: Player) {
        player.scoreboard = Bukkit.getScoreboardManager()?.mainScoreboard ?: return
        boards.remove(player.uniqueId)
    }

    fun apply(player: Player) {
        if (!config.getBoolean("scoreboard.enabled", true)) return

        val titleRaw = config.getString("scoreboard.title", "&aSyntri") ?: "&aSyntri"
        val lines = config.getStringList("scoreboard.lines")

        if (!boards.containsKey(player.uniqueId)) {
            create(player, titleRaw)
        }

        val obj = boards[player.uniqueId]?.getObjective("syntri") ?: return
        obj.displayName = formatText(titleRaw, player)

        var line = lines.size
        for (raw in lines) {
            setLine(player, line--, raw)
        }
    }

    fun startTitleAnimation(players: Collection<Player>) {
        if (!config.getBoolean("scoreboard.animation.enabled", false) || !supportsRGB) return

        val animationType = TitleAnimation.fromName(config.getString("scoreboard.animation.style"))
        val interval = config.getLong("scoreboard.animation.interval_ticks", 10L)

        animationTaskId?.let { Bukkit.getScheduler().cancelTask(it) }

        animationTaskId = Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, {
            val text = config.getString("scoreboard.title", "Syntri") ?: "Syntri"
            val animated = animationType.generateNext(text, animationIndex++)

            players.forEach { player ->
                val board = boards[player.uniqueId] ?: return@forEach
                board.getObjective("syntri")?.displayName = animated
            }
        }, 0L, interval)
    }

    private fun formatText(text: String, player: Player): String {
        var parsed = text
        parsed = ChatColor.translateAlternateColorCodes('&', parsed)
        parsed = parsed.replace('$', '§')
        if (Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")) {
            parsed = PlaceholderAPI.setPlaceholders(player, parsed)
        }
        if (supportsRGB) {
            parsed = applyGradientTags(parsed)
        }
        return parsed
    }

    private fun applyGradientTags(text: String): String {
        var result = text
        gradientRegex.findAll(text).forEach {
            val (start, end, content) = it.destructured
            val gradient = GradientUtil.generateGradient(content, start, end)
            result = result.replace(it.value, gradient)
        }
        return result
    }

    private fun isVersionAtLeast(target: Int): Boolean {
        val version = Bukkit.getBukkitVersion().split("-")[0]
        val major = version.split(".")[1].toIntOrNull() ?: return false
        return major >= target
    }
}
