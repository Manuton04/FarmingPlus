package fp.manuton.utils;

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
