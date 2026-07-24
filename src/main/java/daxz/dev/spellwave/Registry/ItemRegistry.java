package daxz.dev.spellwave.Registry;


import daxz.dev.spellwave.Spellwave;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class ItemRegistry {
    private static final Map<String, SpellwaveItem> REGISTRY = new HashMap<>();

    /**
     * Registers an item, not for direct use
     */
    public static void registerItems() {

//        register(BasicWateringCan.INSTANCE);


    }

    /**
     * Registers an item in the registry from registerItems()
     *
     * @param item
     */
    private static void register(SpellwaveItem item) {

        REGISTRY.put(item.getID(), item);
        ShapedRecipe recipe = item.getRecipe();

        if (recipe != null) {
            Spellwave.getInstance().getServer().addRecipe(recipe);
        }
    }

    /**
     * Getter function to get the item
     *
     * @param id
     * @return item
     */
    public static ItemStack getItem(String id) {
        SpellwaveItem item = REGISTRY.get(id);

        if (item == null) {
            Material material = Material.matchMaterial(id);
            ItemStack nullItem = ItemStack.of(material);
            return nullItem != null ? nullItem : null;
        }

        return item != null ? item.createItem() : null;
    }

    /**
     * Gives an item to a player
     *
     * @param player
     * @param id
     * @return bool
     */
    public static boolean giveItem(Player player, String id) {
        SpellwaveItem item = REGISTRY.get(id);
        if (item != null) {
            player.getInventory().addItem(item.createItem());
            return true;
        }
        return false;
    }

    public static Map<String, SpellwaveItem> getRegisteredItems() {return REGISTRY;}

}
