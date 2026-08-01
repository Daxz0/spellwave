package daxz.dev.spellwave.ImbuementSystem.SpellHandler;

import com.jeff_media.customblockdata.CustomBlockData;
import daxz.dev.spellwave.Registry.SpellRegistry;
import daxz.dev.spellwave.Spellwave;
import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.*;

import static daxz.dev.spellwave.Events.Workstations.ImbuementTableHandler.*;

public class SpellLayers {

    // whole point of spelllayers is to check and make sure all items in the layer will work,
    // then save it and feed it to spell recipe where it will process and save the things to the item

    private TreeMap<Integer, List<ItemStack>> ringItemsRegistry = new TreeMap<>();

    public SpellLayers(Block imbuementTable, Player player, int layers) {

        PersistentDataContainer entitiesTag = new CustomBlockData(imbuementTable, Spellwave.instance);
        List<String> uuidStrings = entitiesTag.getOrDefault(
                highlightEntitiesKey,
                PersistentDataType.LIST.strings(),
                new ArrayList<>()
        );

        Map<Integer, List<ItemStack>> byLayer = new HashMap<>();

        for (String uuidStr : uuidStrings) {
            UUID uuid;
            try {
                uuid = UUID.fromString(uuidStr);
            } catch (IllegalArgumentException ex) {
                continue;
            }

            Entity entity = Bukkit.getEntity(uuid);
            if (entity == null || entity.getType() != EntityType.INTERACTION) continue;

            PersistentDataContainer interactionPDC = entity.getPersistentDataContainer();
            String itemUuidStr = interactionPDC.getOrDefault(imbuementItems, PersistentDataType.STRING, "");
            if (itemUuidStr.isBlank()) continue; // nothing was placed on this ring position

            Integer ringLayer = interactionPDC.get(imbuementItemRing, PersistentDataType.INTEGER);
            if (ringLayer == null) continue;

            UUID itemUUID;
            try {
                itemUUID = UUID.fromString(itemUuidStr);
            } catch (IllegalArgumentException ex) {
                continue;
            }

            Entity itemEntity = Bukkit.getEntity(itemUUID);
            if (!(itemEntity instanceof Item item)) continue;

            ItemStack itemStack = item.getItemStack();
            if (!SpellRegistry.hasCastingModifier(itemStack.getType()) && !SpellRegistry.hasSpellModifier(itemStack.getType()))
                continue; // skip if the item doesn't do anything

            byLayer.computeIfAbsent(ringLayer, k -> new ArrayList<>()).add(itemStack);
        }

        for (int i = 1; i <= layers; i++) {
            ringItemsRegistry.put(i, byLayer.getOrDefault(i, new ArrayList<>()));
        }
    }

    public TreeMap<Integer, List<ItemStack>> getRingItemsRegistry() {
        return ringItemsRegistry;
    }
}