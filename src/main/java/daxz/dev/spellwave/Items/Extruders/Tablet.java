package daxz.dev.spellwave.Items.Extruders;

import daxz.dev.spellwave.Registry.SpellwaveItem;
import daxz.dev.spellwave.Spellwave;
import daxz.dev.spellwave.Utilities.Lib.LoreTool;
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

public class Tablet implements SpellwaveItem {


    private static final NamespacedKey spellwaveItemID = new NamespacedKey(Spellwave.instance, "spellwaveItemID");

    @Override
    public String getID() {
        return "tablet";
    }

    @Override
    public ItemStack createItem() {
        Material material = Material.BLACK_BANNER;
        ItemStack item = ItemStack.of(material);



        item.setData(DataComponentTypes.CUSTOM_NAME, Component.text("Imbuement Tablet", NamedTextColor.WHITE).decoration(TextDecoration.ITALIC, false));

//        LoreTool.lore(item,
//                "<dark_gray> Enables you to start imbuing things.",
//                "<dark_gray><st>                                          </st>",
//                "<white>\uD83C\uDF31 <bold>Workstation"
//        );



        item.editPersistentDataContainer(pdc -> {
            pdc.set(spellwaveItemID, PersistentDataType.STRING, getID());
        });


        return item;
    }

    @Override
    public @Nullable ShapedRecipe getRecipe() {
        return null;
    }
}
