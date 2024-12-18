package fp.manuton.events;

import fp.manuton.FarmingPlus;
import fp.manuton.costs.Cost;
import fp.manuton.enchantments.CustomEnchantments;
import fp.manuton.enchantments.EnchantFp;
import fp.manuton.guis.EnchantGui;
import fp.manuton.utils.ItemUtils;
import fp.manuton.utils.MessageUtils;
import fp.manuton.utils.SoundUtils;
import fp.manuton.utils.VaultUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.List;

public class GuiListener implements Listener {

    @EventHandler(priority = EventPriority.LOWEST)
    public void onClickEn(InventoryClickEvent event){
        Player player = (Player) event.getWhoClicked();
        if (event.getClickedInventory() == null)
            return;
        if (!player.hasMetadata("OpenedMenu"))
            return;
        event.setCancelled(true);
        int slot = event.getSlot();
        if (event.getClickedInventory().getType().equals(InventoryType.PLAYER)){
            ItemStack clicked;
            try {
                clicked = event.getClickedInventory().getItem(event.getSlot());
            }catch (NullPointerException e){
                clicked = null;
            }

            ItemStack item = null;
            if (!player.hasMetadata("menuConfirm")){
                try {
                    item = player.getOpenInventory().getTopInventory().getItem(19);
                }catch (NullPointerException e){
                }
            }

            if (clicked != null && !player.hasMetadata("menuConfirm")){
                if (player.getOpenInventory().getTopInventory().getItem(19) == null){
                    player.getOpenInventory().getTopInventory().setItem(19, clicked);
                    player.getOpenInventory().getBottomInventory().setItem(slot, null);
                }else{
                    ItemStack item19 = player.getOpenInventory().getTopInventory().getItem(19);
                    player.getOpenInventory().getTopInventory().setItem(19, clicked);
                    player.getOpenInventory().getBottomInventory().setItem(slot, null);
                    Inventory playerInventory = player.getInventory();
                    int emptySlot = playerInventory.firstEmpty();
                    if (emptySlot != -1)
                        player.getOpenInventory().getBottomInventory().setItem(emptySlot, item19);
                    else {
                        player.getWorld().dropItemNaturally(player.getLocation(), item19);
                    }
                }
                boolean Enchantable = false;

                if (clicked.getType() == Material.WATER_BUCKET){
                    EnchantGui.guiMenu(player, "water" , player.getOpenInventory().getTopInventory(), null, null);
                    Enchantable = true;
                }
                for (Material type : ItemUtils.boots){
                    if (clicked.getType() == type) {
                        EnchantGui.guiMenu(player, "boots", player.getOpenInventory().getTopInventory(), null, null);
                        Enchantable = true;
                        break;
                    }
                }
                for (Material type : ItemUtils.hoes){
                    if (clicked.getType() == type) {
                        EnchantGui.guiMenu(player, "hoe", player.getOpenInventory().getTopInventory(), null, item);
                        Enchantable = true;
                        break;
                    }
                }
                for (Material type : ItemUtils.axes){
                    if (clicked.getType() == type) {
                        EnchantGui.guiMenu(player, "axe", player.getOpenInventory().getTopInventory(), null, item);
                        Enchantable = true;
                        break;
                    }
                }

                if (!Enchantable){
                    EnchantGui.guiMenu(player, "empty", player.getOpenInventory().getTopInventory(), null, item);
                }

            }
        }else{
            if (!(slot == 19) && !(slot == 12 && player.hasMetadata("menuConfirm"))){
                if (slot == 49){
                    player.closeInventory();
                    return;
                }
                if (slot == 13 && player.hasMetadata("menuConfirm"))
                    return;

                ItemStack clicked;
                try {
                    clicked = event.getClickedInventory().getItem(slot);
                }catch (NullPointerException e){
                    clicked = null;
                }

                ItemStack item;
                try {
                    item = event.getInventory().getItem(19);
                }catch (NullPointerException e){
                    item = null;
                }

                PersistentDataContainer data = null;
                if (clicked != null) {
                    data = clicked.getItemMeta().getPersistentDataContainer();
                }

                if (clicked != null && item != null && !player.hasMetadata("menuConfirm")){
                    if (data.has(new NamespacedKey(FarmingPlus.getPlugin(), "menuItem"), PersistentDataType.STRING)){
                        if (clicked.getItemMeta().hasLore()) {
                            if (clicked.getItemMeta().getLore().contains(MessageUtils.getColoredMessage(FarmingPlus.getPlugin().getMainConfigManager().getEnchanted())))
                                return;
                            int i = 0;
                            boolean found = false;
                            ItemMeta clickedMeta = clicked.getItemMeta();
                            List<String> lore = clickedMeta.getLore();
                            for (String loreL : lore) {
                                if (loreL.contains(MessageUtils.getColoredMessage(FarmingPlus.getPlugin().getMainConfigManager().getClickToSee()))) {
                                    found = true;
                                    break;
                                }
                                i++;
                            }
                            if (found) {
                                lore.remove(i);
                                lore.remove(i-1);
                                clickedMeta.setLore(lore);
                                clicked.setItemMeta(clickedMeta);
                            }
                        }

                        if (clicked.getItemMeta().getDisplayName().equals(MessageUtils.getColoredMessage("&a"+FarmingPlus.getPlugin().getMainConfigManager().getFarmerstepName()))){
                            EnchantGui.guiMenu(player, "farmersStep", player.getOpenInventory().getTopInventory(), null, null);
                        }else if (clicked.getItemMeta().getDisplayName().equals(MessageUtils.getColoredMessage("&a"+FarmingPlus.getPlugin().getMainConfigManager().getGrandtillingName()))) {
                            EnchantGui.guiMenu(player, "grandTilling", player.getOpenInventory().getTopInventory(), null, null);
                        }else if (clicked.getItemMeta().getDisplayName().equals(MessageUtils.getColoredMessage("&a"+FarmingPlus.getPlugin().getMainConfigManager().getFarmersgraceName()))) {
                            EnchantGui.guiMenu(player, "confirm", player.getOpenInventory().getTopInventory(), clicked, item);
                        }else if (clicked.getItemMeta().getDisplayName().equals(MessageUtils.getColoredMessage("&a"+FarmingPlus.getPlugin().getMainConfigManager().getIrrigateName()))) {
                            EnchantGui.guiMenu(player, "confirm", player.getOpenInventory().getTopInventory(), clicked, item);
                        }else if (clicked.getItemMeta().getDisplayName().equals(MessageUtils.getColoredMessage("&a"+FarmingPlus.getPlugin().getMainConfigManager().getDelicateName()))) {
                            EnchantGui.guiMenu(player, "confirm", player.getOpenInventory().getTopInventory(), clicked, item);
                        }else if (clicked.getItemMeta().getDisplayName().equals(MessageUtils.getColoredMessage("&a"+FarmingPlus.getPlugin().getMainConfigManager().getReplenishName()))) {
                            EnchantGui.guiMenu(player, "confirm", player.getOpenInventory().getTopInventory(), clicked, item);
                        }
                    }else if (data.has(new NamespacedKey(FarmingPlus.getPlugin(), "3levels"), PersistentDataType.STRING)){
                        if (clicked.getItemMeta().hasLore()) {
                            if (clicked.getItemMeta().getLore().contains(MessageUtils.getColoredMessage(FarmingPlus.getPlugin().getMainConfigManager().getEnchanted())))
                                return;
                            int i = 0;
                            boolean found = false;
                            ItemMeta clickedMeta = clicked.getItemMeta();
                            List<String> lore = clickedMeta.getLore();
                            for (String loreL : lore) {
                                if (loreL.contains(MessageUtils.getColoredMessage(FarmingPlus.getPlugin().getMainConfigManager().getClickToSee()))) {
                                    found = true;
                                    break;
                                }
                                i++;
                            }
                            if (found) {
                                lore.remove(i);
                                lore.remove(i-1);
                                clickedMeta.setLore(lore);
                                clicked.setItemMeta(clickedMeta);
                            }
                        }
                        EnchantGui.guiMenu(player, "confirm", player.getOpenInventory().getTopInventory(), clicked, item);
                    }
                }else {
                    if (data.has(new NamespacedKey(FarmingPlus.getPlugin(), "menuConfirmItem"), PersistentDataType.STRING)) {
                        if (clicked.getType() == Material.valueOf(FarmingPlus.getPlugin().getMainConfigManager().getGuiConfirm())) {
                            ItemStack enchant;
                            try {
                                enchant = event.getClickedInventory().getItem(13);
                            } catch (NullPointerException e) {
                                enchant = null;
                            }

                            try {
                                item = player.getOpenInventory().getTopInventory().getItem(12);
                            } catch (NullPointerException e) {
                                item = null;
                            }

                            Material itemType = null;
                            try {
                                itemType = player.getOpenInventory().getTopInventory().getItem(12).getType();
                            } catch (NullPointerException e) {
                                itemType = null;
                            }

                            if (item == null || enchant == null)
                                return;

                            String name = enchant.getItemMeta().getDisplayName();
                            EnchantFp enchantment = null;
                            int level = 1;
                            if (name.contains(MessageUtils.getColoredMessage(FarmingPlus.getPlugin().getMainConfigManager().getFarmersgraceName()))) {
                                name = "farmers-grace";
                                enchantment = CustomEnchantments.FARMERSGRACE;
                            }
                            else if (name.contains(MessageUtils.getColoredMessage(FarmingPlus.getPlugin().getMainConfigManager().getIrrigateName()))) {
                                name = "irrigate";
                                enchantment = CustomEnchantments.IRRIGATE;
                            }
                            else if (name.contains(MessageUtils.getColoredMessage(FarmingPlus.getPlugin().getMainConfigManager().getReplenishName()))) {
                                name = "replenish";
                                enchantment = CustomEnchantments.REPLENISH;
                            }
                            else if (name.contains(MessageUtils.getColoredMessage(FarmingPlus.getPlugin().getMainConfigManager().getDelicateName()))) {
                                name = "delicate";
                                enchantment = CustomEnchantments.DELICATE;
                            }
                            else if (name.contains(MessageUtils.getColoredMessage(FarmingPlus.getPlugin().getMainConfigManager().getGrandtillingName()))){
                                enchantment = CustomEnchantments.GRANDTILLING;
                                if (name.endsWith("III")) {
                                    name = "grand-tilling3";
                                    level = 3;
                                }
                                else if (name.endsWith("II")){
                                    name = "grand-tilling2";
                                    level = 2;
                                    }
                                else if (name.endsWith("I"))
                                    name = "grand-tilling1";
                            }else if (name.contains(MessageUtils.getColoredMessage(FarmingPlus.getPlugin().getMainConfigManager().getFarmerstepName()))){
                                enchantment = CustomEnchantments.FARMERSTEP;
                                if (name.endsWith("III")) {
                                    name = "farmers-step3";
                                    level = 3;
                                }
                                else if (name.endsWith("II")) {
                                    name = "farmers-step2";
                                    level = 2;
                                }
                                else if (name.endsWith("I"))
                                    name = "farmers-step1";
                            }

                            Cost cost = FarmingPlus.getPlugin().getMainConfigManager().getCost(name);

                            if (ItemUtils.canPayEnchantment(player, cost)){
                                String enchantedItem = FarmingPlus.getPlugin().getMainConfigManager().getEnchantedItem();
                                String romanLevel = "";
                                if (level == 1)
                                    romanLevel = "I";
                                else if (level == 2)
                                    romanLevel = "II";
                                else if (level == 3)
                                    romanLevel = "III";
                                enchantedItem = ChatColor.stripColor(enchantedItem.replace("%enchantment%", enchantment.getName()+" "+romanLevel));
                                if (item.getItemMeta().hasDisplayName())
                                    enchantedItem = enchantedItem.replace("%item%", item.getItemMeta().getDisplayName());
                                else
                                    enchantedItem = enchantedItem.replace("%item%", item.getType().toString());

                                player.sendMessage(MessageUtils.translateAll(player, enchantedItem));
                                if (!player.hasPermission("fp.bypass.costs")){
                                    String message = FarmingPlus.getPlugin().getMainConfigManager().getPayedStyle();
                                    if (Bukkit.getPluginManager().getPlugin("Vault") != null) {
                                        if (cost.getMoney() > 0) {
                                            VaultUtils.extract(player, cost.getMoney());
                                            message = message.replace("%cost%", VaultUtils.formatCurrencySymbol(cost.getMoney()));
                                            player.sendMessage(MessageUtils.translateAll(player, "  " + message));
                                        }
                                    }

                                    if (cost.getXpLevels() > 0){
                                        player.setLevel(player.getLevel() - cost.getXpLevels());
                                        message = FarmingPlus.getPlugin().getMainConfigManager().getPayedStyle();
                                        message = message.replace("%cost%", cost.getXpLevels()+" "+FarmingPlus.getPlugin().getMainConfigManager().getXpLevelsMessage());
                                        player.sendMessage(MessageUtils.translateAll(player, "  "+message));
                                    }

                                    if (!cost.getItems().isEmpty())
                                        for (String itemP : cost.getItems()){

                                            String[] parts = itemP.split("[\\[\\]]");

                                            String[] partsA = itemP.split(" ");
                                            Material material = Material.valueOf(partsA[0]);
                                            int amount = Integer.parseInt(partsA[1]);
                                            if (amount <= 0)
                                                amount = 1;

                                            String ItemName = null;
                                            try{
                                                ItemName = parts[1];
                                            }catch (Exception e) {
                                                ItemName = partsA[0];
                                            }
                                            if (ItemName == null)
                                                ItemName = partsA[0];

                                            if (material == null)
                                                continue;
                                            if (amount <= 0)
                                                amount = 1;
                                            ItemStack itemPay = new ItemStack(material, amount);
                                            try{
                                                player.getOpenInventory().getBottomInventory().removeItem(itemPay);
                                                message = FarmingPlus.getPlugin().getMainConfigManager().getPayedStyle();
                                                message = message.replace("%cost%", amount+" "+ ItemName);
                                                player.sendMessage(MessageUtils.translateAll(player, "  "+message));
                                            }catch (NullPointerException e){

                                            }

                                        }
                                }

                                item = ItemUtils.enchantedItem(item, enchantment, level);
                                player.getOpenInventory().getTopInventory().setItem(12, item);

                                String sound = FarmingPlus.getPlugin().getMainConfigManager().getSoundOnEnchant();
                                if (SoundUtils.getSoundFromString(sound) != null) {
                                    float volume = FarmingPlus.getPlugin().getMainConfigManager().getVolumeSoundOnEnchant();
                                    player.playSound(player.getLocation(), SoundUtils.getSoundFromString(sound), volume, 1.0f);
                                }
                            }else {
                                player.sendMessage(MessageUtils.translateAll(player, FarmingPlus.getPlugin().getMainConfigManager().getCannotEnchant()));
                                return;
                            }

                            data = enchant.getItemMeta().getPersistentDataContainer();
                            if (data.has(new NamespacedKey(FarmingPlus.getPlugin(), "menuItem"), PersistentDataType.STRING) || data.has(new NamespacedKey(FarmingPlus.getPlugin(), "3levels"), PersistentDataType.STRING)) {
                                player.getOpenInventory().getTopInventory().setItem(19, player.getOpenInventory().getTopInventory().getItem(12));
                                player.getOpenInventory().getTopInventory().setItem(12, null);
                                EnchantGui.guiMenu(player, "empty", player.getOpenInventory().getTopInventory(), null, null);
                                if (enchant.getItemMeta().getDisplayName().contains(MessageUtils.getColoredMessage("&a"+FarmingPlus.getPlugin().getMainConfigManager().getFarmerstepName()))) {

                                    EnchantGui.guiMenu(player, "boots", player.getOpenInventory().getTopInventory(), null, null);

                                } else if (enchant.getItemMeta().getDisplayName().contains(MessageUtils.getColoredMessage("&a"+FarmingPlus.getPlugin().getMainConfigManager().getGrandtillingName()))) {

                                    EnchantGui.guiMenu(player, "hoe", player.getOpenInventory().getTopInventory(), null, null);

                                } else if (enchant.getItemMeta().getDisplayName().equals(MessageUtils.getColoredMessage("&a"+FarmingPlus.getPlugin().getMainConfigManager().getFarmersgraceName()))) {
                                    EnchantGui.guiMenu(player, "boots", player.getOpenInventory().getTopInventory(), null, null);
                                } else if (enchant.getItemMeta().getDisplayName().equals(MessageUtils.getColoredMessage("&a"+FarmingPlus.getPlugin().getMainConfigManager().getIrrigateName()))) {
                                    EnchantGui.guiMenu(player, "water", player.getOpenInventory().getTopInventory(), null, null);
                                } else if (enchant.getItemMeta().getDisplayName().equals(MessageUtils.getColoredMessage("&a"+FarmingPlus.getPlugin().getMainConfigManager().getDelicateName()))) {
                                    EnchantGui.guiMenu(player, "axe", player.getOpenInventory().getTopInventory(), null, null);
                                } else if (enchant.getItemMeta().getDisplayName().equals(MessageUtils.getColoredMessage("&a"+FarmingPlus.getPlugin().getMainConfigManager().getReplenishName()))) {
                                    for (Material type : ItemUtils.hoes)
                                        if (itemType == type) {
                                            EnchantGui.guiMenu(player, "hoe", player.getOpenInventory().getTopInventory(), null, null);
                                            break;
                                        }

                                    for (Material type : ItemUtils.axes)
                                        if (itemType == type) {
                                            EnchantGui.guiMenu(player, "axe", player.getOpenInventory().getTopInventory(), null, null);
                                            break;
                                        }
                                }
                            }

                        } else if (clicked.getType() == Material.valueOf(FarmingPlus.getPlugin().getMainConfigManager().getGuiCancel())) {

                            ItemStack enchant;
                            try {
                                enchant = event.getClickedInventory().getItem(13);
                            } catch (NullPointerException e) {
                                enchant = null;
                            }

                            Material itemType = null;
                            try {
                                itemType = player.getOpenInventory().getTopInventory().getItem(12).getType();
                            } catch (NullPointerException e) {
                                enchant = null;
                            }

                            data = enchant.getItemMeta().getPersistentDataContainer();
                            if (data.has(new NamespacedKey(FarmingPlus.getPlugin(), "menuItem"), PersistentDataType.STRING) || data.has(new NamespacedKey(FarmingPlus.getPlugin(), "3levels"), PersistentDataType.STRING)) {
                                player.getOpenInventory().getTopInventory().setItem(19, player.getOpenInventory().getTopInventory().getItem(12));
                                player.getOpenInventory().getTopInventory().setItem(12, null);
                                EnchantGui.guiMenu(player, "empty", player.getOpenInventory().getTopInventory(), null, null);
                                if (enchant.getItemMeta().getDisplayName().contains(MessageUtils.getColoredMessage("&a"+FarmingPlus.getPlugin().getMainConfigManager().getFarmerstepName()))) {

                                    EnchantGui.guiMenu(player, "boots", player.getOpenInventory().getTopInventory(), null, null);

                                } else if (enchant.getItemMeta().getDisplayName().contains(MessageUtils.getColoredMessage("&a"+FarmingPlus.getPlugin().getMainConfigManager().getGrandtillingName()))) {

                                    EnchantGui.guiMenu(player, "hoe", player.getOpenInventory().getTopInventory(), null, null);

                                } else if (enchant.getItemMeta().getDisplayName().equals(MessageUtils.getColoredMessage("&a"+FarmingPlus.getPlugin().getMainConfigManager().getFarmersgraceName()))) {
                                    EnchantGui.guiMenu(player, "boots", player.getOpenInventory().getTopInventory(), null, null);
                                } else if (enchant.getItemMeta().getDisplayName().equals(MessageUtils.getColoredMessage("&a"+FarmingPlus.getPlugin().getMainConfigManager().getIrrigateName()))) {
                                    EnchantGui.guiMenu(player, "water", player.getOpenInventory().getTopInventory(), null, null);
                                } else if (enchant.getItemMeta().getDisplayName().equals(MessageUtils.getColoredMessage("&a"+FarmingPlus.getPlugin().getMainConfigManager().getDelicateName()))) {
                                    EnchantGui.guiMenu(player, "axe", player.getOpenInventory().getTopInventory(), null, null);
                                } else if (enchant.getItemMeta().getDisplayName().equals(MessageUtils.getColoredMessage("&a"+FarmingPlus.getPlugin().getMainConfigManager().getReplenishName()))) {
                                    for (Material type : ItemUtils.hoes)
                                        if (itemType == type) {
                                            EnchantGui.guiMenu(player, "hoe", player.getOpenInventory().getTopInventory(), null, null);
                                            break;
                                        }

                                    for (Material type : ItemUtils.axes)
                                        if (itemType == type) {
                                            EnchantGui.guiMenu(player, "axe", player.getOpenInventory().getTopInventory(), null, null);
                                            break;
                                        }
                                }
                            }
                        }
                    }
                }

            }else {
                if (player.hasMetadata("menuConfirm") && slot == 19)
                    return;
                if (!player.hasMetadata("menuConfirm") && slot == 12)
                    return;

                Inventory inventory = event.getInventory();
                if (player.hasMetadata("menuConfirm")){
                    slot = 12;
                    player.removeMetadata("menuConfirm", FarmingPlus.getPlugin());
                }else
                    slot = 19;

                ItemStack item;
                try{
                    item = player.getOpenInventory().getTopInventory().getItem(slot);
                }catch (NullPointerException e) {
                    item = null;
                }

                if (item != null){
                    Inventory playerInventory = player.getInventory();
                    int emptySlot = playerInventory.firstEmpty();
                    if (emptySlot != -1)
                        playerInventory.setItem(emptySlot, item);
                    else
                        player.getWorld().dropItemNaturally(player.getLocation(), item);
                    
                    player.getOpenInventory().getTopInventory().setItem(19, null);
                    EnchantGui.guiMenu(player, "empty", player.getOpenInventory().getTopInventory(), null, null);
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
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

    @EventHandler(priority = EventPriority.LOWEST)
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

    @EventHandler(priority = EventPriority.LOWEST)
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
