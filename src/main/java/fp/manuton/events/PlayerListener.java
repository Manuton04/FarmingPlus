package fp.manuton.events;

import fp.manuton.FarmingPlus;
import fp.manuton.enchantments.CustomEnchantments;
import fp.manuton.guis.FarmersStepGui;
import fp.manuton.utils.ItemUtils;
import fp.manuton.utils.MessageUtils;
import fp.manuton.utils.SoundUtils;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;
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

import java.util.Collection;
import java.util.Objects;

import static org.bukkit.event.block.Action.*;

//
public class PlayerListener implements Listener {

    @EventHandler(priority = EventPriority.LOWEST)
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

    @EventHandler(priority = EventPriority.LOWEST)
    public void setFarmersStepCrop(PlayerInteractEvent event){
        Player player = event.getPlayer();
        Action action = event.getAction();

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
            player.sendMessage("7");
        }else if(action == LEFT_CLICK_AIR){
            FarmersStepGui.bootGui(player, player.getInventory().getItemInMainHand());
            player.sendMessage("8");
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void FarmersStep(PlayerMoveEvent event){
        Player player = event.getPlayer();
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


        Location xyz = event.getTo();

        player.sendMessage("Tiene Persistent");

        // If player in CREATIVE, don't check if they have crops in inventory //
        if (event.getPlayer().getGameMode() == GameMode.CREATIVE){

        }else{

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
}
