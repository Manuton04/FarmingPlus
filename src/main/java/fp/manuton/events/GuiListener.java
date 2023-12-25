package fp.manuton.events;

import fp.manuton.FarmingPlus;
import fp.manuton.guis.EnchantGui;
import fp.manuton.utils.ItemUtils;
import fp.manuton.utils.MessageUtils;
import fp.manuton.utils.SoundUtils;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Item;
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
        if (event.getClickedInventory() == null)
            return;
        if (!player.hasMetadata("OpenedMenu"))
            return;
        int slot = event.getSlot();
        if (event.getClickedInventory().getType().equals(InventoryType.PLAYER)){
            ItemStack item;
            try {
                item = event.getCurrentItem();
            }catch (NullPointerException e){
                item = null;
            }
            if (item != null && !player.hasMetadata("menuConfirm")){
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
                        player.getInventory().setItem(emptySlot, item);
                    else {
                        player.getWorld().dropItemNaturally(player.getLocation(), item19);
                    }
                }
                boolean Enchantable = false;

                if (item.getType() == Material.WATER_BUCKET){
                    EnchantGui.enchantGuiWater(player, event.getInventory());
                    Enchantable = true;
                }
                for (Material type : ItemUtils.boots){
                    if (item.getType() == type) {
                        EnchantGui.enchantGuiBoots(player, event.getInventory());
                        Enchantable = true;
                    }
                }
                for (Material type : ItemUtils.hoes){
                    if (item.getType() == type) {
                        EnchantGui.enchantGuiHoes(player, event.getInventory());
                        Enchantable = true;
                    }
                }
                for (Material type : ItemUtils.axes){
                    if (item.getType() == type) {
                        EnchantGui.enchantGuiAxes(player, event.getInventory());
                        Enchantable = true;
                    }
                }

                if (!Enchantable){
                    EnchantGui.enchantGuiEmpty(player, event.getInventory());
                }

            }
        }else{
            if (!(slot == 19) && !(slot == 12 && player.hasMetadata("menuConfirm"))){
                event.setCancelled(true);
                if (slot == 49){
                    player.closeInventory();
                    return;
                }


                ItemStack enchant;
                try {
                    enchant = event.getInventory().getItem(slot);
                }catch (NullPointerException e){
                    enchant = null;
                }

                ItemStack item;
                try {
                    item = event.getInventory().getItem(19);
                }catch (NullPointerException e){
                    item = null;
                }

                if (enchant != null && item != null){
                    PersistentDataContainer data = enchant.getItemMeta().getPersistentDataContainer();
                    if (data.has(new NamespacedKey(FarmingPlus.getPlugin(), "menuItem"), PersistentDataType.STRING)){
                        if (enchant.getItemMeta().getDisplayName().equals(MessageUtils.getColoredMessage(FarmingPlus.getPlugin().getMainConfigManager().getFarmerstepName()))){
                            // code
                        }else if (enchant.getItemMeta().getDisplayName().equals(MessageUtils.getColoredMessage(FarmingPlus.getPlugin().getMainConfigManager().getGrandtillingName()))) {
                            // code
                        }else if (enchant.getItemMeta().getDisplayName().equals(MessageUtils.getColoredMessage(FarmingPlus.getPlugin().getMainConfigManager().getFarmersgraceName()))) {
                            EnchantGui.enchantGuiConfirm(player, event.getInventory(), enchant, item);
                        }else if (enchant.getItemMeta().getDisplayName().equals(MessageUtils.getColoredMessage(FarmingPlus.getPlugin().getMainConfigManager().getIrrigateName()))) {
                            // code
                        }else if (enchant.getItemMeta().getDisplayName().equals(MessageUtils.getColoredMessage(FarmingPlus.getPlugin().getMainConfigManager().getDelicateName()))) {
                            // code
                        }else if (enchant.getItemMeta().getDisplayName().equals(MessageUtils.getColoredMessage(FarmingPlus.getPlugin().getMainConfigManager().getReplenishName()))) {
                            // code
                        }
                    }else if (data.has(new NamespacedKey(FarmingPlus.getPlugin(), "menuConfirmItem"), PersistentDataType.STRING)){
                        if (item.getType() == Material.valueOf(FarmingPlus.getPlugin().getMainConfigManager().getGuiConfirm())){

                        }if (item.getType() == Material.valueOf(FarmingPlus.getPlugin().getMainConfigManager().getGuiCancel())){

                        }
                    }
                }

            }else {

                Inventory inventory = event.getInventory();
                if (player.hasMetadata("menuConfirm")){
                    slot = 12;
                    player.removeMetadata("menuConfirm", FarmingPlus.getPlugin());
                }else
                    slot = 19;

                ItemStack item;
                try{
                    item = event.getClickedInventory().getItem(slot);
                }catch (NullPointerException e) {
                    item = null;
                }

                if (item != null){
                    Inventory playerInventory = player.getInventory();
                    int emptySlot = playerInventory.firstEmpty();
                    if (emptySlot != -1)
                        player.getInventory().setItem(emptySlot, item);
                    else {
                        player.getWorld().dropItemNaturally(player.getLocation(), item);
                    }
                    if (slot == 12)
                        EnchantGui.enchantGuiChange(player, inventory);
                    event.getInventory().setItem(19, null);
                    EnchantGui.enchantGuiEmpty(player, event.getInventory());
                }
            }
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
            ItemStack slot;
            if (player.hasMetadata("menuConfirm")){
                slot = inventory.getItem(12);
                player.removeMetadata("menuConfirm", plugin);
            }else
                slot = inventory.getItem(19);

            if (slot == null || slot.getType().equals(Material.AIR))
                return;
            Inventory playerInventory = player.getInventory();
            int emptySlot = playerInventory.firstEmpty();
            if (emptySlot != -1)
                player.getInventory().addItem(slot);
            else {
                player.getWorld().dropItemNaturally(player.getLocation(), slot);
            }

        }
    }

    @EventHandler
    public void onClickBoots(InventoryClickEvent event){
        if (event.getClickedInventory() == null)
            return;
        Player player = (Player) event.getWhoClicked();
        if (!player.hasMetadata("BootsMenu"))
            return;

        event.setCancelled(true);
        int slot = event.getSlot();
        if (event.getClickedInventory().getType().equals(InventoryType.PLAYER)){
            ItemStack item;
            try{
                item = event.getClickedInventory().getItem(slot);
            }catch (NullPointerException e) {
                item = null;
            }
            if (item != null){
                ItemStack itemT = item.clone();
                itemT.setAmount(1);
                for (Material crops: ItemUtils.crops){
                    if (crops.equals(item.getType())) {
                        event.getInventory().setItem(13, itemT);
                        break;
                    }
                }
            }
        }

        if (event.getClickedInventory().getSize() == 36){
            if (!(slot == 13)) {
                if (slot == 31)
                    player.closeInventory();
            }else
                event.getInventory().setItem(13, null);
        }
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
                return;
            }else{
                String cropS = slot13.getType().toString();
                if (data.has(new NamespacedKey(FarmingPlus.getPlugin(), "crop"), PersistentDataType.STRING)){
                    ItemStack item = new ItemStack(Material.valueOf(data.get(new NamespacedKey(FarmingPlus.getPlugin(), "crop"), PersistentDataType.STRING)));
                    if (!item.equals(slot13)){
                        data.set(new NamespacedKey(FarmingPlus.getPlugin(), "crop"), PersistentDataType.STRING, cropS);
                        boots.setItemMeta(bootsMeta);
                    }
                }else{
                    data.set(new NamespacedKey(FarmingPlus.getPlugin(), "crop"), PersistentDataType.STRING, cropS);
                    boots.setItemMeta(bootsMeta);
                }
            }

        }
    }

}
