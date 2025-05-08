package com.br.gabrielmartins.syntri.listener.manager;

import com.br.gabrielmartins.syntri.SyntriPlugin;
import org.bukkit.Bukkit;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;

import java.lang.reflect.Method;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

public class ListenerRegistry {

    private static final Map<String, Listener> registered = new LinkedHashMap<>();

    public static void autoRegister(Class<? extends Listener> clazz) {
        try {
            Listener instance = clazz.getDeclaredConstructor().newInstance();
            for (Method method : clazz.getDeclaredMethods()) {
                if (method.isAnnotationPresent(EventHandler.class)) {
                    Class<?>[] params = method.getParameterTypes();
                    if (params.length == 1 && Event.class.isAssignableFrom(params[0])) {
                        String eventName = params[0].getSimpleName();
                        if (!registered.containsKey(eventName)) {
                            registered.put(eventName, instance);
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Set<String> getAll() {
        return registered.keySet();
    }

    public static boolean isActive(String name) {
        Listener listener = registered.get(name);
        if (listener == null) return false;

        return HandlerList.getRegisteredListeners(SyntriPlugin.getInstance()).stream()
                .filter(reg -> reg.getListener().getClass().equals(listener.getClass()))
                .map(reg -> reg.getListener())
                .anyMatch(l -> true); // já registrado = ativo
    }

    public static void activate(String name) {
        Listener listener = registered.get(name);
        if (listener != null) {
            Bukkit.getPluginManager().registerEvents(listener, SyntriPlugin.getInstance());
        }
    }

    public static void deactivate(String name) {
        Listener listener = registered.get(name);
        if (listener != null) {
            HandlerList.unregisterAll(listener);
        }
    }

    public static Listener get(String name) {
        return registered.get(name);
    }
}
