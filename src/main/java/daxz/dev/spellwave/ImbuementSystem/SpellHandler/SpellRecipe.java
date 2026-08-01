package daxz.dev.spellwave.ImbuementSystem.SpellHandler;

import com.jeff_media.customblockdata.CustomBlockData;
import daxz.dev.spellwave.ImbuementSystem.Modifiers.CastingModifiers.CastingModifierKey;
import daxz.dev.spellwave.ImbuementSystem.Modifiers.SpellModifierEffect;
import daxz.dev.spellwave.Registry.SpellRegistry;
import daxz.dev.spellwave.Spellwave;
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

        for (Map.Entry<Integer, List<ItemStack>> layerEntry : items.entrySet()) {
            List<ItemStack> layerItems = layerEntry.getValue();

            List<String> castingMaterials = new ArrayList<>();
            Map<CastingModifierKey, Float> aggregatedModifiers = new EnumMap<>(CastingModifierKey.class);

            for (ItemStack ringItem : layerItems) {
                Material type = ringItem.getType();

                if (SpellRegistry.hasCastingModifier(type)) {
                    castingMaterials.add(type.name());
                }

                if (SpellRegistry.hasSpellModifier(type)) {
                    for (SpellModifierEffect effect : SpellRegistry.getModifierEffects(type)) {
                        aggregatedModifiers.merge(effect.key(), effect.value(), Float::sum);
                    }
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
        item.setItemStack(itemStack);

        imbuementTablePDC.set(imbuementCentralItem, PersistentDataType.STRING, "");
    }

    public static List<PersistentDataContainer> getLayers(ItemStack itemStack) {
        ItemMeta meta = itemStack.getItemMeta();
        if (meta == null) return List.of();
        PersistentDataContainer itemPDC = meta.getPersistentDataContainer();
        List<PersistentDataContainer> layers = itemPDC.get(imbuementLayers, PersistentDataType.LIST.dataContainers());
        return layers == null ? List.of() : layers;
    }
}