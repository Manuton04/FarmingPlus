package fp.manuton.rewards;

import fp.manuton.utils.MessageUtils;
import fp.manuton.utils.SoundUtils;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class ItemReward extends Reward{

    private List<String> items;
    private List<String> messages;
    private String sound;

    public ItemReward(List<String> crops, double chance, List<String> items, List<String> messages, String sound) {
        super(crops, chance);
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
        Sound sound1 = SoundUtils.getSoundFromString(getSound());
        if (sound1 != null)
            player.playSound(player.getLocation(), sound1, 1, 1);
        for (String message : getMessages()){
            player.sendMessage(MessageUtils.getColoredMessage(message));
        }
        for (String item : getItems()){
            String[] parts = item.split(" ");
            item = parts[0];
            int amount = Integer.parseInt(parts[1]);

            for (int i = 0; i < amount; i++) {
                Inventory playerInventory = player.getInventory();
                int emptySlot = playerInventory.firstEmpty();
                if (emptySlot != -1)
                    player.getInventory().addItem(new ItemStack(Material.valueOf(item.toUpperCase())));
                else
                    player.getWorld().dropItem(player.getLocation(), new ItemStack(Material.valueOf(item.toUpperCase())));
            }
        }
    }
}
