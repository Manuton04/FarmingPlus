package fp.manuton.utils;

import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;

public class ItemUtils {

    // ItemUtils.setDurability //
    public static void setDurability(ItemStack tool, Player player){
        if (tool.getType().getMaxDurability() > 0 && tool.getItemMeta() instanceof Damageable) {
            Damageable damageable = (Damageable) tool.getItemMeta();

            if (damageable.hasDamage() && damageable.getDamage() < tool.getType().getMaxDurability()) {
                int level = 0;
                if (tool.getItemMeta().hasEnchant(Enchantment.DURABILITY))
                    level = tool.getItemMeta().getEnchantLevel(Enchantment.DURABILITY);

                float chance =  100.0f / (level + 1); // Level 0 = 100% / 1 = 50% / 2 = 33%  / 3 = 25% chances of using durability

                if (Math.random() * 100 < chance){
                    int damageToDeal = 1;
                    damageable.setDamage(damageable.getDamage() + damageToDeal);

                    tool.setItemMeta((ItemMeta) damageable);
                    player.getInventory().setItemInMainHand(tool);
                }
            }
        }
    }

}
