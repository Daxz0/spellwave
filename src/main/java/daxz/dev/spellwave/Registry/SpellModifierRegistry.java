package daxz.dev.spellwave.Registry;

import daxz.dev.spellwave.ImbuementSystem.Modifiers.CastingModifiers.CastingModifierKey;
import daxz.dev.spellwave.ImbuementSystem.Modifiers.SpellModifierEffect;
import org.bukkit.Material;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;

public class SpellModifierRegistry {

    private static final Map<Material, List<SpellModifierEffect>> REGISTRY = new EnumMap<>(Material.class);

    static {
        register(Material.RABBIT_FOOT, List.of(
                new SpellModifierEffect(CastingModifierKey.SPEED, 2.0f),
                new SpellModifierEffect(CastingModifierKey.RANGE, -1.0f)
        ));
    }

    public static void register(Material material, List<SpellModifierEffect> effects) {
        REGISTRY.put(material, effects);
    }

    public static boolean has(Material material) {
        return REGISTRY.containsKey(material);
    }

    public static List<SpellModifierEffect> get(Material material) {
        return REGISTRY.getOrDefault(material, List.of());
    }
}