package daxz.dev.spellwave.ImbuementSystem.SpellHandler;

import com.jeff_media.customblockdata.CustomBlockData;
import daxz.dev.spellwave.Events.Workstations.ImbuementTableHandler;
import daxz.dev.spellwave.ImbuementSystem.Modifiers.CastingModifiers.CastingModifier;
import daxz.dev.spellwave.Registry.SpellRegistry;
import daxz.dev.spellwave.Spellwave;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.*;

import static daxz.dev.spellwave.Events.Workstations.ImbuementTableHandler.getRing;
import static daxz.dev.spellwave.Events.Workstations.ImbuementTableHandler.imbuementItemRing;

public class SpellLayers {

    //whole point of spelllayers is to check and make sure all items in the layer will work, then save it and feed it to spell recipe where it will process and save the things to the item

    private TreeMap<Integer, List<ItemStack>> ringItemsRegistry = new TreeMap<>();

    public SpellLayers(Block imbuementTable, Player player, int layers){

        for (int i = 1; i < layers; i++) {
            List<Block> ring = getRing(imbuementTable.getLocation(), i);
            List<ItemStack> ringItemsRegisterList = new ArrayList<>();

            for (Block ringBlock : ring) {
                PersistentDataContainer ringItems =  new CustomBlockData(ringBlock, Spellwave.instance);
                UUID ringItemUUID = UUID.fromString(Objects.requireNonNull(ringItems.get(imbuementItemRing, PersistentDataType.STRING)));
                Entity entity = Bukkit.getEntity(ringItemUUID);
                if (entity instanceof Item item) {

                    ItemStack itemStack = item.getItemStack();
                    if (!SpellRegistry.hasCastingModifier(itemStack.getType()) && !SpellRegistry.hasSpellModifier(itemStack.getType())) continue; //skip if the item doesnt do anything
                    ringItemsRegisterList.add(itemStack);
                }
            }
            ringItemsRegistry.put(i, ringItemsRegisterList);
        }

    }

    public TreeMap<Integer, List<ItemStack>> getRingItemsRegistry() {
        return ringItemsRegistry;
    }
}
