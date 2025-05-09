package com.br.gabrielmartins.syntri.modulo.loader;

import com.br.gabrielmartins.syntri.SyntriPlugin;
import com.br.gabrielmartins.syntri.modulo.ModuleLoader;
import com.br.gabrielmartins.syntri.modulo.handler.ModuleHandler;

import org.reflections.Reflections;

import java.lang.reflect.Constructor;
import java.util.Set;

public class ModuleAutoLoader {

    public static void loadAll(ModuleLoader loader, SyntriPlugin plugin) {
        Reflections reflections = new Reflections("com.br.gabrielmartins.syntri.modules");
        Set<Class<? extends ModuleHandler>> modules = reflections.getSubTypesOf(ModuleHandler.class);

        for (Class<? extends ModuleHandler> clazz : modules) {
            try {
                Constructor<? extends ModuleHandler> constructor = clazz.getConstructor(ModuleLoader.class, SyntriPlugin.class);
                ModuleHandler instance = constructor.newInstance(loader, plugin);
                instance.register();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
