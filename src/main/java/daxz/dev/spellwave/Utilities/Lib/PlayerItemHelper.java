package daxz.dev.spellwave.Utilities.Lib;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class PlayerItemHelper {

    public static boolean takeHandItem(Player player) {
        ItemStack item = player.getInventory().getItemInMainHand();
        if (item.getType() == Material.AIR) return true;

        ItemStack newItem = item.clone();
        newItem.setAmount(1);

        if (item.getAmount() <= 1) {
            player.getInventory().setItemInMainHand(null);
        } else {
            item.setAmount(item.getAmount() - 1);
        }
        return false;
    }

}
