package fp.manuton.costs;

import fp.manuton.utils.VaultUtils;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class Cost {

    private int xpLevels;
    private double money;
    private List<String> items;

    public Cost(int xpLevels, double money, List<String> items) {
        this.xpLevels = xpLevels;
        this.money = money;
        this.items = items;

    }

    public int getXpLevels() {
        return xpLevels;
    }

    public double getMoney() {
        return money;
    }

    public List<String> getItems() {
        return items;
    }

    public List<String> getCost(){
        List<String> list = new ArrayList<>();
        if (getXpLevels() > 0)
            list.add("Exp Levels: " + getXpLevels());
        if (getMoney() > 0)
            list.add("Money: " + getMoney());
        if (!getItems().isEmpty())
            list.add("Items: " + getItems());
        return list;
    }

    public void execute(Player player){
        player.setLevel(player.getLevel() - getXpLevels());
        VaultUtils.extract(player, getMoney());
        for (String item : getItems()){
            String[] parts = item.split(" ");
            item = parts[0];
            int amount = Integer.parseInt(parts[1]);
            if (amount <= 0)
                amount = 1;

            for (int i = 0; i < amount; i++) {
                Inventory playerInventory = player.getInventory();
                player.getInventory().remove(new ItemStack(Material.valueOf(item.toUpperCase())));
            }
        }
    }
}