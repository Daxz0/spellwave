package daxz.dev.spellwave.Inventories;

import daxz.dev.spellwave.Spellwave;
import daxz.dev.spellwave.Utilities.Lib.InventoryMaker;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.jetbrains.annotations.NotNull;

public class ImbuementTableInventory implements InventoryHolder {

    private final Inventory inventory;

    public ImbuementTableInventory(Spellwave spellwave) {

//        this.inventory = spellwave.getServer().createInventory(this, 54, "Imbuement Table");

        this.inventory = InventoryMaker.setItems("Imbuement Table", 54,
                new String[]
                        {"pane", "pane", "pane", "pane", "pane", "pane", "pane", "pane", "pane",
                         "pane", "pane", "pane", "pane", "pane", "pane", "pane", "pane", "pane",
                         "pane", "pane", "pane", "pane", "pane", "pane", "pane", "pane", "pane",
                         "pane", "pane", "pane", "pane", "pane", "pane", "pane", "pane", "pane",
                         "pane", "pane", "pane", "pane", "pane", "pane", "pane", "pane", "pane"
                        }
        );


    }

    @Override
    public @NotNull Inventory getInventory() {
        return this.inventory;
    }
}
