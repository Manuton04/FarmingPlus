package fp.manuton.rewards;

import org.bukkit.entity.Player;

import java.util.List;

public class ItemReward extends Reward{

    private List<String> items;
    private List<String> messages;
    private String sound;

    public ItemReward(List<String> crops, double chance, List<String> items, List<String> messages, String sound) {
        super(crops, chance, "Item");
        this.items = items;
        this.messages = messages;
        this.sound = sound;
    }

    public List<String> getItems() {
        return items;
    }

    public List<String> getMessages() {
        return messages;
    }

    public String getSound() {
        return sound;
    }

    @Override
    public void give(Player player) {
        // Implement the logic to give this reward to a player
    }
}
