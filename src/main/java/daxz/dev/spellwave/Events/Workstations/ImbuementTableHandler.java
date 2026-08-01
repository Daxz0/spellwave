package daxz.dev.spellwave.Events.Workstations;

import com.jeff_media.customblockdata.CustomBlockData;
import daxz.dev.spellwave.ImbuementSystem.SpellHandler.SpellLayers;
import daxz.dev.spellwave.Inventories.ImbuementTableInventory;
import daxz.dev.spellwave.Registry.ItemRegistry;
import daxz.dev.spellwave.Spellwave;
import daxz.dev.spellwave.Utilities.Lib.PlayerItemHelper;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.util.Transformation;
import org.bukkit.util.Vector;
import org.joml.AxisAngle4f;
import org.joml.Vector3f;

import java.util.*;

public class ImbuementTableHandler implements Listener {

    public static final NamespacedKey spellwaveItemID = new NamespacedKey(Spellwave.instance, "spellwaveItemID");
    public static final NamespacedKey imbuementTableTag = new NamespacedKey(Spellwave.instance, "imbuementTableTag");
    public static final NamespacedKey highlightEntitiesKey = new NamespacedKey(Spellwave.instance, "imbuementTableEntities");
    public static final NamespacedKey imbuementInteraction = new NamespacedKey(Spellwave.instance, "imbuementInteraction");
    public static final NamespacedKey imbuementItems = new NamespacedKey(Spellwave.instance, "imbuementItems");
    public static final NamespacedKey imbuementCentralItem = new NamespacedKey(Spellwave.instance, "imbuementCentralItem");
    public static final NamespacedKey imbuementItemRing = new NamespacedKey(Spellwave.instance, "imbuementItemRing");

    private List<Player> ratelimit = new ArrayList<>();

    @EventHandler
    public void onPlayerPlacesImbuementTable(BlockPlaceEvent event){

        ItemStack item = event.getItemInHand();

        if (Objects.equals(item.getItemMeta().getPersistentDataContainer().get(spellwaveItemID, PersistentDataType.STRING), "imbuement_table")) {
            PersistentDataContainer imbuementTag = new CustomBlockData(event.getBlockPlaced(), Spellwave.instance);
            imbuementTag.set(imbuementTableTag, PersistentDataType.BOOLEAN, true);
        }

    }


    @EventHandler
    public void onPlayerBreaksImbuementTable(BlockBreakEvent event){

        PersistentDataContainer imbuementTag = new CustomBlockData(event.getBlock(), Spellwave.instance);
        if (imbuementTag.getOrDefault(imbuementTableTag, PersistentDataType.BOOLEAN, false)){
            PersistentDataContainer entitiesTag = new CustomBlockData(event.getBlock(), Spellwave.instance);
            List<String> uuidStrings = entitiesTag.getOrDefault(
                    highlightEntitiesKey,
                    PersistentDataType.LIST.strings(),
                    new ArrayList<>()
            );

            PersistentDataContainer centralItem = new CustomBlockData(event.getBlock(), Spellwave.instance);
            if (!centralItem.getOrDefault(imbuementCentralItem, PersistentDataType.STRING, "").isEmpty()){
                UUID itemUUID = UUID.fromString(Objects.requireNonNull(centralItem.get(imbuementCentralItem, PersistentDataType.STRING)));
                Entity entity = Bukkit.getEntity(itemUUID);
                if (entity != null) {
                    if (entity instanceof Item item) {
                        item.setPickupDelay(50);
                        item.setGravity(false);
                        item.setVelocity(new Vector(0, -0.1, 0));
                        centralItem.set(imbuementCentralItem, PersistentDataType.STRING, "");
                    }
                }
            }

            for (String uuidStr : uuidStrings) {
                try {
                    UUID uuid = UUID.fromString(uuidStr);
                    Entity entity = Bukkit.getEntity(uuid);

                    if (entity != null) {
                        if (entity.getType() == EntityType.INTERACTION
                                && !entity.getPersistentDataContainer().getOrDefault(imbuementItems, PersistentDataType.STRING, "").isEmpty()) {

                            UUID droppedItemUUID = UUID.fromString(Objects.requireNonNull(
                                    entity.getPersistentDataContainer().get(imbuementItems, PersistentDataType.STRING)));
                            Entity droppedItemEntity = Bukkit.getEntity(droppedItemUUID);

                            if (droppedItemEntity instanceof Item item) {
                                item.setPickupDelay(25);
                                item.setVelocity(item.getLocation().subtract(event.getBlock().getLocation()).toVector().add(new Vector(0,0.8,0)).multiply(new Vector(0.15,1,0.15)));
                                Particle.EXPLOSION.builder()
                                        .location(event.getBlock().getLocation().add(0,1,0))
                                        .offset(0.1,0.1,0.1)
                                        .spawn();
                            }
                        }
                        entity.remove();
                    }
                } catch (IllegalArgumentException ignored) {
                }
            }

            event.setDropItems(false);
            Item drop = event.getPlayer().getWorld().dropItem(event.getBlock().getLocation(), Objects.requireNonNull(ItemRegistry.getItem("imbuement_table")));
            drop.setVelocity(new Vector(0,0.1,0));
        }


    }

