package daxz.dev.spellwave.Events.Workstations;

import com.jeff_media.customblockdata.CustomBlockData;
import daxz.dev.spellwave.Inventories.ImbuementTableInventory;
import daxz.dev.spellwave.Items.Workstations.ImbuementTable;
import daxz.dev.spellwave.Spellwave;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.sound.Sound;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.util.Transformation;
import org.joml.AxisAngle4f;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ImbuementTableHandler implements Listener {

    private static NamespacedKey spellwaveItemID = new NamespacedKey(Spellwave.instance, "spellwaveItemID");
    private static NamespacedKey imbuementTableTag = new NamespacedKey(Spellwave.instance, "imbuementTableTag");

    @EventHandler
    public void onPlayerPlacesImbuementTable(BlockPlaceEvent event){

        ItemStack item = event.getItemInHand();

        Player player = event.getPlayer();

        if (Objects.equals(item.getItemMeta().getPersistentDataContainer().get(spellwaveItemID, PersistentDataType.STRING), "imbuement_table")) {
            PersistentDataContainer imbuementTag = new CustomBlockData(event.getBlockPlaced(), Spellwave.instance);
            imbuementTag.set(imbuementTableTag, PersistentDataType.BOOLEAN, true);
        }

    }

    @EventHandler
    public void onPlayerRightClicksImbuementTable(PlayerInteractEvent event){

        assert event.getClickedBlock() != null;
        PersistentDataContainer imbuementTag = new CustomBlockData(event.getClickedBlock(), Spellwave.instance);

        if (imbuementTag.getOrDefault(imbuementTableTag, PersistentDataType.BOOLEAN, false) && event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            event.setCancelled(true);
            Player player = event.getPlayer();
            ImbuementTableInventory UI = new ImbuementTableInventory(Spellwave.instance);
            if (detectValidImbuementArea(event.getClickedBlock())){
                player.openInventory(UI.getInventory());
            }
            else{
                player.sendMessage("smth broke yo -- FIX THIS LATER, ADD SOME EFFECTS/PARTICLES OR SUM");
                Particle.ANGRY_VILLAGER.builder()
                        .location(event.getClickedBlock().getLocation().add(0,1,0))
                        .offset(0.5,0.5,0.5)
                        .count(10)
                        .spawn();



            }
        }

    }


    private boolean detectValidImbuementArea(Block imbuementTable) {

        Location scanningLoc = imbuementTable.getLocation().add(0, -1, 0);

        int minRings = 1;
        int maxRings = 7;

        List<Block> validBlocks = new ArrayList<>();
        int depthReached = 0;

        if (scanningLoc.getBlock().getType() != Material.AMETHYST_BLOCK) return false;

        for (int i = 1; i <= maxRings; i++) {
            List<Block> ring = getRing(scanningLoc, i);

            boolean ringValid = true;
            for (Block block : ring) {
                if (block.getType() != Material.BLACKSTONE) {
                    ringValid = false;
                    break;
                }
            }

            if (!ringValid) {
                break;
            }

            validBlocks.addAll(ring);
            depthReached = i;
        }

        if (depthReached < minRings) {
            return false;
        }

        for (Block block : validBlocks) {
            highlightBlockEdges(block, Material.WHITE_CONCRETE);
        }

        return true;
    }

    private void highlightBlockEdges(Block target, Material mat) {
        Location origin = target.getLocation();
        World world = origin.getWorld();

        float t = 0.06f;

        for (int z = 0; z <= 1; z++) {
            spawnEdge(world, origin, new Vector3f(0, 1 - t / 2f, z - t / 2f), new Vector3f(1, t, t), mat);
        }

        for (int x = 0; x <= 1; x++) {
            spawnEdge(world, origin, new Vector3f(x - t / 2f, 1 - t / 2f, 0), new Vector3f(t, t, 1), mat);
        }

    }

    private void spawnEdge(World world, Location origin, Vector3f translation, Vector3f scale, Material mat) {
        BlockDisplay edge = (BlockDisplay) world.spawnEntity(origin, EntityType.BLOCK_DISPLAY);
        edge.setBlock(mat.createBlockData());

        Transformation transform = new Transformation(
                translation,
                new AxisAngle4f(0, 0, 1, 0),
                scale,
                new AxisAngle4f(0, 0, 1, 0)
        );

        edge.setTransformation(transform);
        edge.setBrightness(new Display.Brightness(15, 15));
        edge.setInterpolationDuration(0);
        edge.setShadowRadius(0);
    }


    private List<Block> getRing(Location center, int layer){
        List<Block> ring = new ArrayList<>();
        World world = center.getWorld();
        int cx = center.getBlockX();
        int cy = center.getBlockY();
        int cz = center.getBlockZ();

        if (layer == 0) {
            ring.add(world.getBlockAt(cx, cy, cz));
            return ring;
        }

        for (int dx = -layer; dx <= layer; dx++) {
            if (Math.abs(dx) == layer) {
                for (int dz = -layer; dz <= layer; dz++) {
                    ring.add(world.getBlockAt(cx + dx, cy, cz + dz));
                }
            } else {
                ring.add(world.getBlockAt(cx + dx, cy, cz + layer));
                ring.add(world.getBlockAt(cx + dx, cy, cz - layer));
            }
        }

        return ring;
    }

}
