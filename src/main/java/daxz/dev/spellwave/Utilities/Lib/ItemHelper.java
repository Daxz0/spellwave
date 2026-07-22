package daxz.dev.haulover.Utilities.Lib;

import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

public class ItemHelper {

    public static <T, Z> void setItemPDC(ItemStack item, NamespacedKey key, PersistentDataType<T, Z> type, Z value) {
        ItemMeta meta = item.getItemMeta();
        if (meta == null) return;

        meta.getPersistentDataContainer().set(key, type, value);
        item.setItemMeta(meta);
    }

    public static <T, Z> Z getItemPDC(ItemStack item, NamespacedKey key, PersistentDataType<T, Z> type) {
        ItemMeta meta = item.getItemMeta();
        if (meta == null) return null;

        return meta.getPersistentDataContainer().get(key, type);
    }

    public static <T, Z> Z getItemPDCOrDefault(ItemStack item, NamespacedKey key, PersistentDataType<T, Z> type, Z defaultValue) {
        ItemMeta meta = item.getItemMeta();
        if (meta == null) return defaultValue;

        return meta.getPersistentDataContainer().getOrDefault(key, type, defaultValue);
    }

}