    @EventHandler
    public void onPlayerRightClicksImbuementTable(PlayerInteractEvent event){



        if (event.getClickedBlock() == null) return;
        PersistentDataContainer imbuementTag = new CustomBlockData(event.getClickedBlock(), Spellwave.instance);

        if (imbuementTag.getOrDefault(imbuementTableTag, PersistentDataType.BOOLEAN, false) && event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            Player player = event.getPlayer();
            if (ratelimit.contains(player)) return;
            ratelimit.add(player);
            Bukkit.getScheduler().runTaskLater(Spellwave.instance, () -> ratelimit.remove(player), 2L);

            event.setCancelled(true);

            PersistentDataContainer centralItem = new CustomBlockData(event.getClickedBlock(), Spellwave.instance);
            if (player.isSneaking()) {
                if (!centralItem.getOrDefault(imbuementCentralItem, PersistentDataType.STRING, "").isEmpty()){
                    UUID itemUUID = UUID.fromString(Objects.requireNonNull(centralItem.get(imbuementCentralItem, PersistentDataType.STRING)));
                    Entity entity = Bukkit.getEntity(itemUUID);
                    if (entity instanceof Item item) {
                        item.setPickupDelay(0);
                        item.setGravity(true);
                    }
                    centralItem.set(imbuementCentralItem, PersistentDataType.STRING, "");
                    return;
                }
                if (PlayerItemHelper.takeHandItem(player)) return;
                ItemStack newItem = player.getInventory().getItemInMainHand().clone();
                newItem.setAmount(1);

                Item droppedItem = player.getWorld().dropItem(event.getClickedBlock().getLocation().add(0.5,0.5,0.5), newItem);
                centralItem.set(imbuementCentralItem, PersistentDataType.STRING, droppedItem.getUniqueId().toString());

                droppedItem.setPickupDelay(Integer.MAX_VALUE);
                droppedItem.setPersistent(true);
                droppedItem.setVelocity(new Vector(0,0,0));

//                Bukkit.getScheduler().runTaskTimer(Spellwave.instance, () -> {
//                    Particle.ENCHANT.builder()
//                            .location(event.getClickedBlock().getLocation().add(0.5, 3, 0.5))
//                            .count(25)
//                            .offset(0.4, 1, 0.4)
//                            .spawn();
//                }, 5L, 1L);

                Bukkit.getScheduler().runTaskLater(Spellwave.instance, () -> {
                    droppedItem.setGravity(false);
                    droppedItem.setVelocity(new Vector(0,0.001,0));
                    Bukkit.getScheduler().runTaskLater(Spellwave.instance, () -> {
                        if (!droppedItem.isDead()) {
                            droppedItem.setVelocity(new Vector(0, 0, 0));
                        }
                    }, 10L);
                }, 1L);
            }
            else{
                ImbuementTableInventory UI = new ImbuementTableInventory(Spellwave.instance);
                int maxLayers = detectValidImbuementArea(event.getClickedBlock());
                if (maxLayers > 0){
                    SpellLayers layers = new SpellLayers(event.getClickedBlock(), player, maxLayers);
                    return;
                }

                else{
                    Particle.ANGRY_VILLAGER.builder()
                            .location(event.getClickedBlock().getLocation().add(0,1,0))
                            .offset(0.5,0.5,0.5)
                            .count(10)
                            .spawn();



                }
            }
        }
    }


