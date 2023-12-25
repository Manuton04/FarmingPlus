package fp.manuton.events;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldguard.LocalPlayer;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.flags.Flags;
import com.sk89q.worldguard.protection.regions.RegionContainer;
import com.sk89q.worldguard.protection.regions.RegionQuery;
import fp.manuton.FarmingPlus;
import fp.manuton.enchantments.CustomEnchantments;
import fp.manuton.guis.FarmersStepGui;
import fp.manuton.utils.ItemUtils;
import fp.manuton.utils.LocationUtils;
import fp.manuton.utils.MessageUtils;
import fp.manuton.utils.SoundUtils;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Container;
import org.bukkit.block.data.Ageable;
import org.bukkit.block.data.Openable;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

import static org.bukkit.event.block.Action.*;

//
public class PlayerListener implements Listener {

    @EventHandler(priority = EventPriority.LOW)
    public void Replenish(BlockBreakEvent event){
        if (event.getPlayer().getInventory().getItemInMainHand().getType() == Material.AIR)
            return;
        if (!event.getPlayer().getInventory().getItemInMainHand().hasItemMeta())
            return;
        if (!Objects.requireNonNull(event.getPlayer().getInventory().getItemInMainHand().getItemMeta()).hasEnchant(CustomEnchantments.REPLENISH))
            return;
        if (event.getPlayer().getGameMode() == GameMode.CREATIVE || event.getPlayer().getGameMode() == GameMode.SPECTATOR)
            return;
        Block block = event.getBlock();
        if (block.getState() instanceof Container)
            return;
        if (!(block.getType().equals(Material.CARROTS) || block.getType().equals(Material.WHEAT) || block.getType().equals(Material.POTATOES) || block.getType().equals(Material.BEETROOTS)|| block.getType().equals(Material.NETHER_WART)|| block.getType().equals(Material.COCOA)))
            return;

        Player player = event.getPlayer();

        // Get the WorldGuard instance
        WorldGuard worldGuard = WorldGuard.getInstance();
        // Get the region container
        RegionContainer regionContainer = worldGuard.getPlatform().getRegionContainer();
        // Create a query from the region container
        RegionQuery query = regionContainer.createQuery();
        // Get the WorldGuardPlugin instance for the player
        WorldGuardPlugin pluginW = WorldGuardPlugin.inst();
        // Get the LocalPlayer instance for the player
        LocalPlayer localPlayer = pluginW.wrapPlayer(player);

        // Get all regions at the block's location
        ApplicableRegionSet regions = query.getApplicableRegions(BukkitAdapter.adapt(block.getLocation()));
        // Check if the player has permission to place blocks in these regions
        boolean canPlace = regions.testState(localPlayer, Flags.BUILD);

        // If the player does not have permission to place blocks, skip this iteration
        if (!canPlace && !player.hasPermission("fp.bypass.replenish.protection")) {
            return;
        }

        Ageable ageable = (Ageable) block.getState().getBlockData();
        Collection<ItemStack> drops = block.getDrops(player.getInventory().getItemInMainHand());
        ItemStack tool = player.getInventory().getItemInMainHand();
        if (ageable.getAge() == ageable.getMaximumAge()){
            ItemUtils.setDurability(tool, player);
            for (ItemStack drop: drops){
                block.getWorld().dropItemNaturally(block.getLocation(), drop);
            }
            String sound = FarmingPlus.getPlugin().getMainConfigManager().getReplenishSoundBreak();
            if (SoundUtils.getSoundFromString(sound) != null){
                float volume = FarmingPlus.getPlugin().getMainConfigManager().getVolumeReplenishSoundBreak();
                player.getLocation().getWorld().playSound(player.getLocation(), SoundUtils.getSoundFromString(sound), volume, 1.0f);
            }
        }
        ageable.setAge(0);
        block.setBlockData(ageable);
        event.setCancelled(true);

    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void setFarmersStepCrop(PlayerInteractEvent event){
        Player player = event.getPlayer();
        Action action = event.getAction();

        if (action.equals(PHYSICAL))
            return;
        //player.sendMessage("0");
        if ((!action.equals(LEFT_CLICK_BLOCK)) && (!action.equals(LEFT_CLICK_AIR)))
            return;
        //player.sendMessage("1");
        if (player.getInventory().getItemInMainHand().getType() == Material.AIR)
            return;
        //player.sendMessage("2");
        if (!player.getInventory().getItemInMainHand().hasItemMeta())
            return;
        //player.sendMessage("3");
        boolean areBoots = false;
        for (Material boots: ItemUtils.boots){
            if (player.getInventory().getItemInMainHand().getType() == boots) {
                areBoots = true;
                break;
            }
        }
        if (!areBoots)
            return;
        //player.sendMessage("4");
        if (!player.getInventory().getItemInMainHand().getItemMeta().hasEnchant(CustomEnchantments.FARMERSTEP))
            return;
        //player.sendMessage("5");
        if (player.getGameMode() == GameMode.SPECTATOR)
            return;
        //player.sendMessage("6");

        if (action == LEFT_CLICK_BLOCK){
            player.sendMessage(MessageUtils.getColoredMessage(FarmingPlus.prefix +"&cLeft click this item while pointing the air!"));
            event.setCancelled(true);
            //player.sendMessage("7");
        }else if(action == LEFT_CLICK_AIR){
            FarmersStepGui.bootGui(player, player.getInventory().getItemInMainHand());
            //player.sendMessage("8");
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void FarmersStep(PlayerMoveEvent event){
        Player player = event.getPlayer();
        if (event.getTo().getBlockX() == event.getFrom().getBlockX() && event.getTo().getBlockY() == event.getFrom().getBlockY() && event.getTo().getBlockZ() == event.getFrom().getBlockZ())
            return; //The player hasn't moved
        if (player.getInventory().getBoots() == null)
            return;
        if (!player.getInventory().getBoots().hasItemMeta())
            return;
        if (!player.getInventory().getBoots().getItemMeta().hasEnchant(CustomEnchantments.FARMERSTEP))
            return;
        if (player.getGameMode() == GameMode.SPECTATOR)
            return;
        ItemMeta bootsMeta = player.getInventory().getBoots().getItemMeta();
        PersistentDataContainer data = bootsMeta.getPersistentDataContainer();
        if (!data.has(new NamespacedKey(FarmingPlus.getPlugin(), "crop"), PersistentDataType.STRING))
            return;

        boolean usedSetted = false;
        Location location = player.getLocation();

        List<Location> blocks = new ArrayList<>();
        int level = player.getInventory().getBoots().getItemMeta().getEnchantLevel(CustomEnchantments.FARMERSTEP);
        if (level > 3)
            level = 3;
        int yDifference = -1;
        double yFraction = location.getY() % 1;
        if (yFraction != 0) // If player is not in a complete block Ex: Slabs, Farmland, etc.
            yDifference = 0;
        blocks = LocationUtils.getRadiusBlocks(player.getLocation(), level, yDifference);

        // Get the WorldGuard instance
        WorldGuard worldGuard = WorldGuard.getInstance();
        // Get the region container
        RegionContainer regionContainer = worldGuard.getPlatform().getRegionContainer();
        // Create a query from the region container
        RegionQuery query = regionContainer.createQuery();
        // Get the WorldGuardPlugin instance for the player
        WorldGuardPlugin pluginW = WorldGuardPlugin.inst();
        // Get the LocalPlayer instance for the player
        LocalPlayer localPlayer = pluginW.wrapPlayer(player);

        // If player in CREATIVE, don't check if they have crops in inventory //
        if (player.getGameMode() == GameMode.CREATIVE || player.hasPermission("fp.bypass.farmerstep")){
            Material crop = Material.valueOf(data.get(new NamespacedKey(FarmingPlus.getPlugin(), "crop"), PersistentDataType.STRING));
            if (crop.equals(Material.POTATO)){
                crop = Material.POTATOES;
            } else if (crop.equals(Material.CARROT)) {
                crop = Material.CARROTS;
            }else if (crop.equals(Material.BEETROOT_SEEDS)) {
                crop = Material.BEETROOTS;
            }else if (crop.equals(Material.WHEAT_SEEDS)) {
                crop = Material.WHEAT;
            }else if (crop.equals(Material.MELON_SEEDS)) {
                crop = Material.MELON_STEM;
            }else if (crop.equals(Material.PUMPKIN_SEEDS)) {
                crop = Material.PUMPKIN_STEM;
            }
            for (Location block: blocks){

                // Get all regions at the block's location
                ApplicableRegionSet regions = query.getApplicableRegions(BukkitAdapter.adapt(block));
                // Check if the player has permission to place blocks in these regions
                boolean canPlace = regions.testState(localPlayer, Flags.BUILD);

                // If the player does not have permission to place blocks, skip this iteration
                if (!canPlace && !player.hasPermission("fp.bypass.farmerstep.protection")) {
                    continue;
                }

                if (block.getBlock().getType() == Material.FARMLAND && !crop.equals(Material.NETHER_WART)){
                    block.setY(block.getY() + 1);
                    if (block.getBlock().getType() == Material.AIR){
                        block.getBlock().setType(crop);
                        if (!usedSetted)
                            if (player.getGameMode() != GameMode.CREATIVE) {
                                ItemUtils.setDurabilityBoots(player.getInventory().getBoots(), player);
                                usedSetted = true;
                            }
                    }
                }else if (crop.equals(Material.NETHER_WART) && block.getBlock().getType() == Material.SOUL_SAND){
                    block.setY(block.getY() + 1);
                    if (block.getBlock().getType() == Material.AIR) {
                        block.getBlock().setType(crop);
                        if (usedSetted)
                            if (player.getGameMode() != GameMode.CREATIVE) {
                                ItemUtils.setDurabilityBoots(player.getInventory().getBoots(), player);
                                usedSetted = true;
                            }
                    }
                }

            }
        }else{
            // If player in SURVIVAL, check if they have crops in inventory //
            if (!player.getInventory().contains(Material.valueOf(data.get(new NamespacedKey(FarmingPlus.getPlugin(), "crop"), PersistentDataType.STRING))))
                return;
            Material crop = Material.valueOf(data.get(new NamespacedKey(FarmingPlus.getPlugin(), "crop"), PersistentDataType.STRING));
            Material cropT = crop;
            if (crop.equals(Material.POTATO)){
                cropT = Material.POTATOES;
            } else if (crop.equals(Material.CARROT)) {
                cropT = Material.CARROTS;
            }else if (crop.equals(Material.BEETROOT_SEEDS)) {
                cropT = Material.BEETROOTS;
            }else if (crop.equals(Material.WHEAT_SEEDS)) {
                cropT = Material.WHEAT;
            }else if (crop.equals(Material.MELON_SEEDS)) {
                cropT = Material.MELON_STEM;
            }else if (crop.equals(Material.PUMPKIN_SEEDS)) {
                cropT = Material.PUMPKIN_STEM;
            }
            for (Location block: blocks){
                

                // Get all regions at the block's location
                ApplicableRegionSet regions = query.getApplicableRegions(BukkitAdapter.adapt(block));
                // Check if the player has permission to place blocks in these regions
                boolean canPlace = regions.testState(localPlayer, Flags.BUILD);

                // If the player does not have permission to place blocks, skip this iteration
                if (!canPlace && !player.hasPermission("fp.bypass.farmerstep.protection")) {
                    continue;
                }

                if (block.getBlock().getType() == Material.FARMLAND && !cropT.equals(Material.NETHER_WART)){
                    if (player.getInventory().contains(crop)) {
                        block.setY(block.getY() + 1);
                        if (block.getBlock().getType() == Material.AIR) {
                            block.getBlock().setType(cropT);
                            ItemStack item = new ItemStack(crop, 1);
                            player.getInventory().removeItem(item);
                            if (!usedSetted){
                                ItemUtils.setDurabilityBoots(player.getInventory().getBoots(), player);
                                usedSetted = true;
                            }
                        }
                    }else
                        break;


                }else if (cropT.equals(Material.NETHER_WART) && block.getBlock().getType() == Material.SOUL_SAND){
                    if (player.getInventory().contains(crop)) {
                        block.setY(block.getY() + 1);
                        if (block.getBlock().getType() == Material.AIR) {
                            block.getBlock().setType(cropT);
                            ItemStack item = new ItemStack(crop, 1);
                            player.getInventory().removeItem(item);
                            if (!usedSetted){
                                ItemUtils.setDurabilityBoots(player.getInventory().getBoots(), player);
                                usedSetted = true;
                            }
                        }
                    }else
                        break;

                }
            }
        }
    }

    @EventHandler(priority = EventPriority.LOW)
    public void GrandTilling(PlayerInteractEvent event){
        Player player = event.getPlayer();
        boolean isHoe = false;
        for (Material hoe: ItemUtils.hoes){
            if (event.getPlayer().getInventory().getItemInMainHand().getType() == hoe){
                isHoe = true;
                break;
            }
        }
        if (!isHoe)
            return;
        if (!event.getPlayer().getInventory().getItemInMainHand().hasItemMeta())
            return;
        if (!event.getPlayer().getInventory().getItemInMainHand().getItemMeta().hasEnchant(CustomEnchantments.GRANDTILLING))
            return;
        if (event.getPlayer().getGameMode() == GameMode.SPECTATOR)
            return;
        if (!(event.getAction() == RIGHT_CLICK_BLOCK))
            return;
        if (event.getClickedBlock() == null)
            return;
        if (!(event.getClickedBlock().getType() == Material.GRASS_BLOCK || event.getClickedBlock().getType() == Material.DIRT || event.getClickedBlock().getType() == Material.DIRT_PATH))
            return;
        int level = event.getPlayer().getInventory().getItemInMainHand().getItemMeta().getEnchantLevel(CustomEnchantments.GRANDTILLING);
        if (level > 3)
            level = 3;

        // Get the WorldGuard instance
        WorldGuard worldGuard = WorldGuard.getInstance();
        // Get the region container
        RegionContainer regionContainer = worldGuard.getPlatform().getRegionContainer();
        // Create a query from the region container
        RegionQuery query = regionContainer.createQuery();
        // Get the WorldGuardPlugin instance for the player
        WorldGuardPlugin pluginW = WorldGuardPlugin.inst();
        // Get the LocalPlayer instance for the player
        LocalPlayer localPlayer = pluginW.wrapPlayer(player);

        switch (level){
            case 1, 2:
                List<Location> blocksR = LocationUtils.getRadiusBlocks(event.getClickedBlock().getLocation(), level, 0);
                for (Location block: blocksR){

                    // Get all regions at the block's location
                    ApplicableRegionSet regions = query.getApplicableRegions(BukkitAdapter.adapt(block));
                    // Check if the player has permission to place blocks in these regions
                    boolean canPlace = regions.testState(localPlayer, Flags.BUILD);

                    // If the player does not have permission to place blocks, skip this iteration
                    if (!canPlace && !player.hasPermission("fp.bypass.grandtilling.protection")) {
                        continue;
                    }

                    if (block.getBlock().getType() == Material.GRASS_BLOCK || block.getBlock().getType() == Material.DIRT || block.getBlock().getType() == Material.DIRT_PATH || block.getBlock().getType() == Material.FARMLAND){
                        block.getBlock().setType(Material.FARMLAND);
                    }
                }
                ItemUtils.setDurability(event.getPlayer().getInventory().getItemInMainHand(), event.getPlayer());
                break;
            case 3:
                List<Location> blocksL = LocationUtils.getRowBlocks(event.getClickedBlock().getLocation(), FarmingPlus.getPlugin().getMainConfigManager().getGrandTilling3Blocks(), LocationUtils.getCardinalDirection(player), 0);
                for (Location block: blocksL){

                    // Get all regions at the block's location
                    ApplicableRegionSet regions = query.getApplicableRegions(BukkitAdapter.adapt(block));
                    // Check if the player has permission to place blocks in these regions
                    boolean canPlace = regions.testState(localPlayer, Flags.BUILD);

                    // If the player does not have permission to place blocks, skip this iteration
                    if (!canPlace && !player.hasPermission("fp.bypass.grandtilling.protection")) {
                        break;
                    }

                    if (block.getBlock().getType() == Material.GRASS_BLOCK || block.getBlock().getType() == Material.DIRT || block.getBlock().getType() == Material.DIRT_PATH){
                        block.getBlock().setType(Material.FARMLAND);
                    }else if(block.getBlock().getType() == Material.FARMLAND){
                        continue;
                    }else
                        break;
                }
                ItemUtils.setDurability(event.getPlayer().getInventory().getItemInMainHand(), event.getPlayer());
                break;
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void FarmersGrace(PlayerInteractEvent event){
        if (event.getPlayer().getInventory().getBoots() == null)
            return;
        if (!event.getPlayer().getInventory().getBoots().hasItemMeta())
            return;
        if (!event.getPlayer().getInventory().getBoots().getItemMeta().hasEnchant(CustomEnchantments.FARMERSGRACE))
            return;
        if (event.getPlayer().getGameMode() == GameMode.SPECTATOR)
            return;
        if (!(event.getAction() == PHYSICAL))
            return;
        if (event.getClickedBlock() == null)
            return;
        if (event.getClickedBlock().getType() == Material.FARMLAND)
            event.setCancelled(true);
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void Delicate(BlockBreakEvent event){
        if (event.getPlayer().getInventory().getItemInMainHand().getType() == Material.AIR)
            return;
        if (!event.getPlayer().getInventory().getItemInMainHand().hasItemMeta())
            return;
        if (!event.getPlayer().getInventory().getItemInMainHand().getItemMeta().hasEnchant(CustomEnchantments.DELICATE))
            return;
        if (event.getPlayer().getGameMode() != GameMode.SURVIVAL)
            return;
        Block block = event.getBlock();
        if (block.getState() instanceof Container)
            return;
        if (!(block.getType().equals(Material.MELON_STEM) || block.getType().equals(Material.PUMPKIN_STEM) || block.getType().equals(Material.ATTACHED_MELON_STEM) || block.getType().equals(Material.ATTACHED_PUMPKIN_STEM)))
            return;
        event.setCancelled(true);
    }

    @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
    public void Irrigate(PlayerInteractEvent event){
        Player player = event.getPlayer();
        if (!(event.getPlayer().getInventory().getItemInMainHand().getType().equals(Material.WATER_BUCKET)))
            return;
        if (!event.getPlayer().getInventory().getItemInMainHand().hasItemMeta())
            return;
        if (!event.getPlayer().getInventory().getItemInMainHand().getItemMeta().hasEnchant(CustomEnchantments.IRRIGATE))
            return;
        if (event.getPlayer().getGameMode() == GameMode.SPECTATOR)
            return;
        if (!(event.getAction() == RIGHT_CLICK_BLOCK))
            return;
        Block blockC = event.getClickedBlock();
        if (blockC == null)
            return;
        if (blockC.getRelative(BlockFace.UP).getType() != Material.AIR  && event.getClickedBlock().getRelative(BlockFace.UP).getType() != Material.WATER)
            return;
        if (blockC.getState() instanceof Container)
            return;
        if (blockC.getType().isInteractable())
            return;

        event.setCancelled(true);

        // Get the WorldGuard instance
        WorldGuard worldGuard = WorldGuard.getInstance();
        // Get the region container
        RegionContainer regionContainer = worldGuard.getPlatform().getRegionContainer();
        // Create a query from the region container
        RegionQuery query = regionContainer.createQuery();
        // Get the WorldGuardPlugin instance for the player
        WorldGuardPlugin pluginW = WorldGuardPlugin.inst();
        // Get the LocalPlayer instance for the player
        LocalPlayer localPlayer = pluginW.wrapPlayer(player);

        BlockFace blockFace = event.getBlockFace();

        List<Location> blocks = LocationUtils.getRowBlocks(event.getClickedBlock().getRelative(blockFace).getLocation(), FarmingPlus.getPlugin().getMainConfigManager().getIrrigateMaxBlocks(), LocationUtils.getCardinalDirection(player), 0);
        for (Location block : blocks){
            // Get all regions at the block's location
            ApplicableRegionSet regions = query.getApplicableRegions(BukkitAdapter.adapt(block));
            // Check if the player has permission to place blocks in these regions
            boolean canPlace = regions.testState(localPlayer, Flags.BUILD);

            // If the player does not have permission to place blocks, skip this iteration
            if (!canPlace && !player.hasPermission("fp.bypass.irrigate.protection")) {
                break;
            }
            if (block.getBlock().getType() == Material.AIR || block.getBlock().getType() == Material.WATER)
                block.getBlock().setType(Material.WATER);
            else
                break;
        }

    }
}
