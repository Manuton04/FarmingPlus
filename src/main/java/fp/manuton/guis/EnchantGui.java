package fp.manuton.guis;

import fp.manuton.FarmingPlus;
import fp.manuton.utils.MessageUtils;
import fp.manuton.utils.SoundUtils;
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
import java.util.List;


public class EnchantGui{

    public static void createGui(Player player){
        if (player.hasMetadata("menuConfirm"))
            player.removeMetadata("menuConfirm", FarmingPlus.getPlugin());
        FarmingPlus plugin = FarmingPlus.getPlugin();
        ItemStack empty = new ItemStack(Material.valueOf(plugin.getMainConfigManager().getGuiEmptySlot()));
        ItemMeta emptyMeta = empty.getItemMeta();
        emptyMeta.setDisplayName(" ");
        PersistentDataContainer emptyContainer = emptyMeta.getPersistentDataContainer();
        emptyContainer.set(new NamespacedKey(FarmingPlus.getPlugin(), "empty"), PersistentDataType.STRING, "yes");
        empty.setItemMeta(emptyMeta);
        Inventory inventory = Bukkit.createInventory(player, 9 * 6, MessageUtils.getColoredMessage(plugin.getMainConfigManager().getGuiTitle()));
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
        inventory.setItem(23, putItem);
        inventory.setItem(28, enchantItem);
        inventory.setItem(49, close);

        player.openInventory(inventory);
        String sound = plugin.getMainConfigManager().getGuiSoundOpen();
        if (SoundUtils.getSoundFromString(sound) != null) {
            float volume = plugin.getMainConfigManager().getVolumeGuiSoundOpen();
            player.playSound(player.getLocation(), SoundUtils.getSoundFromString(sound), volume, 1.0f);
        }
        player.setMetadata("OpenedMenu", new FixedMetadataValue(plugin, inventory));
    }

    public static void enchantGuiBoots(Player player, Inventory inventory){
        if (player.hasMetadata("menuConfirm"))
            player.removeMetadata("menuConfirm", FarmingPlus.getPlugin());
        enchantGuiChange(player, inventory);

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
        farmersgraceMeta.setLore(clickLore);
        PersistentDataContainer farmersGraceContainer = farmersgraceMeta.getPersistentDataContainer();
        farmersGraceContainer.set(new NamespacedKey(FarmingPlus.getPlugin(), "menuItem"), PersistentDataType.STRING, "yes");
        farmersgrace.setItemMeta(farmersgraceMeta);
        ItemStack empty = new ItemStack(Material.valueOf(FarmingPlus.getPlugin().getMainConfigManager().getGuiEmptySlot()));
        ItemMeta emptyMeta = empty.getItemMeta();
        emptyMeta.setDisplayName(" ");
        empty.setItemMeta(emptyMeta);

        inventory.setItem(23,empty);
        inventory.setItem(21, farmersgrace);
        inventory.setItem(22, farmerstep);
    }

    public static void enchantGuiHoes(Player player, Inventory inventory){
        if (player.hasMetadata("menuConfirm"))
            player.removeMetadata("menuConfirm", FarmingPlus.getPlugin());
        enchantGuiChange(player, inventory);

    }

    public static void enchantGuiAxes(Player player, Inventory inventory){
        if (player.hasMetadata("menuConfirm"))
            player.removeMetadata("menuConfirm", FarmingPlus.getPlugin());
        enchantGuiChange(player, inventory);

    }

    public static void enchantGuiWater(Player player, Inventory inventory){
        if (player.hasMetadata("menuConfirm"))
            player.removeMetadata("menuConfirm", FarmingPlus.getPlugin());
        enchantGuiChange(player, inventory);

    }

    public static void enchantGuiEmpty(Player player, Inventory inventory){
        if (player.hasMetadata("menuConfirm"))
            player.removeMetadata("menuConfirm", FarmingPlus.getPlugin());
        enchantGuiChange(player, inventory);
        ItemStack empty = new ItemStack(Material.valueOf(FarmingPlus.getPlugin().getMainConfigManager().getGuiEmptySlot()));
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
        ItemStack putItem = new ItemStack(Material.PAPER);
        ItemMeta putItemMeta = putItem.getItemMeta();
        List<String> putItemlore = new ArrayList<String>();
        putItemMeta.setDisplayName(MessageUtils.getColoredMessage("&cEnchant Item"));
        putItemlore.add(MessageUtils.getColoredMessage("&7Place an item to enchant"));
        putItemlore.add(MessageUtils.getColoredMessage("&7in the open slot!"));
        putItemMeta.setLore(putItemlore);
        putItem.setItemMeta(putItemMeta);
        inventory.setItem(28, enchantItem);
        inventory.setItem(23, putItem);
    }

    public static void enchantGuiConfirm(Player player, Inventory inventory, ItemStack enchant, ItemStack item){
        enchantGuiEmptyFull(player, inventory);

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
        ItemStack anvil = new ItemStack(Material.ANVIL);
        ItemMeta anvilMeta = anvil.getItemMeta();


        inventory.setItem(12, item);
        inventory.setItem(13, enchant);
        inventory.setItem(14, anvil);
        inventory.setItem(30, confirm);
        inventory.setItem(32, cancel);
        player.setMetadata("menuConfirm", new FixedMetadataValue(FarmingPlus.getPlugin(), inventory));
    }

    public static void enchantGuiChange(Player player, Inventory inventory){
        if (player.hasMetadata("menuConfirm"))
            player.removeMetadata("menuConfirm", FarmingPlus.getPlugin());
        ItemStack empty = new ItemStack(Material.valueOf(FarmingPlus.getPlugin().getMainConfigManager().getGuiEmptySlot()));
        ItemMeta emptyMeta = empty.getItemMeta();
        emptyMeta.setDisplayName(" ");
        PersistentDataContainer emptyContainer = emptyMeta.getPersistentDataContainer();
        emptyContainer.set(new NamespacedKey(FarmingPlus.getPlugin(), "empty"), PersistentDataType.STRING, "yes");
        empty.setItemMeta(emptyMeta);

        for (int i = 12; i <= 34; i++){ // 12 to 16 - 21 to 25 - 30 to 34 //
            if (i <= 16 || (i >= 21 && i <= 25) || i >= 30)
                inventory.setItem(i, empty);

        }
    }

    public static void enchantGuiEmptyFull(Player player, Inventory inventory){
        if (player.hasMetadata("menuConfirm"))
            player.removeMetadata("menuConfirm", FarmingPlus.getPlugin());
        ItemStack empty = new ItemStack(Material.valueOf(FarmingPlus.getPlugin().getMainConfigManager().getGuiEmptySlot()));
        ItemMeta emptyMeta = empty.getItemMeta();
        emptyMeta.setDisplayName(" ");
        PersistentDataContainer emptyContainer = emptyMeta.getPersistentDataContainer();
        emptyContainer.set(new NamespacedKey(FarmingPlus.getPlugin(), "empty"), PersistentDataType.STRING, "yes");
        empty.setItemMeta(emptyMeta);

        for (int i = 9; i <= 45; i++){
            inventory.setItem(i, empty);
        }
    }





}
