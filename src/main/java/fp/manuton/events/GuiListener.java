package fp.manuton.events;

import fp.manuton.FarmingPlus;
import fp.manuton.guis.EnchantGui;
import fp.manuton.utils.ItemUtils;
import fp.manuton.utils.SoundUtils;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

public class GuiListener implements Listener {

    @EventHandler
    public void onClickEn(InventoryClickEvent event){
        Player player = (Player) event.getWhoClicked();
        if (!player.hasMetadata("OpenedMenu"))
            return;
        int slot = event.getSlot();
        if (event.getClickedInventory().getType().equals(InventoryType.PLAYER)){
            ItemStack item = event.getCurrentItem();
            if (item != null || item.getType() != Material.AIR){
                if (event.getInventory().getItem(19) == null || event.getInventory().getItem(19).getType() == Material.AIR){
                    event.getInventory().setItem(19, item);
                    player.getInventory().setItem(slot, new ItemStack(Material.AIR));
                }else{
                    ItemStack item19 = event.getInventory().getItem(19);
                    event.getInventory().setItem(19, item);
                    player.getInventory().setItem(slot, new ItemStack(Material.AIR));
                    Inventory playerInventory = player.getInventory();
                    int emptySlot = playerInventory.firstEmpty();
                    if (emptySlot != -1)
                        player.getInventory().setItem(slot, item19);
                    else {
                        player.getWorld().dropItemNaturally(player.getLocation(), item19);
                    }
                }
                boolean Enchantable = false;

                for (Material type : ItemUtils.boots){
                    if (item.getType() == type) {
                        EnchantGui.enchantGuiBoots(player, event.getInventory());
                        Enchantable = true;
                    }
                }

                if (!Enchantable){
                    EnchantGui.enchantGuiEmpty(player, event.getInventory());
                }

            }
        }

        if (!(slot == 19)) {
            event.setCancelled(true);
            if (slot == 49)
                player.closeInventory();

        }else {
            ItemStack item = event.getClickedInventory().getItem(slot);
            Inventory playerInventory = player.getInventory();
            int emptySlot = playerInventory.firstEmpty();
            if (emptySlot != -1)
                player.getInventory().addItem(item);
            else {
                player.getWorld().dropItemNaturally(player.getLocation(), item);
            }
            event.getInventory().setItem(slot, null);
            EnchantGui.enchantGuiEmpty(player, event.getInventory());

        }
    }

    @EventHandler
    public void onCloseEn(InventoryCloseEvent event){
        Player player = (Player) event.getPlayer();
        if (!player.hasMetadata("OpenedMenu"))
            return;
        FarmingPlus plugin = FarmingPlus.getPlugin();

        player.removeMetadata("OpenedMenu", plugin);
        String sound = plugin.getMainConfigManager().getGuiSoundClose();
        if (SoundUtils.getSoundFromString(sound) != null) {
            float volume = plugin.getMainConfigManager().getVolumeGuiSoundClose();
            player.playSound(player.getLocation(), SoundUtils.getSoundFromString(sound), volume, 1.0f);
        }

        Inventory inventory = event.getInventory();
        if (inventory.getSize() == 54){
            ItemStack slot19 = inventory.getItem(19);

            if (slot19 == null || slot19.getType().equals(Material.AIR))
                return;
            Inventory playerInventory = player.getInventory();
            int emptySlot = playerInventory.firstEmpty();
            if (emptySlot != -1)
                player.getInventory().addItem(slot19);
            else {
                player.getWorld().dropItemNaturally(player.getLocation(), slot19);
            }

        }
    }

    @EventHandler
    public void onClickBoots(InventoryClickEvent event){
        Player player = (Player) event.getWhoClicked();
        if (!player.hasMetadata("BootsMenu"))
            return;

        event.setCancelled(true);
        int slot = event.getSlot();
        if (event.getClickedInventory().getType().equals(InventoryType.PLAYER)){
            ItemStack item = event.getCurrentItem();
            if (item != null || item.getType() != Material.AIR){
                ItemStack itemT = item.clone();
                itemT.setAmount(1);
                for (Material crops: ItemUtils.crops){
                    if (crops.equals(item.getType()))
                        event.getInventory().setItem(13, itemT);

                }
            }
        }

        if (!(slot == 13)) {
            if (slot == 31)
                player.closeInventory();
        }else
            event.getInventory().setItem(13, null);

    }

    @EventHandler
    public void onCloseBoots(InventoryCloseEvent event){
        Player player = (Player) event.getPlayer();
        if (!player.hasMetadata("BootsMenu"))
            return;

        FarmingPlus plugin = FarmingPlus.getPlugin();
        player.removeMetadata("BootsMenu", plugin);

        Inventory inventory = event.getInventory();
        if (inventory.getSize() == 36){
            ItemStack slot13 = inventory.getItem(13);
            ItemStack boots = player.getInventory().getItemInMainHand();
            ItemMeta bootsMeta = boots.getItemMeta();
            PersistentDataContainer data = bootsMeta.getPersistentDataContainer();
            if (slot13 == null || slot13.getType().equals(Material.AIR)){
                if (data.has(new NamespacedKey(FarmingPlus.getPlugin(), "crop"), PersistentDataType.STRING))
                    data.remove(new NamespacedKey(FarmingPlus.getPlugin(), "crop"));
                boots.setItemMeta(bootsMeta);
                player.sendMessage("No hay Cultivo");
                return;
            }else{
                String cropS = slot13.getType().toString();
                if (data.has(new NamespacedKey(FarmingPlus.getPlugin(), "crop"), PersistentDataType.STRING)){
                    ItemStack item = new ItemStack(Material.valueOf(data.get(new NamespacedKey(FarmingPlus.getPlugin(), "crop"), PersistentDataType.STRING)));
                    if (!item.equals(slot13)){
                        data.set(new NamespacedKey(FarmingPlus.getPlugin(), "crop"), PersistentDataType.STRING, cropS);
                        boots.setItemMeta(bootsMeta);
                        player.sendMessage("Se guardo: "+cropS);
                    }else
                        player.sendMessage("Ya tenia: "+cropS);
                }else{
                    data.set(new NamespacedKey(FarmingPlus.getPlugin(), "crop"), PersistentDataType.STRING, cropS);
                    boots.setItemMeta(bootsMeta);
                    player.sendMessage("Se guardo: "+cropS);
                }
            }

        }
    }

}
