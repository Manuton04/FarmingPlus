package fp.manuton.events;

import fp.manuton.FarmingPlus;
import fp.manuton.SQL.MySQLData;
import fp.manuton.protection.ProtectionManager;
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
import org.bukkit.inventory.ItemStack;

import java.sql.Connection;
import java.util.*;

public class RewardsListener implements Listener {

    private final ProtectionManager protectionManager;

    public RewardsListener(ProtectionManager protectionManager) {
        this.protectionManager = protectionManager;
    }

    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    public void Rewards(BlockBreakEvent event){
        if (!FarmingPlus.getPlugin().getMainConfigManager().getEnabledRewards())
            return;
        Player player = event.getPlayer();
        if (player.getGameMode() == GameMode.CREATIVE)
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

        // Protection check - don't give rewards for blocks in protected regions
        if (!protectionManager.canBuild(player, block.getLocation())
                && !player.hasPermission("fp.bypass.replenish.protection")
                && !(player.isOp() && FarmingPlus.getPlugin().getMainConfigManager().getEnabledDefaultOpPerms())) {
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

                                // CRITICAL: Run database operation asynchronously to prevent server lag
                                // BlockBreakEvent runs on main thread - DB ops must be async!
                                Bukkit.getScheduler().runTaskAsynchronously(FarmingPlus.getPlugin(), () -> {
                                    try (Connection conn = FarmingPlus.getConnectionMySQL()) {
                                        if (conn != null) {
                                            MySQLData.saveRewardCounterToDatabase(conn, playerId, rewardRecord);
                                        }
                                    } catch (Exception e) {
                                        Bukkit.getLogger().warning("[FarmingPlus] Failed to save reward asynchronously: " + e.getMessage());
                                    }
                                });
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
