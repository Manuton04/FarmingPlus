package fp.manuton.enchantments;

import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.inventory.ItemStack;

//With this we can create enchantments that are official to minecraft //
public class EnchantmentWrapper extends Enchantment {

    private final String name;
    private final int MaxLvl;
    private final EnchantmentTarget type;

    public EnchantmentWrapper(String namespace, String name, int lvl, EnchantmentTarget type) {
        super(NamespacedKey.minecraft(namespace));
        this.name = name;
        this.MaxLvl = lvl;
        this.type = type;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public int getMaxLevel() {
        return MaxLvl;
    }

    @Override
    public int getStartLevel() {
        return 0;
    }

    @Override
    public EnchantmentTarget getItemTarget() {
        return type;
    }

    @Override
    public boolean isTreasure() {
        return false;
    }

    @Override
    public boolean isCursed() {
        return false;
    }

    @Override
    public boolean conflictsWith(Enchantment enchantment) {
        return false;
    }

    @Override
    public boolean canEnchantItem(ItemStack itemStack) {
        return false;
    }
}
