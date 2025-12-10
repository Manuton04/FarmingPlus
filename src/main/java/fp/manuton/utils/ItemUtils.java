package fp.manuton.utils;

import fp.manuton.FarmingPlus;
import fp.manuton.costs.Cost;
import fp.manuton.enchantments.CustomEnchantments;
import fp.manuton.enchantments.EnchantFp;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

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

    public static String getItemType(ItemStack item) {
        Material type = item.getType();
        if (hoes.contains(type)) {
            return "hoe";
        } else if (axes.contains(type)) {
            return "axe";
        } else if (pickaxes.contains(type)) {
            return "pickaxe";
        } else if (shovels.contains(type)) {
            return "shovel";
        } else if (swords.contains(type)) {
            return "sword";
        } else if (boots.contains(type)) {
            return "boot";
        } else {
            return "other";
        }
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
        cropsR.add(Material.BEETROOTS);
        cropsR.add(Material.NETHER_WART);
        cropsR.add(Material.MELON);
        cropsR.add(Material.PUMPKIN);
        cropsR.add(Material.SUGAR_CANE);
        cropsR.add(Material.CACTUS);
        cropsR.add(Material.COCOA);

    }

    public static Material getCrop(Material material){
        if (material.equals(Material.WHEAT))
            return Material.WHEAT_SEEDS;
        if (material.equals(Material.POTATOES))
            return Material.POTATO;
        if (material.equals(Material.CARROTS))
            return Material.CARROT;
        if (material.equals(Material.BEETROOTS))
            return Material.BEETROOT_SEEDS;
        if (material.equals(Material.NETHER_WART))
            return Material.NETHER_WART;
        if (material.equals(Material.COCOA))
            return Material.COCOA_BEANS;
        return null;
    }

    public static boolean canPayEnchantment(Player player, Cost cost){
        if (player.hasPermission("fp.bypass.costs"))
            return true;

        if (Bukkit.getPluginManager().getPlugin("Vault") != null) {
            if (cost.getMoney() > 0) {
                if (VaultUtils.getMoney(player) <= cost.getMoney())
                    return false;
            }
        }

        if (cost.getXpLevels() > 0){
            if (player.getLevel() <= cost.getXpLevels())
                return false;
        }

        if (cost.getItems().isEmpty())
            return true;

        for (String item : cost.getItems()){
            String[] itemSplit = item.split(" ");
            Material material = Material.getMaterial(itemSplit[0]);
            int amount = Integer.parseInt(itemSplit[1]);
            if (material == null)
                continue;
            if (amount <= 0)
                amount = 1;
            if (!player.getInventory().contains(material, amount))
                return false;
        }

        return true;
    }

    public static ItemStack enchantedItem(ItemStack item, EnchantFp ench, int level){

        /*
        if (item.hasItemMeta())
            if (item.getItemMeta().hasEnchant(ench) && item.getItemMeta().getEnchantLevel(ench) == level)
                return item;

         */

        if (item.hasItemMeta()) {
            PersistentDataContainer data = item.getItemMeta().getPersistentDataContainer();

            if (data.has(new NamespacedKey(FarmingPlus.getPlugin(), "fpEnchant."+ench.getName().replace(" ", "").replace("'", "")), PersistentDataType.STRING)) {
                String enchant = data.get(new NamespacedKey(FarmingPlus.getPlugin(), "fpEnchant."+ench.getName().replace(" ", "").replace("'", "")), PersistentDataType.STRING);
                int levelEnch = data.get(new NamespacedKey(FarmingPlus.getPlugin(), "fpEnchantLevel."+ench.getName().replace(" ", "").replace("'", "")), PersistentDataType.INTEGER);

                if (enchant.equals(ench.getName()) && levelEnch == level)
                    return item;
            }
        }

        boolean enchanted = false;
        //item.addUnsafeEnchantment(ench, level);
        ItemMeta meta = item.getItemMeta();
        meta.getPersistentDataContainer().set(new NamespacedKey(FarmingPlus.getPlugin(), "fpEnchant."+ench.getName().replace(" ", "").replace("'", "")), PersistentDataType.STRING, ench.getName());
        meta.getPersistentDataContainer().set(new NamespacedKey(FarmingPlus.getPlugin(), "fpEnchantLevel."+ench.getName().replace(" ", "").replace("'", "")), PersistentDataType.INTEGER, level);
        List<String> lore = new ArrayList<String>();
        String loreToAdd = null;
        boolean existsLore = false;
        FarmingPlus plugin = FarmingPlus.getPlugin();
        if (level == 1) {
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
            if (ench.equals(CustomEnchantments.IRRIGATE))
                loreToAdd = MessageUtils.getColoredMessage(plugin.getMainConfigManager().getIrrigateNameLore());
        } else if (level == 2) {
            if (ench.equals(CustomEnchantments.FARMERSTEP))
                loreToAdd = MessageUtils.getColoredMessage(plugin.getMainConfigManager().getFarmerstepNameLore2());
            if (ench.equals(CustomEnchantments.GRANDTILLING))
                loreToAdd = MessageUtils.getColoredMessage(plugin.getMainConfigManager().getGrandtillingNameLore2());
        } else if (level == 3) {
            if (ench.equals(CustomEnchantments.FARMERSTEP))
                loreToAdd = MessageUtils.getColoredMessage(plugin.getMainConfigManager().getFarmerstepNameLore3());
            if (ench.equals(CustomEnchantments.GRANDTILLING))
                loreToAdd = MessageUtils.getColoredMessage(plugin.getMainConfigManager().getGrandtillingNameLore3());
        }

        if (meta.hasLore()) {
            for (String already : meta.getLore()) {
                if (already.equals(loreToAdd)) {
                    existsLore = true;
                    break;
                }
            }
        }

        if (!existsLore) {
            List<String> updatedLore;
            if (meta.hasLore())
                updatedLore = new ArrayList<>(meta.getLore());
            else
                updatedLore = new ArrayList<>();

            boolean loreUpdated = false;
            for (int i = 0; i < updatedLore.size(); i++) {
                String already = updatedLore.get(i);
                if (already.contains(loreToAdd) || loreToAdd.contains(already)) {
                    updatedLore.set(i, loreToAdd);
                    loreUpdated = true;
                    break;
                }
            }
            if (!loreUpdated) {
                updatedLore.add(loreToAdd);
            }
            meta.setLore(updatedLore);
            item.setItemMeta(meta);
        }

        if (meta.hasLore())
            lore.addAll(meta.getLore());
        meta.setEnchantmentGlintOverride(true);
        meta.setLore(lore);
        item.setItemMeta(meta);

        return item;
    }

    public static void enchantItem(List<Material> enchantable, Player player, EnchantFp ench, int level){
        ItemStack item = new ItemStack(player.getItemInHand());
        int slot = player.getInventory().getHeldItemSlot();
        /*
        if (item.hasItemMeta())
            if (item.getItemMeta().hasEnchant(ench) && item.getItemMeta().getEnchantLevel(ench) == level) {
                player.sendMessage(MessageUtils.getColoredMessage(FarmingPlus.prefix+"&cThis item already has that enchantment!"));
                return;
            }

         */

        if (item.hasItemMeta()) {
            PersistentDataContainer data = item.getItemMeta().getPersistentDataContainer();

            if (data.has(new NamespacedKey(FarmingPlus.getPlugin(), "fpEnchant."+ench.getName().replace(" ", "").replace("'", "")), PersistentDataType.STRING)) {
                String enchant = data.get(new NamespacedKey(FarmingPlus.getPlugin(), "fpEnchant."+ench.getName().replace(" ", "").replace("'", "")), PersistentDataType.STRING);
                int levelEnch = data.get(new NamespacedKey(FarmingPlus.getPlugin(), "fpEnchantLevel."+ench.getName().replace(" ", "").replace("'", "")), PersistentDataType.INTEGER);

                if (enchant.equals(ench.getName()) && levelEnch == level) {
                    player.sendMessage(MessageUtils.getColoredMessage(FarmingPlus.prefix+"&cThis item already has that enchantment!"));
                    return;
                }
            }
        }
        boolean enchanted = false;
        for (Material type : enchantable) {
            if (item.getType().equals(type)) {
                player.getInventory().setItem(slot, new ItemStack(Material.AIR));
                //item.addUnsafeEnchantment(ench, level);
                ItemMeta meta = item.getItemMeta();
                meta.getPersistentDataContainer().set(new NamespacedKey(FarmingPlus.getPlugin(), "fpEnchant."+ench.getName().replace(" ", "").replace("'", "")), PersistentDataType.STRING, ench.getName());
                meta.getPersistentDataContainer().set(new NamespacedKey(FarmingPlus.getPlugin(), "fpEnchantLevel."+ench.getName().replace(" ", "").replace("'", "")), PersistentDataType.INTEGER, level);
                meta.setEnchantmentGlintOverride(true);
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
                    if (ench.equals(CustomEnchantments.IRRIGATE))
                        loreToAdd = MessageUtils.getColoredMessage(plugin.getMainConfigManager().getIrrigateNameLore());
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
                    for (String already : meta.getLore()) {
                        if (already.equals(loreToAdd)) {
                            existsLore = true;
                            break;
                        }
                    }
                }

                if (!existsLore) {
                    List<String> updatedLore;
                    if (meta.hasLore())
                        updatedLore = new ArrayList<>(meta.getLore());
                    else
                        updatedLore = new ArrayList<>();
                    boolean loreUpdated = false;
                    for (int i = 0; i < updatedLore.size(); i++) {
                        String already = updatedLore.get(i);
                        if (already.contains(loreToAdd) || loreToAdd.contains(already)) {
                            updatedLore.set(i, loreToAdd);
                            loreUpdated = true;
                            break;
                        }
                    }
                    if (!loreUpdated) {
                        updatedLore.add(loreToAdd);
                    }
                    meta.setLore(updatedLore);
                    item.setItemMeta(meta);
                }
                
                if (meta.hasLore())
                    lore.addAll(meta.getLore());
                meta.setLore(lore);
                meta.setEnchantmentGlintOverride(true);
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

            int level = tool.getItemMeta().getEnchantLevel(Enchantment.UNBREAKING);

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

            int level = boots.getItemMeta().getEnchantLevel(Enchantment.UNBREAKING);

            float chance =  100.0f / (level + 1); // Level 0 = 100% / 1 = 50% / 2 = 33%  / 3 = 25% chances of using durability

            if (Math.random() * 100 < chance){
                int damageToDeal = 1;
                damageable.setDamage(damageable.getDamage() + damageToDeal);

                boots.setItemMeta((ItemMeta) damageable);
                player.getInventory().setBoots(boots);
            }
        }
    }

    public static String getReadableMaterialName(Material material) {
        String name = material.name();
        name = name.replace('_', ' ').toLowerCase();
        name = capitalizeFully(name);
        return name;
    }

    public static String capitalizeFully(String str) {
        String[] words = str.split(" ");
        StringBuilder sb = new StringBuilder();

        for (String word : words) {
            sb.append(Character.toUpperCase(word.charAt(0)))
                    .append(word.substring(1))
                    .append(" ");
        }

        return sb.toString().trim();
    }

    public static boolean hasCustomEnchant(ItemStack item, EnchantFp enchant) {
        if (item.hasItemMeta()) {
            PersistentDataContainer data = item.getItemMeta().getPersistentDataContainer();
            if (data.has(new NamespacedKey(FarmingPlus.getPlugin(), "fpEnchant."+enchant.getName().replace(" ", "").replace("'", "")), PersistentDataType.STRING)) {
                String enchantName = data.get(new NamespacedKey(FarmingPlus.getPlugin(), "fpEnchant."+enchant.getName().replace(" ", "").replace("'", "")), PersistentDataType.STRING);
                return enchantName.equals(enchant.getName());
            }
        }
        return false;
    }

    public static int getCustomEnchantLevel(ItemStack item,EnchantFp enchant) {
        if (item.hasItemMeta()) {
            PersistentDataContainer data = item.getItemMeta().getPersistentDataContainer();
            if (data.has(new NamespacedKey(FarmingPlus.getPlugin(), "fpEnchantLevel."+enchant.getName().replace(" ", "").replace("'", "")), PersistentDataType.INTEGER)) {
                return data.get(new NamespacedKey(FarmingPlus.getPlugin(), "fpEnchantLevel."+enchant.getName().replace(" ", "").replace("'", "")), PersistentDataType.INTEGER);
            }
        }
        return 0;
    }

}
