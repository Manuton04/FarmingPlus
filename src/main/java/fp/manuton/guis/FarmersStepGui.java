package fp.manuton.guis;

import fp.manuton.FarmingPlus;
import fp.manuton.utils.ItemUtils;
import fp.manuton.utils.MessageUtils;
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

public class FarmersStepGui {

    public static void bootGui(Player player, ItemStack boots){
        FarmingPlus plugin = FarmingPlus.getPlugin();
        Inventory inventory = Bukkit.createInventory(player, 9 * 4, MessageUtils.getColoredMessage(plugin.getMainConfigManager().getFarmerstepGuiTitle()));
        ItemStack empty = new ItemStack(Material.valueOf(plugin.getMainConfigManager().getFarmerstepGuiEmptySlot()));
        ItemMeta emptyMeta = empty.getItemMeta();
        emptyMeta.setDisplayName(" ");
        empty.setItemMeta(emptyMeta);
        ItemStack close = new ItemStack(Material.BARRIER);
        ItemMeta closeMeta = close.getItemMeta();
        closeMeta.setDisplayName(MessageUtils.getColoredMessage("&cClose"));
        close.setItemMeta(closeMeta);
        ItemStack putItem = new ItemStack(Material.PAPER);
        ItemMeta putItemMeta = putItem.getItemMeta();
        List<String> putItemlore = new ArrayList<String>();
        putItemMeta.setDisplayName(MessageUtils.getColoredMessage("&aSelect a crop"));
        putItemlore.add(MessageUtils.getColoredMessage("&7Click a crop to"));
        putItemlore.add(MessageUtils.getColoredMessage("&7select it!"));
        putItemMeta.setLore(putItemlore);
        putItem.setItemMeta(putItemMeta);
        ItemStack bootsT = boots.clone();
        ItemMeta bootsTMeta = bootsT.getItemMeta();
        bootsTMeta.getLore().clear();
        bootsT.setItemMeta(bootsTMeta);

        for (int i = 0; i <= 35; i++){
            if (i != 13)
                inventory.setItem(i, empty);
        }
        ItemMeta bootsMeta = boots.getItemMeta();
        PersistentDataContainer data = bootsMeta.getPersistentDataContainer();

        if (data.has(new NamespacedKey(FarmingPlus.getPlugin(), "crop"), PersistentDataType.STRING)){
            String dataType = data.get(new NamespacedKey(FarmingPlus.getPlugin(), "crop"), PersistentDataType.STRING);
            ItemStack crop = new ItemStack(Material.valueOf(dataType));
            for (Material type: ItemUtils.crops){
                if (type == crop.getType()) {
                    inventory.setItem(13, crop);
                    break;
                }
            }
        }
        inventory.setItem(11, bootsT);
        inventory.setItem(15, putItem);
        inventory.setItem(31, close);

        player.openInventory(inventory);
        player.setMetadata("BootsMenu", new FixedMetadataValue(plugin, inventory));
    }


}
