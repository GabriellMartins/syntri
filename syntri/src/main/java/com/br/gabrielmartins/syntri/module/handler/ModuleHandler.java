package com.br.gabrielmartins.syntri.module.handler;

import com.br.gabrielmartins.syntri.module.ModuleLoader;
import com.br.gabrielmartins.syntri.SyntriPlugin;

public abstract class ModuleHandler {

    protected final ModuleLoader loader;
    protected final SyntriPlugin plugin;

    public ModuleHandler(ModuleLoader loader, SyntriPlugin plugin) {
        this.loader = loader;
        this.plugin = plugin;
    }

    public abstract void register();
}
