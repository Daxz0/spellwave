package daxz.dev.spellwave.ImbuementSystem.SpellHandler;

import com.jeff_media.customblockdata.CustomBlockData;
import daxz.dev.spellwave.ImbuementSystem.Modifiers.CastingModifiers.CastingModifier;
import daxz.dev.spellwave.ImbuementSystem.Modifiers.CastingModifiers.CastingModifierKey;
import daxz.dev.spellwave.ImbuementSystem.Modifiers.SpellModifierEffect;
import daxz.dev.spellwave.Registry.SpellRegistry;
import daxz.dev.spellwave.Spellwave;
import daxz.dev.spellwave.Utilities.Lib.LoreTool;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataAdapterContext;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

import static daxz.dev.spellwave.Events.Workstations.ImbuementTableHandler.imbuementCentralItem;

public class SpellRecipe {
    // this is what assigns shit and stuff to the item being imbued. sigh.

    public static final NamespacedKey imbuementLayers = new NamespacedKey(Spellwave.instance, "imbuementLayers");

    public static final NamespacedKey layerCastingMaterials = new NamespacedKey(Spellwave.instance, "layerCastingMaterials");
    public static final NamespacedKey layerModifierKeys = new NamespacedKey(Spellwave.instance, "layerModifierKeys");
    public static final NamespacedKey layerModifierValues = new NamespacedKey(Spellwave.instance, "layerModifierValues");

    public static void createRecipe(Block imbuementTable, TreeMap<Integer, List<ItemStack>> items) {

        PersistentDataContainer imbuementTablePDC = new CustomBlockData(imbuementTable, Spellwave.instance);
        UUID itemUUID = UUID.fromString(Objects.requireNonNull(imbuementTablePDC.get(imbuementCentralItem, PersistentDataType.STRING)));
        Entity entity = Bukkit.getEntity(itemUUID);

        if (!(entity instanceof Item item)) return;

        ItemStack itemStack = item.getItemStack();
        ItemMeta meta = itemStack.getItemMeta();
        if (meta == null) return;

        PersistentDataContainer itemPDC = meta.getPersistentDataContainer();
        PersistentDataAdapterContext context = itemPDC.getAdapterContext();

        List<PersistentDataContainer> layerContainers = new ArrayList<>();
        List<String> loreLines = new ArrayList<>();

        for (Map.Entry<Integer, List<ItemStack>> layerEntry : items.entrySet()) {
            int layerIndex = layerEntry.getKey();
            List<ItemStack> layerItems = layerEntry.getValue();

            List<String> castingCandidates = new ArrayList<>();
            Map<CastingModifierKey, Float> aggregatedModifiers = new EnumMap<>(CastingModifierKey.class);

            for (ItemStack ringItem : layerItems) {
                Material type = ringItem.getType();

                if (SpellRegistry.hasCastingModifier(type)) {
                    Class<? extends CastingModifier> modifierClass = SpellRegistry.getCastingModifier(type);
                    castingCandidates.add(modifierClass.getSimpleName());
                }

                if (SpellRegistry.hasSpellModifier(type)) {
                    for (SpellModifierEffect effect : SpellRegistry.getModifierEffects(type)) {
                        aggregatedModifiers.merge(effect.key(), effect.value(), Float::sum);
                    }
                }
            }

            List<String> castingMaterials = new ArrayList<>();
            if (!castingCandidates.isEmpty()) {
                String chosen = castingCandidates.size() == 1
                        ? castingCandidates.getFirst()
                        : castingCandidates.get(ThreadLocalRandom.current().nextInt(castingCandidates.size()));
                castingMaterials.add(chosen);
            }

            if (!castingMaterials.isEmpty() || !aggregatedModifiers.isEmpty()) {
                loreLines.add("<gray><bold>Layer " + layerIndex);

                if (!castingMaterials.isEmpty()) {
                    loreLines.add("  <aqua>" + castingMaterials.getFirst());
                }

                for (Map.Entry<CastingModifierKey, Float> modEntry : aggregatedModifiers.entrySet()) {
                    float value = modEntry.getValue();
                    String color = value >= 0 ? "<green>" : "<red>";
                    String sign = value >= 0 ? "+" : "";
                    loreLines.add("  <white>" + formatKeyName(modEntry.getKey()) + ": "
                            + color + sign + trimTrailingZero(value) + "<reset>");
                }
            }

            List<String> modifierKeyNames = new ArrayList<>();
            List<Float> modifierValueList = new ArrayList<>();
            for (Map.Entry<CastingModifierKey, Float> e : aggregatedModifiers.entrySet()) {
                modifierKeyNames.add(e.getKey().name());
                modifierValueList.add(e.getValue());
            }

            PersistentDataContainer layerPDC = context.newPersistentDataContainer();
            layerPDC.set(layerCastingMaterials, PersistentDataType.LIST.strings(), castingMaterials);
            layerPDC.set(layerModifierKeys, PersistentDataType.LIST.strings(), modifierKeyNames);
            layerPDC.set(layerModifierValues, PersistentDataType.LIST.floats(), modifierValueList);

            layerContainers.add(layerPDC);
        }

        itemPDC.set(imbuementLayers, PersistentDataType.LIST.dataContainers(), layerContainers);

        itemStack.setItemMeta(meta);
        if (!loreLines.isEmpty()) {
            LoreTool.lore(itemStack, loreLines.toArray(new String[0]));
        }
        item.setItemStack(itemStack);

    }

    private static String formatKeyName(CastingModifierKey key) {
        String[] words = key.name().split("_");
        StringBuilder sb = new StringBuilder();
        for (String word : words) {
            if (sb.length() > 0) sb.append(" ");
            sb.append(word.charAt(0)).append(word.substring(1).toLowerCase());
        }
        return sb.toString();
    }


    private static String trimTrailingZero(float value) {
        if (value == Math.floor(value)) {
            return String.valueOf((int) value);
        }
        return String.valueOf(value);
    }

    public static List<PersistentDataContainer> getLayers(ItemStack itemStack) {
        ItemMeta meta = itemStack.getItemMeta();
        if (meta == null) return List.of();
        PersistentDataContainer itemPDC = meta.getPersistentDataContainer();
        List<PersistentDataContainer> layers = itemPDC.get(imbuementLayers, PersistentDataType.LIST.dataContainers());
        return layers == null ? List.of() : layers;
    }

}