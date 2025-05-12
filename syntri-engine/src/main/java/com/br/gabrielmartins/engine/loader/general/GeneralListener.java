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

        Reflections reflections = new Reflections(
                new ConfigurationBuilder()
                        .forPackages(basePackage)
                        .addScanners(new SubTypesScanner(false))
        );

        Set<Class<? extends Listener>> listenerClasses = reflections.getSubTypesOf(Listener.class);

        for (Class<? extends Listener> clazz : listenerClasses) {
            if (Modifier.isAbstract(clazz.getModifiers())) continue;

            try {
                Listener listenerInstance = null;
                Constructor<?>[] constructors = clazz.getDeclaredConstructors();

                for (Constructor<?> constructor : constructors) {
                    Class<?>[] params = constructor.getParameterTypes();

                    if (params.length == 2 &&
                            params[0].getSimpleName().equalsIgnoreCase("ScoreboardManager") &&
                            params[1] == Plugin.class) {
                        listenerInstance = null;
                        break;
                    }

                    if (params.length == 1 &&
                            params[0].isInstance(inventoryLoader)) {
                        listenerInstance = (Listener) constructor.newInstance(inventoryLoader);
                        break;
                    }

                    if (params.length == 1 &&
                            params[0].isInstance(plugin)) {
                        listenerInstance = (Listener) constructor.newInstance(plugin);
                        break;
                    }
                }

                if (listenerInstance == null) {
                    try {
                        Constructor<? extends Listener> noArgsConstructor = clazz.getDeclaredConstructor();
                        listenerInstance = noArgsConstructor.newInstance();
                    } catch (NoSuchMethodException ignored) {
                        continue;
                    }
                }

                if (listenerInstance != null) {
                    Bukkit.getPluginManager().registerEvents(listenerInstance, plugin);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return this;
    }
}
