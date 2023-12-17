package fp.manuton.enchantments;

import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentTarget;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.stream.Collectors;

public class CustomEnchantments {

    public static final Enchantment REPLENISH = new EnchantmentWrapper("replenish", "Replenish", 1);
    public static final Enchantment DELICATE = new EnchantmentWrapper("delicate", "Delicate", 1);
    public static final Enchantment GRANDTILLING = new EnchantmentWrapper("grandtilling", "Grand Tilling", 3);
    public static final Enchantment FARMERSTEP = new EnchantmentWrapper("farmerstep", "Farmer´s Step", 3);
    public static final Enchantment FARMERSGRACE = new EnchantmentWrapper("farmersgrace", "Farmer´s Grace", 1);

    public static void registerAll(){
        register(REPLENISH);
        register(DELICATE);
        register(GRANDTILLING);
        register(FARMERSTEP);
        register(FARMERSGRACE);
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
