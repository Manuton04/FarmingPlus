package fp.manuton.events;

import fp.manuton.FarmingPlus;
import fp.manuton.rewards.CommandReward;
import fp.manuton.rewards.ItemReward;
import fp.manuton.rewards.MoneyReward;
import fp.manuton.rewards.Reward;
import fp.manuton.utils.ItemUtils;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Container;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

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

        for(Reward reward : FarmingPlus.getPlugin().getMainConfigManager().getAllRewards()) {
            if (reward.getCrops() == null)
                continue;
            if (reward.getChance() == 0)
                continue;
            if (reward.getCrops().contains(block.getType().toString())){
                if (reward instanceof CommandReward){

                }else if (reward instanceof ItemReward){

                }else if (reward instanceof MoneyReward){

                }


                if (Math.random() <= reward.getChance()){
                    reward.give(player);
                    break;
                }
            }
        }

    }
}
