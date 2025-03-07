package fp.manuton.enchantments;

import fp.manuton.FarmingPlus;
import fp.manuton.utils.ItemUtils;
import fp.manuton.utils.MessageUtils;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.stream.Collectors;

public class CustomEnchantments {

    public static final EnchantFp REPLENISH = new EnchantFp("replenish", FarmingPlus.getPlugin().getMainConfigManager().getReplenishName(), 1) {
        @Override
        public boolean canEnchantItem(ItemStack item) {
            return ItemUtils.hoes.contains(item.getType()) || ItemUtils.axes.contains(item.getType());
        }
    };
    public static final EnchantFp DELICATE = new EnchantFp("delicate", FarmingPlus.getPlugin().getMainConfigManager().getDelicateName(), 1) {
        @Override
        public boolean canEnchantItem(ItemStack item) {
            return ItemUtils.axes.contains(item.getType());
        }
    };
    public static final EnchantFp GRANDTILLING = new EnchantFp("grandtilling", FarmingPlus.getPlugin().getMainConfigManager().getGrandtillingName(), 3) {
        @Override
        public boolean canEnchantItem(ItemStack item) {
            return ItemUtils.hoes.contains(item.getType());
        }
    };
    public static final EnchantFp FARMERSTEP = new EnchantFp("farmerstep", FarmingPlus.getPlugin().getMainConfigManager().getFarmerstepName(), 3) {
        @Override
        public boolean canEnchantItem(ItemStack item) {
            return ItemUtils.boots.contains(item.getType());
        }
    };
    public static final EnchantFp FARMERSGRACE = new EnchantFp("farmersgrace", FarmingPlus.getPlugin().getMainConfigManager().getFarmersgraceName(), 1) {
        @Override
        public boolean canEnchantItem(ItemStack item) {
            return ItemUtils.boots.contains(item.getType());
        }
    };
    public static final EnchantFp IRRIGATE = new EnchantFp("irrigate", FarmingPlus.getPlugin().getMainConfigManager().getIrrigateName(), 1) {
        @Override
        public boolean canEnchantItem(ItemStack item) {
            return item.getType() == Material.WATER_BUCKET;
        }
    };

    public EnchantFp getEnchant(String name){
        switch (name){
            case "replenish":
                return REPLENISH;
            case "delicate":
                return DELICATE;
            case "grandtilling":
                return GRANDTILLING;
            case "farmerstep":
                return FARMERSTEP;
            case "farmersgrace":
                return FARMERSGRACE;
            case "irrigate":
                return IRRIGATE;
            default:
                return null;
        }
    }


    /*
    public static void registerAll(){
        register(REPLENISH);
        register(DELICATE);
        register(GRANDTILLING);
        register(FARMERSTEP);
        register(FARMERSGRACE);
        register(IRRIGATE);
    }

     */


    /*
    // Checks if the enchantments is already registered //
    public static void register(EnchantFp ench){
        boolean registered = Arrays.stream(Enchantment.values()).collect(Collectors.toList()).contains(ench);

        if (!registered){
            registerEnchantments(ench);
        }
    }
    */


    /*
    // Adds enchantments to server //
    public static void registerEnchantments(Enchantment enchantment){
        boolean registered = true;
        try{
            Field f = Enchantment.class.getDeclaredField("acceptingNew");
            f.setAccessible(true);
            f.set(null, true);
            Enchantment.registerEnchantment(enchantment);
        }catch (Exception e){
            registered = false;
            e.printStackTrace();
        }
        if (registered){
            //
        }
    }

     */

}
