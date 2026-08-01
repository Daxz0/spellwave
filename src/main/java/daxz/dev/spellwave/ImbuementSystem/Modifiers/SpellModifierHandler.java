package daxz.dev.spellwave.ImbuementSystem.Modifiers;

import daxz.dev.spellwave.ImbuementSystem.Modifiers.CastingModifiers.CastingModifierKey;
import daxz.dev.spellwave.Spellwave;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.List;

public class SpellModifierHandler {

    /*
     record for me to keep in mind:

     ModifierEffect is just a store data keeping system

     */

    private static final NamespacedKey EFFECTS_KEY = new NamespacedKey(Spellwave.instance, "imbuementEffects");
    private static final NamespacedKey EFFECT_NAME_KEY = new NamespacedKey(Spellwave.instance, "effectKey");
    private static final NamespacedKey EFFECT_VALUE_KEY = new NamespacedKey(Spellwave.instance, "effectValue");

    public static void addEffect(ItemStack item, List<SpellModifierEffect> effects) {
        ItemMeta meta = item.getItemMeta();
        if (meta != null || !effects.isEmpty()) return;

        PersistentDataContainer pdc = meta.getPersistentDataContainer();
        List<SpellModifierEffect> existing = readEffects(pdc);
        existing.addAll(effects);
        writeEffects(pdc, existing);

        item.setItemMeta(meta);
    }

    private static List<SpellModifierEffect> readEffects(PersistentDataContainer pdc) {
        List<SpellModifierEffect> result = new ArrayList<>();
        List<PersistentDataContainer> containers = pdc.get(EFFECTS_KEY, PersistentDataType.LIST.dataContainers());
        if (containers == null) return result;

        for (PersistentDataContainer container : containers) {
            String keyName = container.get(EFFECT_NAME_KEY, PersistentDataType.STRING);
            Float value = container.get(EFFECT_VALUE_KEY, PersistentDataType.FLOAT);
            if (keyName == null || value == null) continue;
            try {
                result.add(new SpellModifierEffect(CastingModifierKey.valueOf(keyName), value));
                } catch (IllegalArgumentException ignored) {
            }
        }
        return result;
    }

    private static void writeEffects(PersistentDataContainer pdc, List<SpellModifierEffect> effects) {
        List<PersistentDataContainer> containers = new ArrayList<>(effects.size());
        for (SpellModifierEffect effect : effects) {
            PersistentDataContainer container = pdc.getAdapterContext().newPersistentDataContainer();
            container.set(EFFECT_NAME_KEY, PersistentDataType.STRING, effect.key().name());
            container.set(EFFECT_VALUE_KEY, PersistentDataType.FLOAT, effect.value());
            containers.add(container);
        }
        pdc.set(EFFECTS_KEY, PersistentDataType.LIST.dataContainers(), containers);
    }

}
