package daxz.dev.spellwave.Utilities.Lib;

import daxz.dev.spellwave.Registry.ItemRegistry;
import daxz.dev.spellwave.Spellwave;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.Collection;

import static java.util.Arrays.stream;

public class InventoryMaker {

    public static Inventory setItems(String title, int size, String... items){

        ItemRegistry itemRegistry = new ItemRegistry();
        Inventory inventory = Bukkit.createInventory(null, size, title);

//        Arrays.stream(items)
//                .map(item -> itemRegistry.getItem(item))
//                .toList();

        int loop = 0;

        for(String item : items){
            ItemStack stack = itemRegistry.getItem(item);
            if (stack != null) {
                inventory.setItem(loop, stack);
            }
            loop++;
        }

        return inventory;

    }

}
