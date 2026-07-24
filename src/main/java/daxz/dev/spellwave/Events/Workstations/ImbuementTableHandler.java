package daxz.dev.spellwave.Events.Workstations;

import com.jeff_media.customblockdata.CustomBlockData;
import daxz.dev.spellwave.Inventories.ImbuementTableInventory;
import daxz.dev.spellwave.Items.Workstations.ImbuementTable;
import daxz.dev.spellwave.Spellwave;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

public class ImbuementTableHandler implements Listener {

    private static NamespacedKey spellwaveItemID = new NamespacedKey(Spellwave.instance, "spellwaveItemID");
    private static NamespacedKey imbuementTableTag = new NamespacedKey(Spellwave.instance, "imbuementTableTag");

    @EventHandler
    public void onPlayerPlacesImbuementTable(BlockPlaceEvent event){

        ItemStack item = event.getItemInHand();

        if (item.getItemMeta().getPersistentDataContainer().get(spellwaveItemID, PersistentDataType.STRING).equals("imbuement_table")) {
            PersistentDataContainer imbuementTag = new CustomBlockData(event.getBlockPlaced(), Spellwave.instance);
            imbuementTag.set(imbuementTableTag, PersistentDataType.BOOLEAN, true);
        }

    }

    @EventHandler
    public void onPlayerRightClicksImbuementTable(PlayerInteractEvent event){


        PersistentDataContainer imbuementTag = new CustomBlockData(event.getClickedBlock(), Spellwave.instance);

        if (imbuementTag.getOrDefault(imbuementTableTag, PersistentDataType.BOOLEAN, false)){
            Player player = event.getPlayer();
            ImbuementTableInventory UI = new ImbuementTableInventory(Spellwave.instance);
            player.openInventory(UI.getInventory());
        }

    }

    @EventHandler
    public void onImbuementTableInventoryClick(InventoryClickEvent event){



    }

}
