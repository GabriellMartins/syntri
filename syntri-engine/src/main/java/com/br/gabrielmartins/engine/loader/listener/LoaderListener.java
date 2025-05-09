package com.br.gabrielmartins.engine.loader.listener;

import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.reflections.Reflections;

import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;
import java.util.Set;

public class LoaderListener {

    private final Object pluginInstance;
    private final Object inventoryLoader;


    public LoaderListener(Object pluginInstance, Object inventoryLoader) {
        this.pluginInstance = pluginInstance;
        this.inventoryLoader = inventoryLoader;
    }

    public LoaderListener listener(String basePackage) {
        Reflections reflections = new Reflections(basePackage);
        Set<Class<? extends Listener>> listenerClasses = reflections.getSubTypesOf(Listener.class);

        for (Class<? extends Listener> clazz : listenerClasses) {
            if (Modifier.isAbstract(clazz.getModifiers())) continue;

            try {
                Listener listenerInstance = null;
                Constructor<?>[] constructors = clazz.getDeclaredConstructors();

                for (Constructor<?> constructor : constructors) {
                    if (constructor.getParameterCount() == 1 &&
                            constructor.getParameterTypes()[0].isInstance(inventoryLoader)) {
                        listenerInstance = (Listener) constructor.newInstance(inventoryLoader);
                        break;
                    }
                }

                if (listenerInstance == null) {
                    Constructor<? extends Listener> noArgsConstructor = clazz.getDeclaredConstructor();
                    listenerInstance = noArgsConstructor.newInstance();
                }

                Bukkit.getPluginManager().registerEvents(listenerInstance, (org.bukkit.plugin.Plugin) pluginInstance);

            } catch (Exception ignored) {}
        }

        return this;
    }
}
