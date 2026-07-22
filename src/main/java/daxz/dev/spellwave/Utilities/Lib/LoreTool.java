package daxz.dev.spellwave.Utilities.Lib;

import io.papermc.paper.datacomponent.DataComponentTypes;
import io.papermc.paper.datacomponent.item.ItemLore;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;

public class LoreTool {

    public static ItemStack lore(ItemStack item, String... lore) {
        ItemLore itemLore = ItemLore.lore(Arrays.stream(lore)
                .map(line -> MiniMessage.miniMessage()
                        .deserialize(line)
                        .decorationIfAbsent(TextDecoration.ITALIC, TextDecoration.State.FALSE))
                .toList());

        item.setData(DataComponentTypes.LORE, itemLore);
        return item;
    }


}
