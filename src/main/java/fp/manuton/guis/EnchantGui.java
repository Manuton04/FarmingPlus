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
        enchantItemMeta.setDisplayName(MessageUtils.getColoredMessage("&aEnchant Item"));
        enchantItemlore.add(MessageUtils.getColoredMessage("&7Add and remove FarmingPlus´ enchantments from"));
        enchantItemlore.add(MessageUtils.getColoredMessage("&7the item in the slot above."));
        enchantItemMeta.setLore(enchantItemlore);
        enchantItem.setItemMeta(enchantItemMeta);

        ItemStack close = new ItemStack(Material.BARRIER);
        ItemMeta closeMeta = close.getItemMeta();
        closeMeta.setDisplayName(MessageUtils.getColoredMessage("&cClose"));
        close.setItemMeta(closeMeta);

        ItemStack putItem = new ItemStack(Material.PAPER);
        ItemMeta putItemMeta = putItem.getItemMeta();
        List<String> putItemlore = new ArrayList<String>();
        putItemMeta.setDisplayName(MessageUtils.getColoredMessage("&cEnchant Item"));
        putItemlore.add(MessageUtils.getColoredMessage("&7Place an item to enchant"));
        putItemlore.add(MessageUtils.getColoredMessage("&7in the open slot!"));
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
        enchantItemMeta.setDisplayName(MessageUtils.getColoredMessage("&aEnchant Item"));
        enchantItemlore.add(MessageUtils.getColoredMessage("&7Add and remove FarmingPlus´ enchantments from"));
        enchantItemlore.add(MessageUtils.getColoredMessage("&7the item in the slot above."));
        enchantItemMeta.setLore(enchantItemlore);
        enchantItem.setItemMeta(enchantItemMeta);

        ItemStack close = new ItemStack(Material.BARRIER);
        ItemMeta closeMeta = close.getItemMeta();
        closeMeta.setDisplayName(MessageUtils.getColoredMessage("&cClose"));
        close.setItemMeta(closeMeta);

        ItemStack putItem = new ItemStack(Material.PAPER);
        ItemMeta putItemMeta = putItem.getItemMeta();
        List<String> putItemlore = new ArrayList<String>();
        putItemMeta.setDisplayName(MessageUtils.getColoredMessage("&cEnchant Item"));
        putItemlore.add(MessageUtils.getColoredMessage("&7Place an item to enchant"));
        putItemlore.add(MessageUtils.getColoredMessage("&7in the open slot!"));
        putItemMeta.setLore(putItemlore);
        putItem.setItemMeta(putItemMeta);

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
            farmerstepMeta.setDisplayName(MessageUtils.getColoredMessage(FarmingPlus.getPlugin().getMainConfigManager().getFarmerstepName()));
            List<String> clickFarmerStepLore = new ArrayList<String>();
            for (String loreL : FarmingPlus.getPlugin().getMainConfigManager().getFarmersStepGeneralLore()) {
                clickFarmerStepLore.add(MessageUtils.getColoredMessage(loreL));
            }
            clickFarmerStepLore.add(null);
            clickFarmerStepLore.add(MessageUtils.getColoredMessage("&eClick to see!"));
            farmerstepMeta.setLore(clickFarmerStepLore);
            PersistentDataContainer farmersStepContainer = farmerstepMeta.getPersistentDataContainer();
            farmersStepContainer.set(new NamespacedKey(FarmingPlus.getPlugin(), "menuItem"), PersistentDataType.STRING, "yes");
            farmerstep.setItemMeta(farmerstepMeta);

            ItemStack farmersgrace = new ItemStack(Material.ENCHANTED_BOOK);
            ItemMeta farmersgraceMeta = farmersgrace.getItemMeta();
            farmersgraceMeta.setDisplayName(MessageUtils.getColoredMessage(FarmingPlus.getPlugin().getMainConfigManager().getFarmersgraceName()));
            List<String> loreFarmersgrace = new ArrayList<String>();
            for (String loreL : FarmingPlus.getPlugin().getMainConfigManager().getFarmersgraceLore()) {
                loreFarmersgrace.add(MessageUtils.getColoredMessage(loreL));
            }
            loreFarmersgrace.add(null);
            if (inventory.getItem(19).getItemMeta().hasEnchant(CustomEnchantments.FARMERSGRACE))
                loreFarmersgrace.add(MessageUtils.getColoredMessage("&a&l✔ Enchanted"));
            else
                loreFarmersgrace.add(MessageUtils.getColoredMessage("&eClick to see!"));
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
            replenishMeta.setDisplayName(MessageUtils.getColoredMessage(FarmingPlus.getPlugin().getMainConfigManager().getReplenishName()));
            List<String> loreReplenish = new ArrayList<String>();
            for (String loreL : FarmingPlus.getPlugin().getMainConfigManager().getReplenishLore()) {
                loreReplenish.add(MessageUtils.getColoredMessage(loreL));
            }
            loreReplenish.add(null);
            if (inventory.getItem(19).getItemMeta().hasEnchant(CustomEnchantments.REPLENISH))
                loreReplenish.add(MessageUtils.getColoredMessage("&a&l✔ Enchanted"));
            else
                loreReplenish.add(MessageUtils.getColoredMessage("&eClick to see!"));
            replenishMeta.setLore(loreReplenish);
            PersistentDataContainer replenishContainer = replenishMeta.getPersistentDataContainer();
            replenishContainer.set(new NamespacedKey(FarmingPlus.getPlugin(), "menuItem"), PersistentDataType.STRING, "yes");
            replenish.setItemMeta(replenishMeta);

            ItemStack grandTilling = new ItemStack(Material.ENCHANTED_BOOK);
            ItemMeta grandTillingMeta = grandTilling.getItemMeta();
            grandTillingMeta.setDisplayName(MessageUtils.getColoredMessage(FarmingPlus.getPlugin().getMainConfigManager().getGrandtillingName()));
            List<String> clickGrandTillingLore = new ArrayList<String>();
            for (String loreL : FarmingPlus.getPlugin().getMainConfigManager().getGrandTillingGeneralLore()) {
                clickGrandTillingLore.add(MessageUtils.getColoredMessage(loreL));
            }
            clickGrandTillingLore.add(null);
            clickGrandTillingLore.add(MessageUtils.getColoredMessage("&eClick to see!"));
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
            replenishMeta.setDisplayName(MessageUtils.getColoredMessage(FarmingPlus.getPlugin().getMainConfigManager().getReplenishName()));
            List<String> loreReplenish = new ArrayList<String>();
            for (String loreL : FarmingPlus.getPlugin().getMainConfigManager().getReplenishLore()) {
                loreReplenish.add(MessageUtils.getColoredMessage(loreL));
            }
            loreReplenish.add(null);
            if (inventory.getItem(19).getItemMeta().hasEnchant(CustomEnchantments.REPLENISH))
                loreReplenish.add(MessageUtils.getColoredMessage("&a&l✔ Enchanted"));
            else
                loreReplenish.add(MessageUtils.getColoredMessage("&eClick to see!"));
            replenishMeta.setLore(loreReplenish);
            PersistentDataContainer replenishContainer = replenishMeta.getPersistentDataContainer();
            replenishContainer.set(new NamespacedKey(FarmingPlus.getPlugin(), "menuItem"), PersistentDataType.STRING, "yes");
            replenish.setItemMeta(replenishMeta);

            ItemStack delicate = new ItemStack(Material.ENCHANTED_BOOK);
            ItemMeta delicateMeta = delicate.getItemMeta();
            delicateMeta.setDisplayName(MessageUtils.getColoredMessage(FarmingPlus.getPlugin().getMainConfigManager().getDelicateName()));
            List<String> loreDelicate = new ArrayList<String>();
            for (String loreL : FarmingPlus.getPlugin().getMainConfigManager().getDelicateLore()) {
                loreDelicate.add(MessageUtils.getColoredMessage(loreL));
            }
            loreDelicate.add(null);
            if (inventory.getItem(19).getItemMeta().hasEnchant(CustomEnchantments.DELICATE))
                loreDelicate.add(MessageUtils.getColoredMessage("&a&l✔ Enchanted"));
            else
                loreDelicate.add(MessageUtils.getColoredMessage("&eClick to see!"));
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
            irrigateMeta.setDisplayName(MessageUtils.getColoredMessage(FarmingPlus.getPlugin().getMainConfigManager().getIrrigateName()));
            List<String> loreIrrigate = new ArrayList<String>();
            for (String loreL : FarmingPlus.getPlugin().getMainConfigManager().getIrrigateLore()) {
                loreIrrigate.add(MessageUtils.getColoredMessage(loreL));
            }
            loreIrrigate.add(null);
            if (inventory.getItem(19).getItemMeta().hasEnchant(CustomEnchantments.IRRIGATE))
                loreIrrigate.add(MessageUtils.getColoredMessage("&a&l✔ Enchanted"));
            else
                loreIrrigate.add(MessageUtils.getColoredMessage("&eClick to see!"));
            irrigateMeta.setLore(loreIrrigate);
            PersistentDataContainer irrigateContainer = irrigateMeta.getPersistentDataContainer();
            irrigateContainer.set(new NamespacedKey(FarmingPlus.getPlugin(), "menuItem"), PersistentDataType.STRING, "yes");
            irrigate.setItemMeta(irrigateMeta);

            inventory.setItem(23,empty);
            inventory.setItem(21, irrigate);

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

            if (name.contains(MessageUtils.getColoredMessage(FarmingPlus.getPlugin().getMainConfigManager().getFarmersgraceName())))
                name = "farmers-grace"; // farmers-grace
            else if (name.contains(MessageUtils.getColoredMessage(FarmingPlus.getPlugin().getMainConfigManager().getIrrigateName())))
                name = "irrigate"; //
            else if (name.contains(MessageUtils.getColoredMessage(FarmingPlus.getPlugin().getMainConfigManager().getReplenishName())))
                name = "replenish";
            else if (name.contains(MessageUtils.getColoredMessage(FarmingPlus.getPlugin().getMainConfigManager().getDelicateName())))
                name = "delicate";
            else if (name.contains(MessageUtils.getColoredMessage(FarmingPlus.getPlugin().getMainConfigManager().getGrandtillingName()))){
                if (name.endsWith("III"))
                    name = "grand-tilling3";
                else if (name.endsWith("II"))
                    name = "grand-tilling2";
                else if (name.endsWith("I"))
                    name = "grand-tilling1";
            }else if (name.contains(MessageUtils.getColoredMessage(FarmingPlus.getPlugin().getMainConfigManager().getFarmerstepName()))){
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
            anvilMeta.setDisplayName(MessageUtils.getColoredMessage("&eEnchanting item Cost:"));
            List<String> anvilLore = new ArrayList<String>();
            anvilLore.add(MessageUtils.getColoredMessage("&7Cost: "));

            // ✓ - ✔ - ✗ - ✘ //

            if (costE != null) {
                if (costE.getXpLevels() > 0)
                    if (player.getLevel() >= costE.getXpLevels())
                        anvilLore.add(MessageUtils.getColoredMessage("&a✔ - " + String.valueOf(costE.getXpLevels()) + " XP Levels"));
                    else
                        anvilLore.add(MessageUtils.getColoredMessage("&c✘ - " + String.valueOf(costE.getXpLevels()) + " XP Levels"));
                if (costE.getMoney() > 0)
                    if (VaultUtils.getMoney(player) >= costE.getMoney())
                        anvilLore.add(MessageUtils.getColoredMessage("&a✔ - " + VaultUtils.formatCurrencySymbol(costE.getMoney())));
                    else
                        anvilLore.add(MessageUtils.getColoredMessage("&c✘ - " + VaultUtils.formatCurrencySymbol(costE.getMoney())));
                if (!costE.getItems().isEmpty()) {
                    anvilLore.add(MessageUtils.getColoredMessage("&e- Items:"));
                    for (String itemE : costE.getItems()) {
                        String[] parts = itemE.split("[\\[\\]]");

                        String[] partsA = itemE.split(" ");
                        itemE = partsA[0];
                        int amount = Integer.parseInt(partsA[1]);
                        if (amount <= 0)
                            amount = 1;

                        String ItemName = parts[1];

                        if (player.getInventory().containsAtLeast(new ItemStack(Material.valueOf(itemE.toUpperCase())), amount))
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
