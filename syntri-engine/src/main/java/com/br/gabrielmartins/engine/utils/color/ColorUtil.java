package com.br.gabrielmartins.engine.utils.color;

import org.bukkit.ChatColor;

public class ColorUtil {

    /**
     * Traduz códigos de cor de '&' para '§' (ex: &a -> §a).
     *
     * @param text Texto com cores usando '&'
     * @return Texto com cores traduzidas para '§'
     */
    public static String color(String text) {
        if (text == null) return "";
        return ChatColor.translateAlternateColorCodes('&', text);
    }

    /**
     * Traduz uma lista de mensagens.
     *
     * @param lines Lista de linhas com '&'
     * @return Lista de linhas com '§'
     */
    public static java.util.List<String> color(java.util.List<String> lines) {
        java.util.List<String> colored = new java.util.ArrayList<>();
        for (String line : lines) {
            colored.add(color(line));
        }
        return colored;
    }
}
