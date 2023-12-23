package fp.manuton.rewards;

import fp.manuton.utils.MessageUtils;
import fp.manuton.utils.SoundUtils;
import fp.manuton.utils.VaultUtils;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import java.util.List;

public class MoneyReward extends Reward {
    // Add fields for money reward properties
    private double amount;
    private List<String> messages;
    private String sound;

    public MoneyReward(List<String> crops, double chance, double amount, List<String> messages, String sound) {
        super(crops, chance);
        this.amount = amount;
        this.messages = messages;
    }

    public List<String> getMessages() {
        return messages;
    }

    public String getSound() {
        return sound;
    }

    public double getAmount() {
        return amount;
    }

    @Override
    public void give(Player player) {
        Sound sound1 = SoundUtils.getSoundFromString(getSound());
        if (sound1 != null)
            player.playSound(player.getLocation(), sound1, 1, 1);
        for (String message : getMessages()){
            player.sendMessage(MessageUtils.translateAll(player, message));
        }
        VaultUtils.deposit(player, getAmount());


    }


}
