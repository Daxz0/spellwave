package daxz.dev.spellwave.Registry;

import daxz.dev.spellwave.ImbuementSystem.Modifiers.CastingModifiers.Bolt;
import daxz.dev.spellwave.ImbuementSystem.Modifiers.CastingModifiers.CastingModifier;
import daxz.dev.spellwave.ImbuementSystem.Modifiers.CastingModifiers.CastingModifierKey;
import daxz.dev.spellwave.ImbuementSystem.Modifiers.SpellModifierEffect;
import org.bukkit.Material;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SpellRegistry {

    private static Map<Material, Class<? extends CastingModifier>> castingModifierRegistry = new HashMap<>();
    private static Map<Material, List<SpellModifierEffect>> spellModifiersRegistry = new EnumMap<>(Material.class);

    public static void register() {
        registerCastingModifiers();
        registerModifierEffects();
    }

    private static void registerCastingModifiers() {
        castingModifierRegistry.put(Material.AMETHYST_SHARD, Bolt.class);
    }
    private static void registerModifierEffects() {
        registerModifierEffect(Material.RABBIT_FOOT, List.of(
                new SpellModifierEffect(CastingModifierKey.SPEED, 2.0f),
                new SpellModifierEffect(CastingModifierKey.RANGE, -1.0f)
        ));
    }

    // -------------------------------------------

    public static Class<? extends CastingModifier> getCastingModifier(Material material) {
        return castingModifierRegistry.get(material);
    }

    public static boolean hasCastingModifier(Material material) {
        return castingModifierRegistry.containsKey(material);
    }

    public static boolean hasSpellModifier(Material material) {
        return spellModifiersRegistry.containsKey(material);
    }

    private static void registerModifierEffect(Material material, List<SpellModifierEffect> effects) {
        spellModifiersRegistry.put(material, effects);
    }

    public static List<SpellModifierEffect> getModifierEffects(Material material) {
        return spellModifiersRegistry.getOrDefault(material, List.of());
    }
}