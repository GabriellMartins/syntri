package com.br.gabrielmartins.syntri;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class SyntriPlaceholder extends PlaceholderExpansion {

    @Override
    public @NotNull String getIdentifier() {
        return "syntri";
    }

    @Override
    public @NotNull String getAuthor() {
        return "GabrielMartins";
    }

    @Override
    public @NotNull String getVersion() {
        return SyntriPlugin.getInstance().getDescription().getVersion();
    }

    @Override
    public boolean canRegister() {
        return true;
    }

    @Override
    public boolean persist() {
        return true;
    }

    @Override
    public String onPlaceholderRequest(Player player, @NotNull String identifier) {
        if (player == null) return "";

        switch (identifier.toLowerCase()) {

            case "name":
                return player.getName();

            case "uuid":
                return player.getUniqueId().toString();

            case "health":
                return String.valueOf((int) player.getHealth());

            case "world":
                return player.getWorld().getName();

            case "gamemode":
                GameMode gm = player.getGameMode();
                return gm != null ? gm.name() : "UNKNOWN";

            case "backend":
                return SyntriPlugin.getInstance().getBackend() != null
                        ? SyntriPlugin.getInstance().getBackend().getClass().getSimpleName().replace("Backend", "")
                        : "Desconhecido";

            case "online":
                return String.valueOf(Bukkit.getOnlinePlayers().size());

            case "ping":
                try {
                    Object handle = player.getClass().getMethod("getHandle").invoke(player);
                    return String.valueOf(handle.getClass().getField("ping").getInt(handle));
                } catch (Exception e) {
                    return "-1";
                }

            default:
                return null;
        }
    }
}
