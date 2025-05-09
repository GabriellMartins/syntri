//package com.br.gabrielmartins.engine.api.translate;
//
//import org.bukkit.configuration.file.YamlConfiguration;
//
//import java.io.*;
//import java.net.URL;
//import java.nio.charset.StandardCharsets;
//import java.util.HashMap;
//import java.util.Map;
//
//public class Translate {
//
//    private static final Map<String, YamlConfiguration> translations = new HashMap<>();
//    private static String currentLanguage = "br";
//    private static final String[] SUPPORTED_LANGUAGES = {
//
//            "br",
//            "en_us",
//            "en_uk",
//            "ja",
//            "zh",
//            "hu"
//
//    };
//    private static final File LANGS_FOLDER = new File("plugins/Syntri/langs");
//
//    public static void load() {
//        if (!LANGS_FOLDER.exists()) LANGS_FOLDER.mkdirs();
//
//        for (String lang : SUPPORTED_LANGUAGES) {
//            File langFile = new File(LANGS_FOLDER, lang + ".yml");
//
//            if (!langFile.exists()) copyDefaultLangFile(langFile, lang);
//
//            YamlConfiguration config = loadYamlSafely(langFile);
//            if (config != null) {
//                translations.put(lang.toLowerCase(), config);
//            }
//        }
//    }
//
//    private static void copyDefaultLangFile(File file, String lang) {
//        try (InputStream input = Translate.class.getClassLoader().getResourceAsStream("langs/" + lang + ".yml")) {
//            if (input == null) return;
//
//            try (OutputStream output = new FileOutputStream(file)) {
//                byte[] buffer = new byte[1024];
//                int bytesRead;
//                while ((bytesRead = input.read(buffer)) != -1) {
//                    output.write(buffer, 0, bytesRead);
//                }
//            }
//        } catch (IOException ignored) {}
//    }
//
//    private static YamlConfiguration loadYamlSafely(File file) {
//        try {
//            URL url = file.toURI().toURL();
//            try (InputStream input = url.openStream();
//                 InputStreamReader reader = new InputStreamReader(input, StandardCharsets.UTF_8)) {
//                return YamlConfiguration.loadConfiguration(reader);
//            }
//        } catch (IOException ignored) {}
//        return null;
//    }
//
//    public static String get(String key) {
//        return get(key, currentLanguage);
//    }
//
//    public static String get(String key, String lang) {
//        YamlConfiguration config = translations.get(lang.toLowerCase());
//        if (config == null) config = translations.get("br");
//        return config != null ? config.getString(key, key) : key;
//    }
//
//    public static void setLanguage(String lang) {
//        currentLanguage = lang;
//    }
//}
