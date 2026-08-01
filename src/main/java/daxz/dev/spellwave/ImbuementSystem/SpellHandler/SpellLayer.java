package daxz.dev.spellwave.ImbuementSystem.SpellHandler;

import com.jeff_media.customblockdata.CustomBlockData;
import daxz.dev.spellwave.Events.Workstations.ImbuementTableHandler;
import daxz.dev.spellwave.Spellwave;
import org.bukkit.Bukkit;
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

public class SpellLayer {

    public static void completeRecipe(Block imbuementTable, Player player, int layers){

        PersistentDataContainer tableTag = new CustomBlockData(imbuementTable, Spellwave.instance);

        String centralUuidStr = tableTag.getOrDefault(ImbuementTableHandler.imbuementCentralItem, PersistentDataType.STRING, "");
        if (centralUuidStr.isEmpty()) return;

        Entity centralEntity = Bukkit.getEntity(UUID.fromString(centralUuidStr));
        if (!(centralEntity instanceof Item centralItemEntity)) return;

        ItemStack centralItem = centralItemEntity.getItemStack();

//        if (centralItem.getOrDefault(imbuementCentralItem, PersistentDataType.STRING, "").isEmpty()) return;

        LinkedList<Integer, List<ItemStack>> ringItemsRegistry = new LinkedList<>();

        for (int i = 1; i < layers; i++) {

            List<Block> ring = getRing(imbuementTable.getLocation(), i);

            PersistentDataContainer ringItems =  new CustomBlockData(imbuementTable, Spellwave.instance);
            UUID ringItemUUID = UUID.fromString(Objects.requireNonNull(ringItems.get(imbuementItemRing, PersistentDataType.STRING)));
            Entity entity = Bukkit.getEntity(ringItemUUID);
            if (entity != null && entity instanceof Item item) {


            }


        }

    }

}
