package daxz.dev.spellwave.Items.Development;

import daxz.dev.spellwave.Registry.SpellwaveItem;
import daxz.dev.spellwave.Spellwave;
import daxz.dev.spellwave.Utilities.Flags.guiItem;
import io.papermc.paper.datacomponent.DataComponentTypes;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.Nullable;

public class InventoryPane implements SpellwaveItem {

    private static NamespacedKey spellwaveItemID =new NamespacedKey(Spellwave.instance, "spellwaveItemID");


    @Override
    public String getID() {
        return "pane";
    }

    @Override
    public ItemStack createItem() {
        Material material = Material.BLACK_STAINED_GLASS_PANE;
        ItemStack item = new ItemStack(material);

        item.setData(DataComponentTypes.CUSTOM_NAME, Component.text(" ", NamedTextColor.WHITE).decoration(TextDecoration.ITALIC, false));


        item.editPersistentDataContainer(pdc -> {
                pdc.set(guiItem.flag, PersistentDataType.BOOLEAN, true);
            }
        );


        return item;
    }


    @Override
    public @Nullable ShapedRecipe getRecipe() {
        return null;
    }
}
