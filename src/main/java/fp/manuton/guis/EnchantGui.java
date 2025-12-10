package fp.manuton.guis;

import fp.manuton.FarmingPlus;
import fp.manuton.config.MainConfigManager;
import fp.manuton.costs.Cost;
import fp.manuton.enchantments.CustomEnchantments;
import fp.manuton.utils.ItemUtils;
import fp.manuton.utils.MessageUtils;
import fp.manuton.utils.SoundUtils;
import fp.manuton.utils.VaultUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class EnchantGui{

    public static void createGui(Player player, Inventory inventory){
        if (player.hasMetadata("menuConfirm"))
            player.removeMetadata("menuConfirm", FarmingPlus.getPlugin());

        FarmingPlus plugin = FarmingPlus.getPlugin();
        ItemStack empty = new ItemStack(Material.valueOf(plugin.getMainConfigManager().getGuiEmptySlot()));
        ItemMeta emptyMeta = empty.getItemMeta();
        emptyMeta.setDisplayName(" ");
        PersistentDataContainer emptyContainer = emptyMeta.getPersistentDataContainer();
        emptyContainer.set(new NamespacedKey(FarmingPlus.getPlugin(), "empty"), PersistentDataType.STRING, "yes");
        empty.setItemMeta(emptyMeta);

        inventory = Bukkit.createInventory(player, 9 * 6, MessageUtils.getColoredMessage(plugin.getMainConfigManager().getGuiTitle()));

        ItemStack enchantItem = new ItemStack(Material.ENCHANTING_TABLE);
        ItemMeta enchantItemMeta = enchantItem.getItemMeta();
        List<String> enchantItemlore = new ArrayList<String>();
        enchantItemMeta.setDisplayName(MessageUtils.translateAll(player, plugin.getMainConfigManager().getEnchantTitle()));
        for (String line : plugin.getMainConfigManager().getEnchantMessage1()) {
            enchantItemlore.add(MessageUtils.getColoredMessage(line));
        }
        enchantItemMeta.setLore(enchantItemlore);
        enchantItem.setItemMeta(enchantItemMeta);

        ItemStack close = new ItemStack(Material.BARRIER);
        ItemMeta closeMeta = close.getItemMeta();
        closeMeta.setDisplayName(MessageUtils.getColoredMessage("&cClose"));
        close.setItemMeta(closeMeta);

        ItemStack putItem = new ItemStack(Material.PAPER);
        ItemMeta putItemMeta = putItem.getItemMeta();
        List<String> putItemlore = new ArrayList<String>();
        putItemMeta.setDisplayName(MessageUtils.translateAll(player, plugin.getMainConfigManager().getEnchantTitle()));
        for (String line : plugin.getMainConfigManager().getEnchantMessage2()) {
            putItemlore.add(MessageUtils.getColoredMessage(line));
        }
        putItemMeta.setLore(putItemlore);
        putItem.setItemMeta(putItemMeta);

        for (int i = 0; i <= 53; i++){
            if (i != 19)
                inventory.setItem(i, empty);
        }
        inventory.setItem(49, close);
        inventory.setItem(23, putItem);
        inventory.setItem(28, enchantItem);


        if (player.hasMetadata("menuConfirm"))
            player.removeMetadata("menuConfirm", FarmingPlus.getPlugin());

        String sound = plugin.getMainConfigManager().getGuiSoundOpen();
        if (SoundUtils.getSoundFromString(sound) != null) {
            float volume = plugin.getMainConfigManager().getVolumeGuiSoundOpen();
            player.playSound(player.getLocation(), SoundUtils.getSoundFromString(sound), volume, 1.0f);
        }

        player.setMetadata("OpenedMenu", new FixedMetadataValue(plugin, inventory));
        player.openInventory(inventory);
    }

    public static void guiMenu(Player player, String Page, Inventory inventory, ItemStack enchant, ItemStack item){
        if (player.hasMetadata("menuConfirm"))
            player.removeMetadata("menuConfirm", FarmingPlus.getPlugin());

        FarmingPlus plugin = FarmingPlus.getPlugin();
        ItemStack empty = new ItemStack(Material.valueOf(plugin.getMainConfigManager().getGuiEmptySlot()));
        ItemMeta emptyMeta = empty.getItemMeta();
        emptyMeta.setDisplayName(" ");
        PersistentDataContainer emptyContainer = emptyMeta.getPersistentDataContainer();
        emptyContainer.set(new NamespacedKey(FarmingPlus.getPlugin(), "empty"), PersistentDataType.STRING, "yes");
        empty.setItemMeta(emptyMeta);

        ItemStack enchantItem = new ItemStack(Material.ENCHANTING_TABLE);
        ItemMeta enchantItemMeta = enchantItem.getItemMeta();
        List<String> enchantItemlore = new ArrayList<String>();
        enchantItemMeta.setDisplayName(MessageUtils.translateAll(player, plugin.getMainConfigManager().getEnchantTitle()));
        for (String line : plugin.getMainConfigManager().getEnchantMessage1()) {
            enchantItemlore.add(MessageUtils.getColoredMessage(line));
        }
        enchantItemMeta.setLore(enchantItemlore);
        enchantItem.setItemMeta(enchantItemMeta);

        ItemStack close = new ItemStack(Material.BARRIER);
        ItemMeta closeMeta = close.getItemMeta();
        closeMeta.setDisplayName(MessageUtils.getColoredMessage("&cClose"));
        close.setItemMeta(closeMeta);

        ItemStack putItem = new ItemStack(Material.PAPER);
        ItemMeta putItemMeta = putItem.getItemMeta();
        List<String> putItemlore = new ArrayList<String>();
        putItemMeta.setDisplayName(MessageUtils.translateAll(player, plugin.getMainConfigManager().getEnchantTitle()));
        for (String line : plugin.getMainConfigManager().getEnchantMessage2()) {
            putItemlore.add(MessageUtils.getColoredMessage(line));
        }
        putItemMeta.setLore(putItemlore);
        putItem.setItemMeta(putItemMeta);

        for (int i = 21; i <= 25; i++)
            inventory.setItem(i, empty);

        if (Page.equals("empty")){
            for (int i = 0; i <= 53; i++){
                if (i != 19)
                    inventory.setItem(i, empty);
            }
            inventory.setItem(49, close);
            inventory.setItem(23, putItem);
            inventory.setItem(28, enchantItem);
        }else if (Page.equals("boots")){
            if (player.hasMetadata("menuConfirm"))
                player.removeMetadata("menuConfirm", FarmingPlus.getPlugin());

            ItemStack farmerstep = new ItemStack(Material.ENCHANTED_BOOK);
            ItemMeta farmerstepMeta = farmerstep.getItemMeta();
            farmerstepMeta.setDisplayName(MessageUtils.getColoredMessage("&a"+FarmingPlus.getPlugin().getMainConfigManager().getFarmerstepName()));
            List<String> clickFarmerStepLore = new ArrayList<String>();
            for (String loreL : FarmingPlus.getPlugin().getMainConfigManager().getFarmersStepGeneralLore()) {
                clickFarmerStepLore.add(MessageUtils.getColoredMessage(loreL));
            }
            clickFarmerStepLore.add(null);
            clickFarmerStepLore.add(MessageUtils.translateAll(player, plugin.getMainConfigManager().getClickToSee()));
            farmerstepMeta.setLore(clickFarmerStepLore);
            PersistentDataContainer farmersStepContainer = farmerstepMeta.getPersistentDataContainer();
            farmersStepContainer.set(new NamespacedKey(FarmingPlus.getPlugin(), "menuItem"), PersistentDataType.STRING, "yes");
            farmerstep.setItemMeta(farmerstepMeta);

            ItemStack farmersgrace = new ItemStack(Material.ENCHANTED_BOOK);
            ItemMeta farmersgraceMeta = farmersgrace.getItemMeta();
            farmersgraceMeta.setDisplayName(MessageUtils.getColoredMessage("&a"+FarmingPlus.getPlugin().getMainConfigManager().getFarmersgraceName()));
            List<String> loreFarmersgrace = new ArrayList<String>();
            for (String loreL : FarmingPlus.getPlugin().getMainConfigManager().getFarmersgraceLore()) {
                loreFarmersgrace.add(MessageUtils.getColoredMessage(loreL));
            }
            loreFarmersgrace.add(null);
            if (ItemUtils.hasCustomEnchant(inventory.getItem(19), CustomEnchantments.FARMERSGRACE))
                loreFarmersgrace.add(MessageUtils.translateAll(player, plugin.getMainConfigManager().getEnchanted()));
            else
                loreFarmersgrace.add(MessageUtils.translateAll(player, plugin.getMainConfigManager().getClickToSee()));
            farmersgraceMeta.setLore(loreFarmersgrace);
            PersistentDataContainer farmersGraceContainer = farmersgraceMeta.getPersistentDataContainer();
            farmersGraceContainer.set(new NamespacedKey(FarmingPlus.getPlugin(), "menuItem"), PersistentDataType.STRING, "yes");
            farmersgrace.setItemMeta(farmersgraceMeta);

            inventory.setItem(23,empty);
            inventory.setItem(21, farmersgrace);
            inventory.setItem(22, farmerstep);
        }else if (Page.equals("hoe")){
            if (player.hasMetadata("menuConfirm"))
                player.removeMetadata("menuConfirm", FarmingPlus.getPlugin());

            ItemStack replenish = new ItemStack(Material.ENCHANTED_BOOK);
            ItemMeta replenishMeta = replenish.getItemMeta();
            replenishMeta.setDisplayName(MessageUtils.getColoredMessage("&a"+FarmingPlus.getPlugin().getMainConfigManager().getReplenishName()));
            List<String> loreReplenish = new ArrayList<String>();
            for (String loreL : FarmingPlus.getPlugin().getMainConfigManager().getReplenishLore()) {
                loreReplenish.add(MessageUtils.getColoredMessage(loreL));
            }
            loreReplenish.add(null);
            if (ItemUtils.hasCustomEnchant(inventory.getItem(19), CustomEnchantments.REPLENISH))
                loreReplenish.add(MessageUtils.translateAll(player, plugin.getMainConfigManager().getEnchanted()));
            else
                loreReplenish.add(MessageUtils.translateAll(player, plugin.getMainConfigManager().getClickToSee()));
            replenishMeta.setLore(loreReplenish);
            PersistentDataContainer replenishContainer = replenishMeta.getPersistentDataContainer();
            replenishContainer.set(new NamespacedKey(FarmingPlus.getPlugin(), "menuItem"), PersistentDataType.STRING, "yes");
            replenish.setItemMeta(replenishMeta);

            ItemStack grandTilling = new ItemStack(Material.ENCHANTED_BOOK);
            ItemMeta grandTillingMeta = grandTilling.getItemMeta();
            grandTillingMeta.setDisplayName(MessageUtils.getColoredMessage("&a"+FarmingPlus.getPlugin().getMainConfigManager().getGrandtillingName()));
            List<String> clickGrandTillingLore = new ArrayList<String>();
            for (String loreL : FarmingPlus.getPlugin().getMainConfigManager().getGrandTillingGeneralLore()) {
                clickGrandTillingLore.add(MessageUtils.getColoredMessage(loreL));
            }
            clickGrandTillingLore.add(null);
            clickGrandTillingLore.add(MessageUtils.translateAll(player, plugin.getMainConfigManager().getClickToSee()));
            grandTillingMeta.setLore(clickGrandTillingLore);
            PersistentDataContainer grandTillingContainer = grandTillingMeta.getPersistentDataContainer();
            grandTillingContainer.set(new NamespacedKey(FarmingPlus.getPlugin(), "menuItem"), PersistentDataType.STRING, "yes");
            grandTilling.setItemMeta(grandTillingMeta);

            inventory.setItem(23,empty);
            inventory.setItem(21, replenish);
            inventory.setItem(22, grandTilling);

        }else if (Page.equals("axe")){
            if (player.hasMetadata("menuConfirm"))
                player.removeMetadata("menuConfirm", FarmingPlus.getPlugin());

            ItemStack replenish = new ItemStack(Material.ENCHANTED_BOOK);
            ItemMeta replenishMeta = replenish.getItemMeta();
            replenishMeta.setDisplayName(MessageUtils.getColoredMessage("&a"+FarmingPlus.getPlugin().getMainConfigManager().getReplenishName()));
            List<String> loreReplenish = new ArrayList<String>();
            for (String loreL : FarmingPlus.getPlugin().getMainConfigManager().getReplenishLore()) {
                loreReplenish.add(MessageUtils.getColoredMessage(loreL));
            }
            loreReplenish.add(null);
            if (ItemUtils.hasCustomEnchant(inventory.getItem(19), CustomEnchantments.REPLENISH))
                loreReplenish.add(MessageUtils.translateAll(player, plugin.getMainConfigManager().getEnchanted()));
            else
                loreReplenish.add(MessageUtils.translateAll(player, plugin.getMainConfigManager().getClickToSee()));
            replenishMeta.setLore(loreReplenish);
            PersistentDataContainer replenishContainer = replenishMeta.getPersistentDataContainer();
            replenishContainer.set(new NamespacedKey(FarmingPlus.getPlugin(), "menuItem"), PersistentDataType.STRING, "yes");
            replenish.setItemMeta(replenishMeta);

            ItemStack delicate = new ItemStack(Material.ENCHANTED_BOOK);
            ItemMeta delicateMeta = delicate.getItemMeta();
            delicateMeta.setDisplayName(MessageUtils.getColoredMessage("&a"+FarmingPlus.getPlugin().getMainConfigManager().getDelicateName()));
            List<String> loreDelicate = new ArrayList<String>();
            for (String loreL : FarmingPlus.getPlugin().getMainConfigManager().getDelicateLore()) {
                loreDelicate.add(MessageUtils.getColoredMessage(loreL));
            }
            loreDelicate.add(null);
            if (ItemUtils.hasCustomEnchant(inventory.getItem(19), CustomEnchantments.DELICATE))
                loreDelicate.add(MessageUtils.translateAll(player, plugin.getMainConfigManager().getEnchanted()));
            else
                loreDelicate.add(MessageUtils.translateAll(player, plugin.getMainConfigManager().getClickToSee()));
            delicateMeta.setLore(loreDelicate);
            PersistentDataContainer delicateContainer = delicateMeta.getPersistentDataContainer();
            delicateContainer.set(new NamespacedKey(FarmingPlus.getPlugin(), "menuItem"), PersistentDataType.STRING, "yes");
            delicate.setItemMeta(delicateMeta);

            inventory.setItem(23,empty);
            inventory.setItem(21, replenish);
            inventory.setItem(22, delicate);

        }else if (Page.equals("water")){
            if (player.hasMetadata("menuConfirm"))
                player.removeMetadata("menuConfirm", FarmingPlus.getPlugin());

            ItemStack irrigate = new ItemStack(Material.ENCHANTED_BOOK);
            ItemMeta irrigateMeta = irrigate.getItemMeta();
            irrigateMeta.setDisplayName(MessageUtils.getColoredMessage("&a"+FarmingPlus.getPlugin().getMainConfigManager().getIrrigateName()));
            List<String> loreIrrigate = new ArrayList<String>();
            for (String loreL : FarmingPlus.getPlugin().getMainConfigManager().getIrrigateLore()) {
                loreIrrigate.add(MessageUtils.getColoredMessage(loreL));
            }
            loreIrrigate.add(null);
            if (ItemUtils.hasCustomEnchant(inventory.getItem(19), CustomEnchantments.IRRIGATE))
                loreIrrigate.add(MessageUtils.translateAll(player, plugin.getMainConfigManager().getEnchanted()));
            else
                loreIrrigate.add(MessageUtils.translateAll(player, plugin.getMainConfigManager().getClickToSee()));
            irrigateMeta.setLore(loreIrrigate);
            PersistentDataContainer irrigateContainer = irrigateMeta.getPersistentDataContainer();
            irrigateContainer.set(new NamespacedKey(FarmingPlus.getPlugin(), "menuItem"), PersistentDataType.STRING, "yes");
            irrigate.setItemMeta(irrigateMeta);

            inventory.setItem(23,empty);
            inventory.setItem(21, irrigate);

        }else if (Page.equals("grandTilling")){
            if (player.hasMetadata("menuConfirm"))
                player.removeMetadata("menuConfirm", FarmingPlus.getPlugin());

            ItemStack grandTilling1 = new ItemStack(Material.ENCHANTED_BOOK);
            ItemMeta grandTillingMeta1 = grandTilling1.getItemMeta();
            grandTillingMeta1.setDisplayName(MessageUtils.getColoredMessage("&a"+FarmingPlus.getPlugin().getMainConfigManager().getGrandtillingName()+" I"));
            List<String> clickGrandTillingLore1 = new ArrayList<String>();
            for (String loreL : FarmingPlus.getPlugin().getMainConfigManager().getGrandtillingLore1()) {
                clickGrandTillingLore1.add(MessageUtils.getColoredMessage(loreL));
            }
            clickGrandTillingLore1.add(null);
            if (ItemUtils.hasCustomEnchant(inventory.getItem(19), CustomEnchantments.GRANDTILLING) && ItemUtils.getCustomEnchantLevel(inventory.getItem(19), CustomEnchantments.GRANDTILLING) == 1)
                clickGrandTillingLore1.add(MessageUtils.translateAll(player, plugin.getMainConfigManager().getEnchanted()));
            else
                clickGrandTillingLore1.add(MessageUtils.translateAll(player, plugin.getMainConfigManager().getClickToSee()));
            grandTillingMeta1.setLore(clickGrandTillingLore1);
            PersistentDataContainer grandTillingContainer1 = grandTillingMeta1.getPersistentDataContainer();
            grandTillingContainer1.set(new NamespacedKey(FarmingPlus.getPlugin(), "3levels"), PersistentDataType.STRING, "yes");
            grandTilling1.setItemMeta(grandTillingMeta1);

            ItemStack grandTilling2 = new ItemStack(Material.ENCHANTED_BOOK);
            ItemMeta grandTillingMeta2 = grandTilling2.getItemMeta();
            grandTillingMeta2.setDisplayName(MessageUtils.getColoredMessage("&a"+FarmingPlus.getPlugin().getMainConfigManager().getGrandtillingName()+" II"));
            List<String> clickGrandTillingLore2 = new ArrayList<String>();
            for (String loreL : FarmingPlus.getPlugin().getMainConfigManager().getGrandtillingLore2()) {
                clickGrandTillingLore2.add(MessageUtils.getColoredMessage(loreL));
            }
            clickGrandTillingLore2.add(null);
            if (ItemUtils.hasCustomEnchant(inventory.getItem(19), CustomEnchantments.GRANDTILLING) && ItemUtils.getCustomEnchantLevel(inventory.getItem(19), CustomEnchantments.GRANDTILLING) == 2)
                clickGrandTillingLore2.add(MessageUtils.translateAll(player, plugin.getMainConfigManager().getEnchanted()));
            else
                clickGrandTillingLore2.add(MessageUtils.translateAll(player, plugin.getMainConfigManager().getClickToSee()));
            grandTillingMeta2.setLore(clickGrandTillingLore2);
            PersistentDataContainer grandTillingContainer2 = grandTillingMeta2.getPersistentDataContainer();
            grandTillingContainer2.set(new NamespacedKey(FarmingPlus.getPlugin(), "3levels"), PersistentDataType.STRING, "yes");
            grandTilling2.setItemMeta(grandTillingMeta2);

            ItemStack grandTilling3 = new ItemStack(Material.ENCHANTED_BOOK);
            ItemMeta grandTillingMeta3 = grandTilling3.getItemMeta();
            grandTillingMeta3.setDisplayName(MessageUtils.getColoredMessage("&a"+FarmingPlus.getPlugin().getMainConfigManager().getGrandtillingName()+" III"));
            List<String> clickGrandTillingLore3 = new ArrayList<String>();
            for (String loreL : FarmingPlus.getPlugin().getMainConfigManager().getGrandtillingLore3()) {
                clickGrandTillingLore3.add(MessageUtils.getColoredMessage(loreL));
            }
            clickGrandTillingLore3.add(null);
            if (ItemUtils.hasCustomEnchant(inventory.getItem(19), CustomEnchantments.GRANDTILLING) && ItemUtils.getCustomEnchantLevel(inventory.getItem(19), CustomEnchantments.GRANDTILLING) == 3)
                clickGrandTillingLore3.add(MessageUtils.translateAll(player, plugin.getMainConfigManager().getEnchanted()));
            else
                clickGrandTillingLore3.add(MessageUtils.translateAll(player, plugin.getMainConfigManager().getClickToSee()));
            grandTillingMeta3.setLore(clickGrandTillingLore3);
            PersistentDataContainer grandTillingContainer3 = grandTillingMeta3.getPersistentDataContainer();
            grandTillingContainer3.set(new NamespacedKey(FarmingPlus.getPlugin(), "3levels"), PersistentDataType.STRING, "yes");
            grandTilling3.setItemMeta(grandTillingMeta3);

            inventory.setItem(21,grandTilling1);
            inventory.setItem(23,grandTilling2);
            inventory.setItem(25,grandTilling3);


        }else if (Page.equals("farmersStep")){
            if (player.hasMetadata("menuConfirm"))
                player.removeMetadata("menuConfirm", FarmingPlus.getPlugin());

            ItemStack farmersStep1 = new ItemStack(Material.ENCHANTED_BOOK);
            ItemMeta farmersStepMeta1 = farmersStep1.getItemMeta();
            farmersStepMeta1.setDisplayName(MessageUtils.getColoredMessage("&a"+FarmingPlus.getPlugin().getMainConfigManager().getFarmerstepName()+" I"));
            List<String> clickFarmersStep1 = new ArrayList<String>();
            for (String loreL : FarmingPlus.getPlugin().getMainConfigManager().getFarmerstepLore1()) {
                clickFarmersStep1.add(MessageUtils.getColoredMessage(loreL));
            }
            clickFarmersStep1.add(null);
            if (ItemUtils.hasCustomEnchant(inventory.getItem(19), CustomEnchantments.FARMERSTEP) && ItemUtils.getCustomEnchantLevel(inventory.getItem(19), CustomEnchantments.FARMERSTEP) == 1)
                clickFarmersStep1.add(MessageUtils.translateAll(player, plugin.getMainConfigManager().getEnchanted()));
            else
                clickFarmersStep1.add(MessageUtils.translateAll(player, plugin.getMainConfigManager().getClickToSee()));
            farmersStepMeta1.setLore(clickFarmersStep1);
            PersistentDataContainer grandTillingContainer1 = farmersStepMeta1.getPersistentDataContainer();
            grandTillingContainer1.set(new NamespacedKey(FarmingPlus.getPlugin(), "3levels"), PersistentDataType.STRING, "yes");
            farmersStep1.setItemMeta(farmersStepMeta1);

            ItemStack farmersStep2 = new ItemStack(Material.ENCHANTED_BOOK);
            ItemMeta farmersStepMeta2 = farmersStep2.getItemMeta();
            farmersStepMeta2.setDisplayName(MessageUtils.getColoredMessage("&a"+FarmingPlus.getPlugin().getMainConfigManager().getFarmerstepName()+" II"));
            List<String> clickFarmersStepLore2 = new ArrayList<String>();
            for (String loreL : FarmingPlus.getPlugin().getMainConfigManager().getFarmerstepLore2()) {
                clickFarmersStepLore2.add(MessageUtils.getColoredMessage(loreL));
            }
            clickFarmersStepLore2.add(null);
            if (ItemUtils.hasCustomEnchant(inventory.getItem(19), CustomEnchantments.FARMERSTEP) && ItemUtils.getCustomEnchantLevel(inventory.getItem(19), CustomEnchantments.FARMERSTEP) == 2)
                clickFarmersStepLore2.add(MessageUtils.translateAll(player, plugin.getMainConfigManager().getEnchanted()));
            else
                clickFarmersStepLore2.add(MessageUtils.translateAll(player, plugin.getMainConfigManager().getClickToSee()));
            farmersStepMeta2.setLore(clickFarmersStepLore2);
            PersistentDataContainer grandTillingContainer2 = farmersStepMeta2.getPersistentDataContainer();
            grandTillingContainer2.set(new NamespacedKey(FarmingPlus.getPlugin(), "3levels"), PersistentDataType.STRING, "yes");
            farmersStep2.setItemMeta(farmersStepMeta2);

            ItemStack farmersStep3 = new ItemStack(Material.ENCHANTED_BOOK);
            ItemMeta farmersStepMeta3 = farmersStep3.getItemMeta();
            farmersStepMeta3.setDisplayName(MessageUtils.getColoredMessage("&a"+FarmingPlus.getPlugin().getMainConfigManager().getFarmerstepName()+" III"));
            List<String> clickFarmersStepLore3 = new ArrayList<String>();
            for (String loreL : FarmingPlus.getPlugin().getMainConfigManager().getFarmerstepLore3()) {
                clickFarmersStepLore3.add(MessageUtils.getColoredMessage(loreL));
            }
            clickFarmersStepLore3.add(null);
            if (ItemUtils.hasCustomEnchant(inventory.getItem(19), CustomEnchantments.FARMERSTEP) && ItemUtils.getCustomEnchantLevel(inventory.getItem(19), CustomEnchantments.FARMERSTEP) == 3)
                clickFarmersStepLore3.add(MessageUtils.translateAll(player, plugin.getMainConfigManager().getEnchanted()));
            else
                clickFarmersStepLore3.add(MessageUtils.translateAll(player, plugin.getMainConfigManager().getClickToSee()));
            farmersStepMeta3.setLore(clickFarmersStepLore3);
            PersistentDataContainer grandTillingContainer3 = farmersStepMeta3.getPersistentDataContainer();
            grandTillingContainer3.set(new NamespacedKey(FarmingPlus.getPlugin(), "3levels"), PersistentDataType.STRING, "yes");
            farmersStep3.setItemMeta(farmersStepMeta3);

            inventory.setItem(21,farmersStep1);
            inventory.setItem(23,farmersStep2);
            inventory.setItem(25,farmersStep3);


        }else if (Page.equals("confirm")){

            for (int i = 9; i <= 45; i++){
                inventory.setItem(i, empty);
            }

            ItemStack confirm = new ItemStack(Material.valueOf(FarmingPlus.getPlugin().getMainConfigManager().getGuiConfirm()));
            ItemMeta confirmMeta = confirm.getItemMeta();
            confirmMeta.setDisplayName(MessageUtils.getColoredMessage("&aConfirm"));
            PersistentDataContainer confirmContainer = confirmMeta.getPersistentDataContainer();
            confirmContainer.set(new NamespacedKey(FarmingPlus.getPlugin(), "menuConfirmItem"), PersistentDataType.STRING, "yes");
            confirm.setItemMeta(confirmMeta);

            ItemStack cancel = new ItemStack(Material.valueOf(FarmingPlus.getPlugin().getMainConfigManager().getGuiCancel()));
            ItemMeta cancelMeta = cancel.getItemMeta();
            cancelMeta.setDisplayName(MessageUtils.getColoredMessage("&cCancel"));
            PersistentDataContainer cancelContainer = cancelMeta.getPersistentDataContainer();
            cancelContainer.set(new NamespacedKey(FarmingPlus.getPlugin(), "menuConfirmItem"), PersistentDataType.STRING, "yes");
            cancel.setItemMeta(cancelMeta);

            ItemMeta enchantMeta = enchant.getItemMeta();
            String name = enchantMeta.getDisplayName();

            if (name.contains(MessageUtils.getColoredMessage("&a"+FarmingPlus.getPlugin().getMainConfigManager().getFarmersgraceName())))
                name = "farmers-grace"; // farmers-grace
            else if (name.contains(MessageUtils.getColoredMessage("&a"+FarmingPlus.getPlugin().getMainConfigManager().getIrrigateName())))
                name = "irrigate"; //
            else if (name.contains(MessageUtils.getColoredMessage("&a"+FarmingPlus.getPlugin().getMainConfigManager().getReplenishName())))
                name = "replenish";
            else if (name.contains(MessageUtils.getColoredMessage("&a"+FarmingPlus.getPlugin().getMainConfigManager().getDelicateName())))
                name = "delicate";
            else if (name.contains(MessageUtils.getColoredMessage("&a"+FarmingPlus.getPlugin().getMainConfigManager().getGrandtillingName()))){
                if (name.endsWith("III"))
                    name = "grand-tilling3";
                else if (name.endsWith("II"))
                    name = "grand-tilling2";
                else if (name.endsWith("I"))
                    name = "grand-tilling1";
            }else if (name.contains(MessageUtils.getColoredMessage("&a"+FarmingPlus.getPlugin().getMainConfigManager().getFarmerstepName()))){
                if (name.endsWith("III"))
                    name = "farmers-step3";
                else if (name.endsWith("II"))
                    name = "farmers-step2";
                else if (name.endsWith("I"))
                    name = "farmers-step1";
            }


            Cost costE = null;
            for (String cost : FarmingPlus.getPlugin().getMainConfigManager().getAllCostsNames()){
                if (name.equals(cost.toString())){
                    costE = FarmingPlus.getPlugin().getMainConfigManager().getCost(cost);
                    break;
                }
            }

            ItemStack anvil = new ItemStack(Material.ANVIL);
            ItemMeta anvilMeta = anvil.getItemMeta();
            anvilMeta.setDisplayName(MessageUtils.translateAll(player, plugin.getMainConfigManager().getItemCostTitle()));
            List<String> anvilLore = new ArrayList<String>();
            anvilLore.add(MessageUtils.translateAll(player, plugin.getMainConfigManager().getCostMessage()));

            // ✓ - ✔ - ✗ - ✘ //

            if (costE != null) {
                if (costE.getXpLevels() > 0)
                    if (player.hasPermission("fp.bypass.costs") || player.getLevel() >= costE.getXpLevels()) {
                        anvilLore.add(MessageUtils.getColoredMessage("&a✔ - " + String.valueOf(costE.getXpLevels()) + " " + plugin.getMainConfigManager().getXpLevelsMessage()));
                    }else {
                        anvilLore.add(MessageUtils.getColoredMessage("&c✘ - " + String.valueOf(costE.getXpLevels()) + " " + plugin.getMainConfigManager().getXpLevelsMessage()));
                    }
                if (Bukkit.getPluginManager().getPlugin("Vault") != null) {
                    if (costE.getMoney() > 0)
                        if (player.hasPermission("fp.bypass.costs") || VaultUtils.getMoney(player) >= costE.getMoney())
                            anvilLore.add(MessageUtils.getColoredMessage("&a✔ - " + VaultUtils.formatCurrencySymbol(costE.getMoney())));
                        else
                            anvilLore.add(MessageUtils.getColoredMessage("&c✘ - " + VaultUtils.formatCurrencySymbol(costE.getMoney())));
                }
                if (!costE.getItems().isEmpty()) {
                    anvilLore.add(MessageUtils.getColoredMessage("&e- Items:"));
                    for (String itemE : costE.getItems()) {
                        String[] parts = itemE.split("[\\[\\]]");

                        String[] partsA = itemE.split(" ");
                        itemE = partsA[0];
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

                        if (player.hasPermission("fp.bypass.costs") || player.getInventory().containsAtLeast(new ItemStack(Material.valueOf(itemE.toUpperCase())), amount))
                            anvilLore.add(MessageUtils.getColoredMessage("&a✔ - " + amount + " " + ItemName));
                        else
                            anvilLore.add(MessageUtils.getColoredMessage("&c✘ - " + amount + " " + ItemName));

                    }
                }
            }
            anvilMeta.setLore(anvilLore);
            anvil.setItemMeta(anvilMeta);


            inventory.setItem(12, item);
            inventory.setItem(13, enchant);
            inventory.setItem(14, anvil);
            inventory.setItem(30, confirm);
            inventory.setItem(32, cancel);
            player.setMetadata("menuConfirm", new FixedMetadataValue(FarmingPlus.getPlugin(), inventory));
        }
    }




}
