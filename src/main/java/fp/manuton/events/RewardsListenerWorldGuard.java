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
import fp.manuton.SQL.MySQLData;
import fp.manuton.rewards.MoneyReward;
import fp.manuton.rewards.Reward;
import fp.manuton.rewardsCounter.RewardRecord;
import fp.manuton.rewardsCounter.RewardsCounter;
import fp.manuton.utils.ItemUtils;
import fp.manuton.utils.MessageUtils;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Container;
import org.bukkit.block.data.Ageable;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

import java.util.ArrayList;
import java.util.Date;
import java.util.Map;
import java.util.UUID;

public class RewardsListenerWorldGuard implements Listener {

    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    public void Rewards(BlockBreakEvent event){
        if (!FarmingPlus.getPlugin().getMainConfigManager().getEnabledRewards())
            return;
        Player player = event.getPlayer();
        if (player.getGameMode() != GameMode.SURVIVAL)
            return;
        Block block = event.getBlock();
        if (block.getLocation().equals(block))
            return;
        if (block.getState() instanceof Container)
            return;
        boolean isType = false;
        for (Material type : ItemUtils.cropsR){
            if (block.getType() == type) {
                isType = true;
                break;
            }
        }
        if (!isType)
            return;
        if (block.getType() != Material.CACTUS && block.getType() != Material.SUGAR_CANE)
            if (block.getState().getBlockData() instanceof Ageable) {
                Ageable ageable = (Ageable) block.getState().getBlockData();
                if (ageable.getAge() != ageable.getMaximumAge())
                    return;
            }


        boolean canPlace = true;
        if (Bukkit.getPluginManager().getPlugin("WorldGuard") != null){
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
            canPlace = regions.testState(localPlayer, Flags.BUILD);
        }


        // If the player does not have permission to place blocks, skip this iteration
        if (Bukkit.getPluginManager().getPlugin("WorldGuard") != null && !canPlace && !player.hasPermission("fp.bypass.replenish.protection")) {
            return;
        }

        boolean rewardGiven = false;

        for(Reward reward : FarmingPlus.getPlugin().getMainConfigManager().getAllRewards()) {
            if (rewardGiven) break;
            if (reward.getCrops() == null)
                continue;
            if (reward.getChance() == 0)
                continue;

            for (String crop : reward.getCrops()){
                if (rewardGiven) break;

                if (crop.contains(block.getType().toString()) || crop.equals("ALL")) {
                    int amount = 1;
                    if (block.getType().equals(Material.SUGAR_CANE) || block.getType().equals(Material.CACTUS))
                        while (block.getRelative(BlockFace.UP).getType() == block.getType()) {
                            block = block.getRelative(BlockFace.UP);
                            amount++;
                        }

                    for (int i = 0; i < amount; i++) {
                        if (rewardGiven) break;

                        if (Math.random() <= reward.getChance()) {
                            if (reward instanceof MoneyReward) {
                                if (Bukkit.getPluginManager().getPlugin("Vault") == null) {
                                    Bukkit.getConsoleSender().sendMessage(MessageUtils.translateAll(null, FarmingPlus.prefix + " &cYou need Vault to use this reward."));
                                    return;
                                }
                            }

                            reward.give(player);
                            rewardGiven = true;
                            if (FarmingPlus.isMySQLConnected()) {
                                UUID playerId = player.getUniqueId();
                                RewardRecord rewardRecord = new RewardRecord(new Date(), FarmingPlus.getPlugin().getMainConfigManager().getKeyFromReward(reward));
                                MySQLData.saveRewardCounterToDatabase(FarmingPlus.getConnectionMySQL(), playerId, rewardRecord);
                            }
                            Map<UUID, RewardsCounter> rewardsCounterMap = FarmingPlus.getPlugin().getMainConfigManager().getRewardsCounterMap();
                            UUID playerId = player.getUniqueId();
                            RewardsCounter rewardsCounter = new RewardsCounter(new ArrayList<>());
                            if (!rewardsCounterMap.containsKey(playerId))
                                rewardsCounterMap.put(playerId, rewardsCounter);
                            rewardsCounter = rewardsCounterMap.get(playerId);
                            rewardsCounter.addRecord(FarmingPlus.getPlugin().getMainConfigManager().getKeyFromReward(reward));
                            break;
                        }
                    }
                }
            }
        }

    }
}
