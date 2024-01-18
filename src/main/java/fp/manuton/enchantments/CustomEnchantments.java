package fp.manuton.enchantments;

import fp.manuton.FarmingPlus;
import fp.manuton.utils.MessageUtils;
import org.bukkit.enchantments.Enchantment;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.stream.Collectors;

public class CustomEnchantments {

    public static final Enchantment REPLENISH = new EnchantmentWrapper("replenish", MessageUtils.getColoredMessage(FarmingPlus.getPlugin().getMainConfigManager().getReplenishName()), 1);
    public static final Enchantment DELICATE = new EnchantmentWrapper("delicate", MessageUtils.getColoredMessage(FarmingPlus.getPlugin().getMainConfigManager().getDelicateName()), 1);
    public static final Enchantment GRANDTILLING = new EnchantmentWrapper("grandtilling", MessageUtils.getColoredMessage(FarmingPlus.getPlugin().getMainConfigManager().getGrandtillingName()), 3);
    public static final Enchantment FARMERSTEP = new EnchantmentWrapper("farmerstep", MessageUtils.getColoredMessage(FarmingPlus.getPlugin().getMainConfigManager().getFarmerstepName()), 3);
    public static final Enchantment FARMERSGRACE = new EnchantmentWrapper("farmersgrace", MessageUtils.getColoredMessage(FarmingPlus.getPlugin().getMainConfigManager().getFarmersgraceName()), 1);
    public static final Enchantment IRRIGATE = new EnchantmentWrapper("irrigate", MessageUtils.getColoredMessage(FarmingPlus.getPlugin().getMainConfigManager().getIrrigateName()), 1);

    public static void registerAll(){
        register(REPLENISH);
        register(DELICATE);
        register(GRANDTILLING);
        register(FARMERSTEP);
        register(FARMERSGRACE);
        register(IRRIGATE);
    }


    // Checks if the enchantments is already registered //
    public static void register(Enchantment ench){
        boolean registered = Arrays.stream(Enchantment.values()).collect(Collectors.toList()).contains(ench);

        if (!registered){
            registerEnchantments(ench);
        }
    }

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

}
