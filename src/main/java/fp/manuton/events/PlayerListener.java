package fp.manuton.events;

import fp.manuton.FarmingPlus;
import fp.manuton.enchantments.CustomEnchantments;
import fp.manuton.utils.ItemUtils;
import fp.manuton.utils.MessageUtils;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Container;
import org.bukkit.block.data.Ageable;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Collection;
import java.util.Objects;

import static org.bukkit.event.block.Action.*;

//
public class PlayerListener implements Listener {

    private FarmingPlus plugin;

    public PlayerListener(FarmingPlus plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
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
        Ageable ageable = (Ageable) block.getState().getBlockData();
        Collection<ItemStack> drops = block.getDrops(player.getInventory().getItemInMainHand());
        ItemStack tool = player.getInventory().getItemInMainHand();
        if (ageable.getAge() == ageable.getMaximumAge()){
            ItemUtils.setDurability(tool, player);
            for (ItemStack drop: drops){
                block.getWorld().dropItemNaturally(block.getLocation(), drop);
            }
        }
        ageable.setAge(0);
        block.setBlockData(ageable);
        event.setCancelled(true);

    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void setFarmersStepCrop(PlayerInteractEvent event){
        if (event.getPlayer().getInventory().getItemInMainHand().getType() == Material.AIR)
            return;
        if (!event.getPlayer().getInventory().getItemInMainHand().hasItemMeta())
            return;
        if (!event.getPlayer().getInventory().getItemInMainHand().getItemMeta().hasEnchant(CustomEnchantments.FARMERSTEP))
            return;
        if (event.getPlayer().getGameMode() == GameMode.SPECTATOR)
            return;
        if (!(event.getAction() == Action.LEFT_CLICK_AIR || event.getAction() == Action.LEFT_CLICK_BLOCK))
            return;

        Player player = event.getPlayer();

        if (event.getAction() == LEFT_CLICK_BLOCK){
            player.sendMessage(MessageUtils.getColoredMessage(FarmingPlus.prefix +"&cLeft click this item while pointing the air!"));
        }else if (event.getAction() == LEFT_CLICK_AIR){

        }


    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void FarmersStep(PlayerMoveEvent event){
        if (event.getPlayer().getInventory().getBoots() == null)
            return;
        if (!event.getPlayer().getInventory().getBoots().hasItemMeta())
            return;
        if (!event.getPlayer().getInventory().getBoots().getItemMeta().hasEnchant(CustomEnchantments.FARMERSTEP))
            return;
        if (event.getPlayer().getGameMode() == GameMode.SPECTATOR)
            return;

        Player player = event.getPlayer();
        Location xyz = event.getTo();

        // If player in CREATIVE, don't check if they have crops in inventory //
        if (event.getPlayer().getGameMode() == GameMode.CREATIVE){

        }else{

        }


    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void FarmersGrace(PlayerInteractEvent event){
        if (event.getPlayer().getInventory().getBoots() == null)
            return;
        if (!event.getPlayer().getInventory().getBoots().hasItemMeta())
            return;
        if (!event.getPlayer().getInventory().getBoots().getItemMeta().hasEnchant(CustomEnchantments.FARMERSGRACE))
            return;
        if (event.getPlayer().getGameMode() == GameMode.SPECTATOR)
            return;
        if (!(event.getAction() == Action.PHYSICAL))
            return;
        if (event.getClickedBlock() == null)
            return;
        if (event.getClickedBlock().getType() == Material.FARMLAND)
            event.setCancelled(true);
    }
}
