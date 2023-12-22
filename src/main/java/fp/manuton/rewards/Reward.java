package fp.manuton.rewards;

import org.bukkit.entity.Player;

import java.util.List;

public abstract class Reward {
    private List<String> crops;
    private String type;
    private double chance;

    public Reward(List<String> crops, double chance, String type) {
        this.crops = crops;
        this.chance = chance;
        this.type = type;
    }

    public List<String> getCrops() {
        return crops;
    }

    public double getChance() {
        return chance;
    }

    public String getType() {
        return type;
    }

    public abstract void give(Player player);

}

