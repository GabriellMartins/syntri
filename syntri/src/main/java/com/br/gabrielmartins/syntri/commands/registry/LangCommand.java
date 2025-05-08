package com.br.gabrielmartins.syntri.commands.registry;

import com.br.gabrielmartins.engine.api.translate.Translate;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class LangCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("§cApenas jogadores podem usar este comando.");
            return true;
        }

        Player player = (Player) sender;

        if (!player.hasPermission("syntri.lang")) {
            player.sendMessage("§cVocê não tem permissão para usar este comando.");
            return true;
        }

        if (args.length == 0) {
            player.sendMessage("§cPor favor, forneça um código de idioma (br, us, uk, china, japao, hungria).");
            return false;
        }

        String lang = args[0].toLowerCase();

        if (!isValidLanguage(lang)) {
            player.sendMessage("§cIdioma inválido. Use: br, us, uk, china, japao, hungria.");
            return true;
        }

        Translate.setLanguage(lang);
        player.sendMessage("§aIdioma alterado para: " + lang);

        return true;
    }

    private boolean isValidLanguage(String lang) {
        switch (lang) {
            case "br":
            case "us":
            case "uk":
            case "china":
            case "japao":
            case "hungria":
                return true;
            default:
                return false;
        }
    }
}
