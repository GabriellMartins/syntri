package com.br.gabrielmartins.syntri.api.scoreboard;

import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.*;

import java.lang.reflect.Field;
import java.util.*;

public class ScoreboardManager {

    private final Map<UUID, Scoreboard> boards = new HashMap<>();
    private final FileConfiguration config;

    public ScoreboardManager(FileConfiguration config) {
        this.config = config;
    }

    public void create(Player player, String title) {
        Scoreboard board = Bukkit.getScoreboardManager().getNewScoreboard();
        Objective obj = board.registerNewObjective("syntri", "dummy");
        obj.setDisplaySlot(DisplaySlot.SIDEBAR);
        obj.setDisplayName(color(title));
        boards.put(player.getUniqueId(), board);
        player.setScoreboard(board);
    }

    public void setLine(Player player, int line, String text) {
        Scoreboard board = boards.get(player.getUniqueId());
        if (board == null) return;

        Objective obj = board.getObjective("syntri");
        if (obj == null) return;

        String entry = color(text);
        if (entry.length() > 40) entry = entry.substring(0, 40);

        for (String old : board.getEntries()) {
            Score score = obj.getScore(old);
            if (score.getScore() == line && !old.equals(entry)) {
                board.resetScores(old);
                break;
            }
        }

        obj.getScore(entry).setScore(line);
    }

    public void remove(Player player) {
        player.setScoreboard(Bukkit.getScoreboardManager().getMainScoreboard());
        boards.remove(player.getUniqueId());
    }

    public void apply(Player player) {
        String title = config.getString("scoreboard.title", "&aSyntri");
        List<String> lines = config.getStringList("scoreboard.lines");

        create(player, title);

        int line = lines.size();
        for (String raw : lines) {
            setLine(player, line--, parse(raw, player));
        }
    }

    private String parse(String raw, Player p) {
        String parsed = ChatColor.translateAlternateColorCodes('&', raw);

        if (Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")) {
            parsed = PlaceholderAPI.setPlaceholders(p, parsed);
        }

        return parsed;
    }

    private int getPing(Player player) {
        try {
            Object handle = player.getClass().getMethod("getHandle").invoke(player);
            Field field = handle.getClass().getDeclaredField("ping");
            return field.getInt(handle);
        } catch (Exception e) {
            return -1;
        }
    }

    private String color(String s) {
        return ChatColor.translateAlternateColorCodes('&', s);
    }
}
