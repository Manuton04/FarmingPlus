package fp.manuton.events;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
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
import org.bukkit.plugin.Plugin;

import java.util.*;

import static org.bukkit.event.block.Action.*;

//
public class PlayerListener implements Listener {

    private boolean playerHasItemInInventory(Player player, Material item) {
        return player.getInventory().contains(item, 1);
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void autoPickup(BlockBreakEvent event){
        Player player = event.getPlayer();
        Block block = event.getBlock();
        if (player.getGameMode() == GameMode.CREATIVE || player.getGameMode() == GameMode.SPECTATOR)
            return;
        if (player.getInventory().getItemInMainHand().getType() != Material.AIR) {
            if (ItemUtils.hasCustomEnchant(player.getInventory().getItemInMainHand(), CustomEnchantments.REPLENISH) && ItemUtils.getCrop(block.getType()) != null)
                return;
        }
        if (ItemUtils.getCrop(block.getType()) == null && block.getType() != Material.PUMPKIN && block.getType() != Material.MELON && block.getType() != Material.PUMPKIN_STEM && block.getType() != Material.MELON_STEM && block.getType() != Material.SUGAR_CANE && block.getType() != Material.CACTUS && block.getType() != Material.KELP && block.getType() != Material.KELP_PLANT && block.getType() != Material.BAMBOO && block.getType() != Material.BAMBOO_SAPLING && block.getType() != Material.SWEET_BERRY_BUSH)
            return;
        if (player.hasPermission("fp.autopickup")){
            Collection<ItemStack> drops = block.getDrops(player.getInventory().getItemInMainHand());
            if (block.getType() == Material.SUGAR_CANE || block.getType() == Material.CACTUS || block.getType() == Material.KELP || block.getType() == Material.KELP_PLANT || block.getType() == Material.BAMBOO){
                while (block.getRelative(BlockFace.UP).getType() == block.getType()){
                    block.setType(Material.AIR);
                    block = block.getRelative(BlockFace.UP);
                    drops.addAll(block.getDrops(player.getInventory().getItemInMainHand()));
                }
            }
            for (ItemStack drop : drops) {
                if (!(player.getInventory().firstEmpty() == -1))
                    player.getInventory().addItem(drop);
                else
                    player.getWorld().dropItemNaturally(player.getLocation(), drop);
            }
            block.setType(Material.AIR);
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void Replenish(BlockBreakEvent event){
        if (event.getPlayer().getInventory().getItemInMainHand().getType() == Material.AIR)
            return;
        if (!event.getPlayer().getInventory().getItemInMainHand().hasItemMeta())
            return;
        if (!ItemUtils.hasCustomEnchant(event.getPlayer().getInventory().getItemInMainHand(), CustomEnchantments.REPLENISH))
            return;
        if (event.getPlayer().getGameMode() == GameMode.CREATIVE || event.getPlayer().getGameMode() == GameMode.SPECTATOR)
            return;
        Block block = event.getBlock();
        if (block.getState() instanceof Container)
            return;
        if (ItemUtils.getCrop(block.getType()) == null)
            return;

        Player player = event.getPlayer();


        Ageable ageable = (Ageable) block.getState().getBlockData();
        Collection<ItemStack> drops = block.getDrops(player.getInventory().getItemInMainHand());
        ItemStack tool = player.getInventory().getItemInMainHand();

        if (drops.isEmpty())
            return;

        if (ageable.getAge() == ageable.getMaximumAge()){
            Material cropType = block.getType();

            ItemUtils.setDurability(tool, player);
            String sound = FarmingPlus.getPlugin().getMainConfigManager().getReplenishSoundBreak();
            if (SoundUtils.getSoundFromString(sound) != null){
                float volume = FarmingPlus.getPlugin().getMainConfigManager().getVolumeReplenishSoundBreak();
                player.getLocation().getWorld().playSound(player.getLocation(), SoundUtils.getSoundFromString(sound), volume, 1.0f);
            }

            ItemStack toRemove = new ItemStack(ItemUtils.getCrop(cropType), 1);
            for (Iterator<ItemStack> iterator = drops.iterator(); iterator.hasNext();) {
                ItemStack drop = iterator.next();
                if (drop.isSimilar(toRemove)) {
                    drop.setAmount(drop.getAmount() - 1);
                    if (drop.getAmount() <= 0) {
                        iterator.remove();
                    }
                    break;
                }
            }

            if (player.hasPermission("fp.autopickup")){
                for (ItemStack drop : drops) {
                    if (!(player.getInventory().firstEmpty() == -1))
                        player.getInventory().addItem(drop);
                    else
                        player.getWorld().dropItemNaturally(player.getLocation(), drop);
                }

            }else {
                for (ItemStack drop : drops) {
                    block.getWorld().dropItemNaturally(block.getLocation(), drop);
                }
            }

            Location particleLocation = event.getBlock().getLocation();
            particleLocation.add(0.5, 0, 0.5);
            try {
                player.getWorld().spawnParticle(Particle.valueOf("HAPPY_VILLAGER"), particleLocation, 1);
            } catch (Exception e) {
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
        if (!ItemUtils.hasCustomEnchant(player.getInventory().getItemInMainHand(), CustomEnchantments.FARMERSTEP))
            return;
        //player.sendMessage("5");
        if (player.getGameMode() == GameMode.SPECTATOR)
            return;
        //player.sendMessage("6");

        if (action == LEFT_CLICK_BLOCK){
            player.sendMessage(MessageUtils.translateAll(player, FarmingPlus.getPlugin().getMainConfigManager().getLeftClickAir()));
            event.setCancelled(true);
            //player.sendMessage("7");
        }else if(action == LEFT_CLICK_AIR){
            FarmersStepGui.bootGui(player, player.getInventory().getItemInMainHand());
            //player.sendMessage("8");
        }
    }

    @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
    public void FarmersStep(PlayerMoveEvent event){
        Player player = event.getPlayer();
        if (event.getTo().getBlockX() == event.getFrom().getBlockX() && event.getTo().getBlockY() == event.getFrom().getBlockY() && event.getTo().getBlockZ() == event.getFrom().getBlockZ())
            return; //The player hasn't moved
        if (player.getInventory().getBoots() == null)
            return;
        if (!player.getInventory().getBoots().hasItemMeta())
            return;
        if (!ItemUtils.hasCustomEnchant(player.getInventory().getBoots(), CustomEnchantments.FARMERSTEP))
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
        int level = ItemUtils.getCustomEnchantLevel(player.getInventory().getBoots());
        if (level > 3)
            level = 3;
        int yDifference = -1;
        double yFraction = location.getY() % 1;
        if (yFraction != 0) // If player is not in a complete block Ex: Slabs, Farmland, etc.
            yDifference = 0;
        blocks = LocationUtils.getRadiusBlocks(player.getLocation(), level, yDifference);


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
        if (!ItemUtils.hasCustomEnchant(player.getInventory().getItemInMainHand(), CustomEnchantments.GRANDTILLING))
            return;
        if (event.getPlayer().getGameMode() == GameMode.SPECTATOR)
            return;
        if (!(event.getAction() == RIGHT_CLICK_BLOCK))
            return;
        if (event.getClickedBlock() == null)
            return;
        if (!(event.getClickedBlock().getType() == Material.GRASS_BLOCK || event.getClickedBlock().getType() == Material.DIRT || event.getClickedBlock().getType() == Material.DIRT_PATH))
            return;
        int level = ItemUtils.getCustomEnchantLevel(player.getInventory().getItemInMainHand());
        if (level > 3)
            level = 3;

        switch (level){
            case 1, 2:
                List<Location> blocksR = LocationUtils.getRadiusBlocks(event.getClickedBlock().getLocation(), level, 0);
                for (Location block: blocksR){

                    if (block.getBlock().getType() == Material.GRASS_BLOCK || block.getBlock().getType() == Material.DIRT || block.getBlock().getType() == Material.DIRT_PATH || block.getBlock().getType() == Material.FARMLAND){
                        block.getBlock().setType(Material.FARMLAND);
                    }
                }
                ItemUtils.setDurability(event.getPlayer().getInventory().getItemInMainHand(), event.getPlayer());
                break;
            case 3:
                List<Location> blocksL = LocationUtils.getRowBlocks(event.getClickedBlock().getLocation(), FarmingPlus.getPlugin().getMainConfigManager().getGrandTilling3Blocks(), LocationUtils.getCardinalDirection(player), 0);
                for (Location block: blocksL){

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

    @EventHandler(priority = EventPriority.LOW)
    public void FarmersGrace(PlayerInteractEvent event){
        if (event.getPlayer().getInventory().getBoots() == null)
            return;
        if (!event.getPlayer().getInventory().getBoots().hasItemMeta())
            return;
        if (!ItemUtils.hasCustomEnchant(event.getPlayer().getInventory().getBoots(), CustomEnchantments.FARMERSGRACE))
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

    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    public void Delicate(BlockBreakEvent event){
        if (event.getPlayer().getInventory().getItemInMainHand().getType() == Material.AIR)
            return;
        if (!event.getPlayer().getInventory().getItemInMainHand().hasItemMeta())
            return;
        if (!ItemUtils.hasCustomEnchant(event.getPlayer().getInventory().getItemInMainHand(), CustomEnchantments.DELICATE))
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

    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    public void Irrigate(PlayerInteractEvent event){
        Player player = event.getPlayer();
        if (!(event.getPlayer().getInventory().getItemInMainHand().getType().equals(Material.WATER_BUCKET)))
            return;
        if (!event.getPlayer().getInventory().getItemInMainHand().hasItemMeta())
            return;
        if (!ItemUtils.hasCustomEnchant(player.getInventory().getItemInMainHand(), CustomEnchantments.IRRIGATE))
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


        BlockFace blockFace = event.getBlockFace();

        List<Location> blocks = LocationUtils.getRowBlocks(event.getClickedBlock().getRelative(blockFace).getLocation(), FarmingPlus.getPlugin().getMainConfigManager().getIrrigateMaxBlocks(), LocationUtils.getCardinalDirection(player), 0);
        for (Location block : blocks){
            if (block.getBlock().getType() == Material.AIR || block.getBlock().getType() == Material.WATER)
                block.getBlock().setType(Material.WATER);
            else
                break;
        }

    }
}