    @EventHandler
    public void onPlayerRightClicksImbuementInteraction(PlayerInteractAtEntityEvent event){
        Entity entity = event.getRightClicked();

        if (entity.getType() != EntityType.INTERACTION
                || entity.getPersistentDataContainer().getOrDefault(imbuementInteraction, PersistentDataType.STRING, "").isBlank()) {
            return;
        }

        Player player = event.getPlayer();
        if (ratelimit.contains(player)) return;
        ratelimit.add(player);
        Bukkit.getScheduler().runTaskLater(Spellwave.instance, () -> ratelimit.remove(player), 2L);
        String storedUuid = entity.getPersistentDataContainer().getOrDefault(imbuementItems, PersistentDataType.STRING, "");

        if (!storedUuid.isBlank()) {
            UUID uuid = UUID.fromString(storedUuid);
            Entity storedEntity = Bukkit.getEntity(uuid);

            if (storedEntity instanceof Item itemEntity) {
                ItemStack stack = itemEntity.getItemStack();
                if (stack.getType() != Material.AIR && stack.getAmount() > 0) {
                    player.give(stack);
                }
                itemEntity.remove();
            }

            entity.getPersistentDataContainer().remove(imbuementItems);
            return;
        }

        if (PlayerItemHelper.takeHandItem(player)) return;
        ItemStack newItem = player.getInventory().getItemInMainHand().clone();
        newItem.setAmount(1);
        Item droppedItem = player.getWorld().dropItem(entity.getLocation(), newItem);
        entity.getPersistentDataContainer().set(imbuementItems, PersistentDataType.STRING, droppedItem.getUniqueId().toString());
        droppedItem.setPickupDelay(Integer.MAX_VALUE);
        droppedItem.setUnlimitedLifetime(true);
        droppedItem.setVelocity(new Vector(0, 0, 0));
    }


    private int detectValidImbuementArea(Block imbuementTable) {

        Location scanningLoc = imbuementTable.getLocation().add(0, -1, 0);

        int minRings = 1;
        int maxRings = 1; //TODO lets just get 1 ring working for now

        Map<Block, Integer> validBlocks = new LinkedHashMap<>();
        int depthReached = 0;

        if (scanningLoc.getBlock().getType() != Material.AMETHYST_BLOCK) return 0;

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

            for (Block block : ring) {
                validBlocks.put(block, i);
            }
            depthReached = i;
        }

        if (depthReached < minRings) {
            return 0;
        }

        List<String> finalList = new ArrayList<>();

        PersistentDataContainer entitiesTag = new CustomBlockData(imbuementTable, Spellwave.instance);
        if (!entitiesTag.getOrDefault(highlightEntitiesKey, PersistentDataType.LIST.strings(), new ArrayList<>()).isEmpty()) {
            return depthReached;
        }

        for (Map.Entry<Block, Integer> entry : validBlocks.entrySet()) {
            Block block = entry.getKey();
            int ringLayer = entry.getValue();

            for (BlockDisplay display : highlightBlockEdges(block)) {
                finalList.add(display.getUniqueId().toString());
            }
            PersistentDataContainer blockTag = new CustomBlockData(block, Spellwave.instance);
            blockTag.set(imbuementItemRing, PersistentDataType.INTEGER, ringLayer); //hopefully just gets overriden when a new one is placed
            Interaction interaction = spawnInteract(block);
            interaction.getPersistentDataContainer().set(imbuementInteraction, PersistentDataType.STRING, imbuementTable.getLocation().toString());
            interaction.getPersistentDataContainer().set(imbuementItemRing, PersistentDataType.INTEGER, ringLayer);
            finalList.add(interaction.getUniqueId().toString());
        }

        entitiesTag.set(highlightEntitiesKey, PersistentDataType.LIST.strings(), finalList);

        return depthReached;
    }

    private List<BlockDisplay> highlightBlockEdges(Block target) {
        Location origin = target.getLocation();
        World world = origin.getWorld();

        float t = 0.06f;

        List<BlockDisplay> output = new ArrayList<>();

        for (int z = 0; z <= 1; z++) {
            output.add(spawnEdge(world, origin, new Vector3f(0, 1 - t / 2f, z - t / 2f), new Vector3f(1, t, t), Material.WHITE_CONCRETE));
        }

        for (int x = 0; x <= 1; x++) {
            output.add(spawnEdge(world, origin, new Vector3f(x - t / 2f, 1 - t / 2f, 0), new Vector3f(t, t, 1), Material.WHITE_CONCRETE));
        }

        return output;
    }

    private BlockDisplay spawnEdge(World world, Location origin, Vector3f translation, Vector3f scale, Material mat) {
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

        return edge;
    }

    private Interaction spawnInteract(Block target) {
        Location center = target.getLocation().add(0.5, 1, 0.5);

        Interaction interaction = (Interaction) target.getWorld().spawnEntity(center, EntityType.INTERACTION);
        interaction.setInteractionWidth(1.0f);
        interaction.setInteractionHeight(0.1f);
        interaction.setResponsive(true);

        return interaction;
    }


    public static List<Block> getRing(Location center, int layer){
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
