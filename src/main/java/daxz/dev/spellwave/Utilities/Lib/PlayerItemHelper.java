package daxz.dev.spellwave.Utilities.Lib;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class PlayerItemHelper {

    /**
     * Takes 1 item from the player's main hand and returns a new ItemStack of amount 1.
     * If hand is empty, returns null.
     */
    public static ItemStack takeHandItem(Player player) {
        ItemStack handItem = player.getInventory().getItemInMainHand();

        if (handItem.getType() == Material.AIR || handItem.getAmount() <= 0) {
            return null;
        }

        ItemStack singleItem = handItem.clone();
        singleItem.setAmount(1);

        if (handItem.getAmount() > 1) {
            handItem.setAmount(handItem.getAmount() - 1);
        } else {
            player.getInventory().setItemInMainHand(null);
        }

        return singleItem;
    }
}