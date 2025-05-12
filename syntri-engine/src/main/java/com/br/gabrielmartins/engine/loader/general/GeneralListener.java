package com.br.gabrielmartins.engine.loader.general;

import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.reflections.Reflections;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.util.ConfigurationBuilder;

import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;
import java.util.Set;
import java.util.logging.Logger;

public class GeneralListener {

    private final Plugin plugin;
    private final Object inventoryLoader;
    private final Logger logger;

    public GeneralListener(Object pluginInstance, Object inventoryLoader) {
        this.plugin = (Plugin) pluginInstance;
        this.inventoryLoader = inventoryLoader;
        this.logger = plugin.getLogger();
    }

    public GeneralListener listener(String basePackage) {
        logger.info("[Syntri] 🔍 Procurando Listeners no pacote: " + basePackage);

        Reflections reflections = new Reflections(
                new ConfigurationBuilder()
                        .forPackages(basePackage)
                        .addScanners(new SubTypesScanner(false)) // inclui classes sem anotação
        );

        Set<Class<? extends Listener>> listenerClasses = reflections.getSubTypesOf(Listener.class);

        logger.info("[Syntri] 📦 Total de Listeners encontrados: " + listenerClasses.size());

        for (Class<? extends Listener> clazz : listenerClasses) {
            if (Modifier.isAbstract(clazz.getModifiers())) {
                logger.info("[Syntri] ⏭️ Ignorando classe abstrata: " + clazz.getName());
                continue;
            }

            try {
                Listener listenerInstance = null;

                // 1. Tenta com inventoryLoader
                for (Constructor<?> constructor : clazz.getDeclaredConstructors()) {
                    if (constructor.getParameterCount() == 1 &&
                            constructor.getParameterTypes()[0].isInstance(inventoryLoader)) {
                        listenerInstance = (Listener) constructor.newInstance(inventoryLoader);
                        logger.info("[Syntri] ✅ Registrado (com loader): " + clazz.getSimpleName());
                        break;
                    }
                }

                // 2. Tenta com Plugin
                if (listenerInstance == null) {
                    for (Constructor<?> constructor : clazz.getDeclaredConstructors()) {
                        if (constructor.getParameterCount() == 1 &&
                                constructor.getParameterTypes()[0].isInstance(plugin)) {
                            listenerInstance = (Listener) constructor.newInstance(plugin);
                            logger.info("[Syntri] ✅ Registrado (com plugin): " + clazz.getSimpleName());
                            break;
                        }
                    }
                }

                // 3. Tenta com construtor vazio
                if (listenerInstance == null) {
                    Constructor<? extends Listener> noArgsConstructor = clazz.getDeclaredConstructor();
                    listenerInstance = noArgsConstructor.newInstance();
                    logger.info("[Syntri] ✅ Registrado (construtor vazio): " + clazz.getSimpleName());
                }

                Bukkit.getPluginManager().registerEvents(listenerInstance, plugin);

            } catch (Exception e) {
                logger.warning("[Syntri] ❌ Falha ao registrar listener: " + clazz.getName() + " -> " + e.getClass().getSimpleName() + ": " + e.getMessage());
                e.printStackTrace();
            }
        }

        return this;
    }
}
