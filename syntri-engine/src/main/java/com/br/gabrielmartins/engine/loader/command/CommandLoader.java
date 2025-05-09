package com.br.gabrielmartins.engine.loader.command;

import com.br.gabrielmartins.engine.loader.command.info.CommandInfo;

import org.bukkit.Bukkit;
import org.bukkit.command.*;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.reflections.Reflections;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.Map;
import java.util.Set;

public class CommandLoader {

    private final JavaPlugin plugin;

    public CommandLoader(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    public void load(String basePackage) {
        Reflections reflections = new Reflections(basePackage);
        Set<Class<?>> classes = reflections.getTypesAnnotatedWith(CommandInfo.class);

        for (Class<?> clazz : classes) {
            if (!CommandExecutor.class.isAssignableFrom(clazz)) continue;

            try {
                CommandInfo annotation = clazz.getAnnotation(CommandInfo.class);
                CommandExecutor executor = (CommandExecutor) clazz.getDeclaredConstructor().newInstance();

                for (String name : annotation.names()) {
                    PluginCommand command = createPluginCommand(name);
                    command.setExecutor(executor);

                    if (annotation.permission().length > 0) {
                        command.setPermission(annotation.permission()[0]);
                    }

                    registerCommand(command);
                }

            } catch (Exception ignored) {}
        }
    }

    private PluginCommand createPluginCommand(String name) {
        try {
            Constructor<PluginCommand> constructor = PluginCommand.class.getDeclaredConstructor(String.class, Plugin.class);
            constructor.setAccessible(true);
            return constructor.newInstance(name, plugin);
        } catch (Exception e) {
            throw new RuntimeException("Falha ao criar comando: /" + name, e);
        }
    }

    private void registerCommand(PluginCommand command) {
        try {
            CommandMap commandMap = getCommandMap();

            Map<String, Command> knownCommands = getKnownCommands(commandMap);
            if (knownCommands.containsKey(command.getName())) {
                knownCommands.remove(command.getName());
                knownCommands.remove(plugin.getName().toLowerCase() + ":" + command.getName());
            }

            commandMap.register(plugin.getName(), command);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao registrar comando: " + command.getName(), e);
        }
    }

    private CommandMap getCommandMap() {
        try {
            var pm = Bukkit.getPluginManager();
            Field f = pm.getClass().getDeclaredField("commandMap");
            f.setAccessible(true);
            return (CommandMap) f.get(pm);
        } catch (Exception e) {
            throw new RuntimeException("Não foi possível acessar a CommandMap!", e);
        }
    }

    @SuppressWarnings("unchecked")
    private Map<String, Command> getKnownCommands(CommandMap commandMap) throws Exception {
        Field f = SimpleCommandMap.class.getDeclaredField("knownCommands");
        f.setAccessible(true);
        return (Map<String, Command>) f.get(commandMap);
    }
}
