package com.br.gabrielmartins.syntri.enums;

import org.bukkit.enchantments.Enchantment;

import java.util.HashMap;
import java.util.Map;

public enum EnchantmentName {

    ARROW_DAMAGE("Força"),
    ARROW_FIRE("Chama"),
    ARROW_INFINITE("Infinidade"),
    ARROW_KNOCKBACK("Impacto"),
    BINDING_CURSE("Maldição da ligação"),
    CHANNELING("Condutividade"),
    DAMAGE_ALL("Afiação"),
    DAMAGE_ARTHROPODS("Ruína dos artrópodes"),
    DAMAGE_UNDEAD("Julgamento"),
    DEPTH_STRIDER("Passos profundos"),
    DIG_SPEED("Eficiência"),
    DURABILITY("Inquebrável"),
    FIRE_ASPECT("Aspecto flamejante"),
    FROST_WALKER("Passos gelados"),
    IMPALING("Penetração"),
    KNOCKBACK("Repulsão"),
    LOOT_BONUS_BLOCKS("Fortuna"),
    LOOT_BONUS_MOBS("Pilhagem"),
    LOYALTY("Lealdade"),
    LUCK("Sorte do mar"),
    LURE("Isca"),
    MENDING("Remendo"),
    MULTISHOT("Tiro múltiplo"),
    OXYGEN("Respiração"),
    PIERCING("Perfuração"),
    PROTECTION_ENVIRONMENTAL("Proteção"),
    PROTECTION_EXPLOSIONS("Proteção contra explosões"),
    PROTECTION_FALL("Peso pena"),
    PROTECTION_FIRE("Proteção contra o fogo"),
    PROTECTION_PROJECTILE("Proteção contra projéteis"),
    QUICK_CHARGE("Carga rápida"),
    RIPTIDE("Correnteza"),
    SILK_TOUCH("Toque suave"),
    SOUL_SPEED("Velocidade das almas"),
    SWEEPING_EDGE("Alcance"),
    SWIFT_SNEAK("Passos furtivos"),
    THORNS("Espinhos"),
    VANISHING_CURSE("Maldição do desaparecimento"),
    WATER_WORKER("Afinidade aquática");

    private static final Map<Enchantment, EnchantmentName> CACHE = new HashMap<>();

    static {
        for (EnchantmentName name : values()) {
            Enchantment enchantment = Enchantment.getByName(name.name());
            if (enchantment != null) {
                CACHE.put(enchantment, name);
            }
        }
    }

    private final String translatedName;

    EnchantmentName(String translatedName) {
        this.translatedName = translatedName;
    }

    public String getName() {
        return translatedName;
    }

    public static String translate(Enchantment enchantment) {
        EnchantmentName name = CACHE.get(enchantment);
        return name != null ? name.getName() : enchantment.getName();
    }

    @Override
    public String toString() {
        return translatedName;
    }
}
