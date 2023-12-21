package fp.manuton.events;

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
    }

}
