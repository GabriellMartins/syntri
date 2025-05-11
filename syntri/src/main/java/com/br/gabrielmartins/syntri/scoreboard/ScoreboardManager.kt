package com.br.gabrielmartins.syntri.scoreboard

import com.br.gabrielmartins.syntri.scoreboard.enum.TitleAnimation
import me.clip.placeholderapi.PlaceholderAPI
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.configuration.file.FileConfiguration
import org.bukkit.entity.Player
import org.bukkit.plugin.Plugin
import org.bukkit.scoreboard.DisplaySlot
import org.bukkit.scoreboard.Objective
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

    fun apply(player: Player) {
        if (!config.getBoolean("scoreboard.enabled", true)) return

        val manager = Bukkit.getScoreboardManager() ?: return
        val board = manager.newScoreboard
        val objective = board.registerNewObjective("dummy", "dummy")
        objective.displaySlot = DisplaySlot.SIDEBAR

        var rawTitle = config.getString("scoreboard.title", "&bSyntri Network") ?: "&bSyntri Network"

        if (supportsRGB) {
            rawTitle = applyGradientTags(rawTitle)
        } else {
            rawTitle = rawTitle.replace(gradientRegex) { "&b${it.groupValues[3]}" }
        }

        var title = ChatColor.translateAlternateColorCodes('&', rawTitle)
        title = ChatColor.stripColor(title)?.let {
            if (it.length > 32) title.substring(0, title.length - (it.length - 32)) else title
        } ?: title

        objective.displayName = title

        val lines = config.getStringList("scoreboard.lines").reversed()
        lines.forEachIndexed { index, line ->
            val text = formatText(line, player)
            objective.getScore(text.take(40)).score = index + 1
        }

        boards[player.uniqueId] = board
        player.scoreboard = board
    }

    fun updateLines(player: Player) {
        val board = boards[player.uniqueId] ?: return
        val objective = board.getObjective(DisplaySlot.SIDEBAR) ?: return

        board.entries.forEach { board.resetScores(it) }

        val lines = config.getStringList("scoreboard.lines").reversed()
        lines.forEachIndexed { index, line ->
            val text = formatText(line, player)
            objective.getScore(text.take(40)).score = index + 1
        }
    }

    fun remove(player: Player) {
        boards.remove(player.uniqueId)
        player.scoreboard = Bukkit.getScoreboardManager()?.mainScoreboard ?: return
    }

    fun startTitleAnimation(players: Collection<Player>) {
        if (!config.getBoolean("scoreboard.animation.enabled", false) || !supportsRGB) return

        val animationType = TitleAnimation.fromName(config.getString("scoreboard.animation.style"))
        val interval = config.getLong("scoreboard.animation.interval_ticks", 10L)

        animationTaskId?.let { Bukkit.getScheduler().cancelTask(it) }

        animationTaskId = Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, {
            val baseTitle = config.getString("scoreboard.title", "Syntri") ?: "Syntri"
            val animated = animationType.generateNext(baseTitle, animationIndex++)

            players.forEach { player ->
                boards[player.uniqueId]
                    ?.getObjective(DisplaySlot.SIDEBAR)
                    ?.displayName = animated.take(32)
            }
        }, 0L, interval)
    }

    private fun formatText(text: String, player: Player): String {
        var parsed = text

        if (Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")) {
            parsed = PlaceholderAPI.setPlaceholders(player, parsed)
        }

        if (supportsRGB) {
            parsed = applyGradientTags(parsed)
        }

        parsed = ChatColor.translateAlternateColorCodes('&', parsed).replace('$', '§')

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
        return try {
            val version = Bukkit.getBukkitVersion().split("-")[0]
            val major = version.split(".")[1].toIntOrNull() ?: 0
            major >= target
        } catch (ex: Exception) {
            false
        }
    }
}
