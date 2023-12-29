package fp.manuton.rewards;

import org.bukkit.entity.Player;

import java.util.List;
import java.util.Map;

public abstract class Reward {
    private List<String> crops;
    private double chance;

    public Reward(List<String> crops, double chance) {
        this.crops = crops;
        this.chance = chance;
    }

    public List<String> getCrops() {
        return crops;
    }

    public double getChance() {
        return chance;
    }

    public abstract void give(Player player);

}

