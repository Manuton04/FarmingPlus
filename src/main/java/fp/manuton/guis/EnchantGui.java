package fp.manuton.guis;

import fp.manuton.FarmingPlus;
import fp.manuton.utils.MessageUtils;
import fp.manuton.utils.SoundUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.metadata.FixedMetadataValue;

import java.util.ArrayList;
import java.util.List;


public class EnchantGui{

    private static FarmingPlus plugin;

    public EnchantGui(FarmingPlus plugin) {
        this.plugin = plugin;
    }

    public static void createGui(Player player){
        ItemStack empty = new ItemStack(Material.valueOf(plugin.getMainConfigManager().getGuiEmptySlot()));
        ItemMeta emptyMeta = empty.getItemMeta();
        emptyMeta.setDisplayName(" ");
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

    public static void enchantGui(Player player, Inventory inventory){
        ItemStack replenish = new ItemStack(Material.ENCHANTED_BOOK);
        ItemMeta replenishMeta = replenish.getItemMeta();
        replenishMeta.setDisplayName(MessageUtils.getColoredMessage(plugin.getMainConfigManager().getReplenishNameLore()));
        ItemStack delicate = new ItemStack(Material.ENCHANTED_BOOK);
        ItemMeta delicateMeta = delicate.getItemMeta();
        ItemStack grandtilling = new ItemStack(Material.ENCHANTED_BOOK);
        ItemMeta grandtillingMeta = grandtilling.getItemMeta();
        ItemStack farmerstep = new ItemStack(Material.ENCHANTED_BOOK);
        ItemMeta farmerstepMeta = farmerstep.getItemMeta();
        ItemStack farmersgrace = new ItemStack(Material.ENCHANTED_BOOK);
        ItemMeta farmersgraceMeta = farmersgrace.getItemMeta();
    }

    public static void enchantGuiBoots(Player player, Inventory inventory){
        ItemStack farmerstep = new ItemStack(Material.ENCHANTED_BOOK);
        ItemMeta farmerstepMeta = farmerstep.getItemMeta();
        ItemStack farmersgrace = new ItemStack(Material.ENCHANTED_BOOK);
        ItemMeta farmersgraceMeta = farmersgrace.getItemMeta();
        ItemStack empty = new ItemStack(Material.valueOf(plugin.getMainConfigManager().getGuiEmptySlot()));
        ItemMeta emptyMeta = empty.getItemMeta();
        emptyMeta.setDisplayName(" ");
        empty.setItemMeta(emptyMeta);

        inventory.setItem(23,empty);
        inventory.setItem(21, farmersgrace);
        inventory.setItem(22, farmerstep);

    }

    public static void enchantGuiEmpty(Player player, Inventory inventory){
        ItemStack empty = new ItemStack(Material.valueOf(plugin.getMainConfigManager().getGuiEmptySlot()));
        ItemMeta emptyMeta = empty.getItemMeta();
        emptyMeta.setDisplayName(" ");
        empty.setItemMeta(emptyMeta);

        ItemStack putItem = new ItemStack(Material.PAPER);
        ItemMeta putItemMeta = putItem.getItemMeta();
        List<String> putItemlore = new ArrayList<String>();
        putItemMeta.setDisplayName(MessageUtils.getColoredMessage("&cEnchant Item"));
        putItemlore.add(MessageUtils.getColoredMessage("&7Place an item to enchant"));
        putItemlore.add(MessageUtils.getColoredMessage("&7in the open slot!"));
        putItemMeta.setLore(putItemlore);
        putItem.setItemMeta(putItemMeta);

        for (int i = 21; i <= 25; i++){
            inventory.setItem(i, empty);
        }
        inventory.setItem(23, putItem);
    }






}
