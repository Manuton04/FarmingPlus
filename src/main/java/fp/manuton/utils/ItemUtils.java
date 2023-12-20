package fp.manuton.utils;

import fp.manuton.FarmingPlus;
import fp.manuton.enchantments.CustomEnchantments;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class ItemUtils {
    public static List<Material> hoes = new ArrayList<>();
    public static List<Material> axes = new ArrayList<>();
    public static List<Material> pickaxes = new ArrayList<>();
    public static List<Material> shovels = new ArrayList<>();
    public static List<Material> swords = new ArrayList<>();
    public static List<Material> boots = new ArrayList<>();
    public static List<Material> crops = new ArrayList<>();
    public static List<Material> cropsR = new ArrayList<>();

    public static void getMaterials(){
        hoes.add(Material.WOODEN_HOE);
        hoes.add(Material.STONE_HOE);
        hoes.add(Material.IRON_HOE);
        hoes.add(Material.GOLDEN_HOE);
        hoes.add(Material.DIAMOND_HOE);
        hoes.add(Material.NETHERITE_HOE);

        axes.add(Material.WOODEN_AXE);
        axes.add(Material.STONE_AXE);
        axes.add(Material.IRON_AXE);
        axes.add(Material.GOLDEN_AXE);
        axes.add(Material.DIAMOND_AXE);
        axes.add(Material.NETHERITE_AXE);

        pickaxes.add(Material.WOODEN_PICKAXE);
        pickaxes.add(Material.STONE_PICKAXE);
        pickaxes.add(Material.IRON_PICKAXE);
        pickaxes.add(Material.GOLDEN_PICKAXE);
        pickaxes.add(Material.DIAMOND_PICKAXE);
        pickaxes.add(Material.NETHERITE_PICKAXE);

        shovels.add(Material.WOODEN_SHOVEL);
        shovels.add(Material.STONE_SHOVEL);
        shovels.add(Material.IRON_SHOVEL);
        shovels.add(Material.GOLDEN_SHOVEL);
        shovels.add(Material.DIAMOND_SHOVEL);
        shovels.add(Material.NETHERITE_SHOVEL);

        swords.add(Material.WOODEN_SWORD);
        swords.add(Material.STONE_SWORD);
        swords.add(Material.IRON_SWORD);
        swords.add(Material.GOLDEN_SWORD);
        swords.add(Material.DIAMOND_SWORD);
        swords.add(Material.NETHERITE_SWORD);

        boots.add(Material.LEATHER_BOOTS);
        boots.add(Material.CHAINMAIL_BOOTS);
        boots.add(Material.IRON_BOOTS);
        boots.add(Material.GOLDEN_BOOTS);
        boots.add(Material.DIAMOND_BOOTS);
        boots.add(Material.NETHERITE_BOOTS);
    }

    public static void getCropsStep(){
        crops.add(Material.WHEAT_SEEDS);
        crops.add(Material.POTATO);
        crops.add(Material.CARROT);
        crops.add(Material.BEETROOT_SEEDS);
        crops.add(Material.NETHER_WART);
        crops.add(Material.MELON_SEEDS);
        crops.add(Material.PUMPKIN_SEEDS);
    }

    public static void getCropsRewards(){
        cropsR.add(Material.WHEAT);
        cropsR.add(Material.POTATOES);
        cropsR.add(Material.CARROTS);
        cropsR.add(Material.BEETROOT);
        cropsR.add(Material.NETHER_WART);
        cropsR.add(Material.MELON);
        cropsR.add(Material.PUMPKIN);
        cropsR.add(Material.SUGAR_CANE);
        cropsR.add(Material.CACTUS);
        cropsR.add(Material.COCOA_BEANS);

    }

    public static void enchantItem(List<Material> enchantable, Player player, Enchantment ench, int level){
        ItemStack item = new ItemStack(player.getItemInHand());
        int slot = player.getInventory().getHeldItemSlot();
        if (item.hasItemMeta())
            if (item.getItemMeta().hasEnchant(ench) && item.getItemMeta().getEnchantLevel(ench) == level) {
                player.sendMessage(MessageUtils.getColoredMessage(FarmingPlus.prefix+"&cThis item already has that enchantment!"));
                return;
            }
        boolean enchanted = false;
        for (Material type : enchantable) {
            if (item.getType().equals(type)) {
                player.getInventory().setItem(slot, new ItemStack(Material.AIR));
                item.addUnsafeEnchantment(ench, level);
                ItemMeta meta = item.getItemMeta();
                List<String> lore = new ArrayList<String>();
                String loreToAdd = null;
                boolean existsLore = false;
                FarmingPlus plugin = FarmingPlus.getPlugin();
                if (level == 1){
                    if (ench.equals(CustomEnchantments.REPLENISH))
                        loreToAdd = MessageUtils.getColoredMessage(plugin.getMainConfigManager().getReplenishNameLore());
                    if (ench.equals(CustomEnchantments.DELICATE))
                        loreToAdd = MessageUtils.getColoredMessage(plugin.getMainConfigManager().getDelicateNameLore());
                    if (ench.equals(CustomEnchantments.FARMERSGRACE))
                        loreToAdd = MessageUtils.getColoredMessage(plugin.getMainConfigManager().getFarmersgraceNameLore());
                    if (ench.equals(CustomEnchantments.FARMERSTEP))
                        loreToAdd = MessageUtils.getColoredMessage(plugin.getMainConfigManager().getFarmerstepNameLore1());
                    if (ench.equals(CustomEnchantments.GRANDTILLING))
                        loreToAdd = MessageUtils.getColoredMessage(plugin.getMainConfigManager().getGrandtillingNameLore1());
                }else if (level == 2){
                    if (ench.equals(CustomEnchantments.FARMERSTEP))
                        loreToAdd = MessageUtils.getColoredMessage(plugin.getMainConfigManager().getFarmerstepNameLore2());
                    if (ench.equals(CustomEnchantments.GRANDTILLING))
                        loreToAdd = MessageUtils.getColoredMessage(plugin.getMainConfigManager().getGrandtillingNameLore2());
                }else if (level == 3){
                    if (ench.equals(CustomEnchantments.FARMERSTEP))
                        loreToAdd = MessageUtils.getColoredMessage(plugin.getMainConfigManager().getFarmerstepNameLore3());
                    if (ench.equals(CustomEnchantments.GRANDTILLING))
                        loreToAdd = MessageUtils.getColoredMessage(plugin.getMainConfigManager().getGrandtillingNameLore3());
                }

                if (meta.hasLore()) {
                    for (String already : meta.getLore()){
                        if (already.equals(loreToAdd))
                            existsLore = true;
                    }
                }
                if (!existsLore)
                    lore.add(loreToAdd);
                if (meta.hasLore())
                    lore.addAll(meta.getLore());
                meta.setLore(lore);
                item.setItemMeta(meta);

                player.getInventory().setItem(slot, item);
                player.sendMessage(MessageUtils.getColoredMessage(FarmingPlus.prefix+"&aItem enchanted with "+loreToAdd+"."));
                enchanted = true;
                return;
            }
        }
        if (!enchanted)
            player.sendMessage(MessageUtils.getColoredMessage(FarmingPlus.prefix+"&cThat enchantment can't be applied on this item!"));

    }

    // ItemUtils.setDurability //
    public static void setDurability(ItemStack tool, Player player){
        if (player.hasPermission("fp.bypass.durability-damage"))
            return;
        if (tool.getType().getMaxDurability() > 0 && tool.getItemMeta() instanceof Damageable) {
            Damageable damageable = (Damageable) tool.getItemMeta();

            int level = tool.getItemMeta().getEnchantLevel(Enchantment.DURABILITY);

            float chance =  100.0f / (level + 1); // Level 0 = 100% / 1 = 50% / 2 = 33%  / 3 = 25% chances of using durability

            if (Math.random() * 100 < chance){
                int damageToDeal = 1;
                damageable.setDamage(damageable.getDamage() + damageToDeal);

                tool.setItemMeta((ItemMeta) damageable);
                player.getInventory().setItemInMainHand(tool);
            }
        }
    }

    // Set durability of boots//
    public static void setDurabilityBoots(ItemStack boots, Player player){
        if (player.hasPermission("fp.bypass.durability-damage"))
            return;
        if (boots.getType().getMaxDurability() > 0 && boots.getItemMeta() instanceof Damageable) {
            Damageable damageable = (Damageable) boots.getItemMeta();

            int level = boots.getItemMeta().getEnchantLevel(Enchantment.DURABILITY);

            float chance =  100.0f / (level + 1); // Level 0 = 100% / 1 = 50% / 2 = 33%  / 3 = 25% chances of using durability

            if (Math.random() * 100 < chance){
                int damageToDeal = 1;
                damageable.setDamage(damageable.getDamage() + damageToDeal);

                boots.setItemMeta((ItemMeta) damageable);
                player.getInventory().setBoots(boots);
            }
        }
    }


}
