package fp.manuton.events;

import fp.manuton.FarmingPlus;
import fp.manuton.rewards.Reward;
import fp.manuton.utils.ItemUtils;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Container;
import org.bukkit.block.data.Ageable;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Collection;

public class RewardsListener implements Listener {

    @EventHandler(priority = EventPriority.LOWEST)
    public void Rewards(BlockBreakEvent event){
        Player player = event.getPlayer();
        if (player.getGameMode() != GameMode.SURVIVAL)
            return;
        Block block = event.getBlock();
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

        boolean rewardGiven = false;

        for(Reward reward : FarmingPlus.getPlugin().getMainConfigManager().getAllRewards()) {
            if (rewardGiven) break;
            if (reward.getCrops() == null)
                continue;
            if (reward.getChance() == 0)
                continue;

            for (String crop : reward.getCrops()){
                if (crop.contains(block.getType().toString()) || crop.equals("ALL")){
                    if (Math.random() <= reward.getChance()){
                        reward.give(player);
                        rewardGiven = true;
                        break;
                    }
                }
            }
        }

    }
}
