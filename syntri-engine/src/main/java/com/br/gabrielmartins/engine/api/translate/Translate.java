package com.br.gabrielmartins.engine.api.translate;

import org.bukkit.configuration.file.YamlConfiguration;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class Translate {

    private static final Map<String, YamlConfiguration> translations = new HashMap<>();
    private static String currentLanguage = "br";

    public static void load() {
        String[] languages = {"br", "us", "uk", "japao", "china", "hungria"};

        File langsFolder = new File("plugins/Syntri/langs");
        if (!langsFolder.exists()) {
            langsFolder.mkdirs();
        }

        for (String lang : languages) {
            File langFile = new File(langsFolder, lang + ".yml");

            if (!langFile.exists()) {
                createDefaultTranslationFile(langFile, lang);
            }

            try (Reader reader = new InputStreamReader(langFile.toURI().toURL().openStream(), StandardCharsets.UTF_8)) {
                YamlConfiguration config = YamlConfiguration.loadConfiguration(reader);
                translations.put(lang.toLowerCase(), config);
                System.out.println("[Syntri] Tradução carregada: " + lang);
            } catch (Exception e) {
                System.err.println("[Syntri] Erro ao carregar tradução: " + lang);
                e.printStackTrace();
            }
        }
    }

    private static void createDefaultTranslationFile(File file, String lang) {
        try (InputStream inputStream = Translate.class.getClassLoader().getResourceAsStream("langs/" + lang + ".yml")) {
            if (inputStream == null) {
                System.err.println("[Syntri] Arquivo de tradução não encontrado no JAR para: " + lang);
                return;
            }

            try (OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream(file), StandardCharsets.UTF_8)) {
                int read;
                while ((read = inputStream.read()) != -1) {
                    writer.write(read);
                }
            }

            System.out.println("[Syntri] Arquivo de tradução copiado para o servidor: " + lang);
        } catch (Exception e) {
            System.err.println("[Syntri] Erro ao copiar arquivo de tradução para: " + lang);
            e.printStackTrace();
        }
    }


    public static String get(String key) {
        return get(key, currentLanguage);
    }

    public static String get(String key, String lang) {
        YamlConfiguration config = translations.getOrDefault(lang.toLowerCase(), null);
        if (config == null) return key;

        return config.getString(key, key);
    }

    public static void setLanguage(String lang) {
        currentLanguage = lang;
    }
}
