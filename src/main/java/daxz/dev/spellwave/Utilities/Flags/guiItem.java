package daxz.dev.spellwave.Utilities.Flags;

import daxz.dev.spellwave.Spellwave;
import org.bukkit.NamespacedKey;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;

public class guiItem implements Listener {

    public static NamespacedKey flag = new NamespacedKey(Spellwave.instance, "guiItem");

    @EventHandler
    public void preventGuiItemClick(InventoryClickEvent event) {
        if (!event.getInventory().equals(event.getWhoClicked().getOpenInventory().getTopInventory())) return;
        ItemStack item = event.getCurrentItem();
        if (item.getItemMeta() == null) return;
        if (item.getItemMeta().getPersistentDataContainer().getOrDefault(flag, PersistentDataType.BOOLEAN, false)) event.setCancelled(true);
    }


}
