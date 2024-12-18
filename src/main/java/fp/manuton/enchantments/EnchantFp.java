package fp.manuton.enchantments;

import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;

public class EnchantFp {
    private String namespace;
    private String name;
    private int maxLvl;

    public EnchantFp (String namespace, String name, int maxLvl) {
        this.namespace = namespace;
        this.name = name;
        this.maxLvl = maxLvl;
    }

    public String getName() {
        return name;
    }

    public int getMaxLevel() {
        return maxLvl;
    }

    public int getStartLevel() {
        return 0;
    }

    public EnchantmentTarget getItemTarget() {
        return null;
    }

    public boolean isTreasure() {
        return false;
    }

    public boolean isCursed() {
        return false;
    }

    public boolean conflictsWith(Enchantment enchantment) {
        return false;
    }

    public boolean canEnchantItem(ItemStack itemStack) {
        return false;
    }

}
