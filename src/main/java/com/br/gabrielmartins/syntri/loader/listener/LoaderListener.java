package com.br.gabrielmartins.syntri.loader.listener;

import com.br.gabrielmartins.syntri.SyntriPlugin;
import com.br.gabrielmartins.syntri.api.inventory.loader.InventoryLoader;
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
            return this;
        }

        for (Class<? extends Listener> clazz : listenerClasses) {
            if (Modifier.isAbstract(clazz.getModifiers())) continue;

            try {
                Listener listenerInstance = null;
                Constructor<?>[] constructors = clazz.getDeclaredConstructors();

                for (Constructor<?> constructor : constructors) {
                    if (constructor.getParameterCount() == 1 &&
                            constructor.getParameterTypes()[0] == InventoryLoader.class) {

                        listenerInstance = (Listener) constructor.newInstance(plugin.getInventoryLoader());
                        break;
                    }
                }

                if (listenerInstance == null) {
                    Constructor<? extends Listener> noArgsConstructor = clazz.getDeclaredConstructor();
                    listenerInstance = noArgsConstructor.newInstance();
                }

                Bukkit.getPluginManager().registerEvents(listenerInstance, plugin);
                plugin.getLogger().info("Listener carregado: " + clazz.getSimpleName());

            } catch (Exception e) {
                plugin.getLogger().warning("Erro ao carregar listener: " + clazz.getName());
                e.printStackTrace();
            }
        }

        return this;
    }
}
