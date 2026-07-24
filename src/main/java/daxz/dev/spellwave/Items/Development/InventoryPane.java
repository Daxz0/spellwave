package daxz.dev.spellwave.Items.Development;

import daxz.dev.spellwave.Registry.SpellwaveItem;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.jetbrains.annotations.Nullable;

public abstract class InventoryPane implements SpellwaveItem {


    @Override
    public String getID() {
        return "pane";
    }

    @Override
    public ItemStack createItem() {
        Material material = Material.BLACK_STAINED_GLASS_PANE;
        return ItemStack.of(material);
    }


    @Override
    public @Nullable ShapedRecipe getRecipe() {
        return null;
    }
}
