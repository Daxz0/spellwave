package daxz.dev.spellwave.Events.Extruders;

import daxz.dev.spellwave.ImbuementSystem.Modifiers.CastingModifiers.Bolt;
import daxz.dev.spellwave.ImbuementSystem.Modifiers.CastingModifiers.CastingModifierKey;
import daxz.dev.spellwave.ImbuementSystem.SpellHandler.SpellRecipe;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;

import static daxz.dev.spellwave.ImbuementSystem.SpellHandler.SpellRecipe.*;

public class SpellRunner implements Listener {

    @EventHandler
    public void onPlayerRightClicksWithImbuedItem(PlayerInteractEvent event) {

        Player player = event.getPlayer();
        ItemStack handItem = player.getInventory().getItemInMainHand();

        List<PersistentDataContainer> layers = SpellRecipe.getLayers(handItem);
        if (layers.isEmpty()) return;

        for (PersistentDataContainer layerPDC : layers) {
            runLayer(player, layerPDC);
        }
    }

    private void runLayer(Player player, PersistentDataContainer layerPDC) {
        List<String> castingMaterials = layerPDC.getOrDefault(layerCastingMaterials, PersistentDataType.LIST.strings(), List.of());
        if (castingMaterials.isEmpty()) return; // layer had no casting item, spell-modifiers-only ring

        List<String> modifierKeyNames = layerPDC.getOrDefault(layerModifierKeys, PersistentDataType.LIST.strings(), List.of());
        List<Float> modifierValues = layerPDC.getOrDefault(layerModifierValues, PersistentDataType.LIST.floats(), List.of());

        Map<CastingModifierKey, Float> modifiers = new EnumMap<>(CastingModifierKey.class);
        for (int i = 0; i < modifierKeyNames.size() && i < modifierValues.size(); i++) {
            try {
                modifiers.put(CastingModifierKey.valueOf(modifierKeyNames.get(i)), modifierValues.get(i));
            } catch (IllegalArgumentException ignored) {
            }
        }

        // TODO: only Bolt exists right now
        switch (castingMaterials.getFirst()) {
            case "Bolt" -> Bolt.fromModifiers(player, modifiers).createModifier();
            default -> {}
        }
    }
}