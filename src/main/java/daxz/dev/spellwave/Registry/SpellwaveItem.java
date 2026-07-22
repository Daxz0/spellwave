package daxz.dev.spellwave.Registry;

import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.jetbrains.annotations.Nullable;

/**
 * Item parent i guess
 */
public interface SpellwaveItem {
    /**
     * Gets the item ID
     * @return id
     */
    String getID();

    /**
     * Creates an item
     * @return bool
     */
    ItemStack createItem();


    @Nullable ShapedRecipe getRecipe();
}