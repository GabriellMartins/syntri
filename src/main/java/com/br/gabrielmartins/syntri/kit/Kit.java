package com.br.gabrielmartins.syntri.kit;

import org.bukkit.inventory.ItemStack;
import java.util.List;

public class Kit {
    private final String name;
    private final String permission;
    private final List<ItemStack> contents;
    private final List<ItemStack> armor;
    private final long cooldownMillis;

    public Kit(String name, String permission, List<ItemStack> contents, List<ItemStack> armor, long cooldownMillis) {
        this.name = name;
        this.permission = permission;
        this.contents = contents;
        this.armor = armor;
        this.cooldownMillis = cooldownMillis;
    }

    public String getName() {
        return name;
    }

    public String getPermission() {
        return permission;
    }

    public List<ItemStack> getContents() {
        return contents;
    }

    public List<ItemStack> getArmor() {
        return armor;
    }

    public long getCooldownMillis() {
        return cooldownMillis;
    }

    public ItemStack[] getItems() {
        return contents.toArray(new ItemStack[0]);
    }
}
