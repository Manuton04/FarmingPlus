package fp.manuton.guis;

import fp.manuton.FarmingPlus;
import fp.manuton.config.MainConfigManager;
import fp.manuton.costs.Cost;
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

            List<String> clickLore = new ArrayList<String>();
            clickLore.add(MessageUtils.getColoredMessage("&eClick to see!"));

            farmerstepMeta.setLore(clickLore);
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

        }else if (Page.equals("axe")){
            if (player.hasMetadata("menuConfirm"))
                player.removeMetadata("menuConfirm", FarmingPlus.getPlugin());

        }else if (Page.equals("water")){
            if (player.hasMetadata("menuConfirm"))
                player.removeMetadata("menuConfirm", FarmingPlus.getPlugin());

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
