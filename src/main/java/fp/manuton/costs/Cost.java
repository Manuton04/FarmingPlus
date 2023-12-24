package fp.manuton.costs;

import org.bukkit.entity.Player;

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

    public void execute(Player player){

    }

}