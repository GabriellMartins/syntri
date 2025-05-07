package com.br.gabrielmartins.syntri.loader.listener;

import com.br.gabrielmartins.syntri.SyntriPlugin;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.reflections.Reflections;

import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;
import java.util.Set;

public class LoaderListener {

    public LoaderListener listener(String basePackage) {
        SyntriPlugin plugin = SyntriPlugin.getInstance();

        plugin.getLogger().info("Escaneando pacote: " + basePackage);

        Reflections reflections = new Reflections(basePackage);
        Set<Class<? extends Listener>> listenerClasses = reflections.getSubTypesOf(Listener.class);

        if (listenerClasses.isEmpty()) {
            plugin.getLogger().warning("Nenhum Listener encontrado no pacote: " + basePackage);
        }

        for (Class<? extends Listener> clazz : listenerClasses) {
            if (Modifier.isAbstract(clazz.getModifiers())) continue;

            try {
                Listener listener;

                Constructor<?> constructorWithPlugin = null;

                for (Constructor<?> constructor : clazz.getDeclaredConstructors()) {
                    Class<?>[] parameters = constructor.getParameterTypes();
                    if (parameters.length == 1 && parameters[0] == SyntriPlugin.class) {
                        constructorWithPlugin = constructor;
                        break;
                    }
                }

                if (constructorWithPlugin != null) {
                    listener = (Listener) constructorWithPlugin.newInstance(plugin);
                } else {
                    listener = clazz.getDeclaredConstructor().newInstance();
                }

                Bukkit.getPluginManager().registerEvents(listener, plugin);
                plugin.getLogger().info("Listener registrado: " + clazz.getSimpleName());

            } catch (Exception e) {
                plugin.getLogger().warning("Erro ao registrar listener: " + clazz.getName());
                e.printStackTrace();
            }
        }

        return this;
    }
}
