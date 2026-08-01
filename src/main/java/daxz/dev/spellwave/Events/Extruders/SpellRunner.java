package daxz.dev.spellwave.Events.Extruders;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;

public class SpellRunner implements Listener {

    @EventHandler
    public void onPlayerRightClicksWithImbuedItem(PlayerInteractAtEntityEvent event) {

        Player player = event.getPlayer();


    }


}
