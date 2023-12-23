package fp.manuton.rewards;

import fp.manuton.utils.MessageUtils;
import fp.manuton.utils.SoundUtils;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import java.util.List;

public class CommandReward extends Reward {
    private List<String> commands;
    private List<String> messages;
    private String sound;

    public CommandReward(List<String> crops, double chance, List<String> commands, List<String> messages, String sound){
        super(crops, chance);
        this.commands = commands;
        this.messages = messages;
        this.sound = sound;
    }

    public List<String> getCommands() {
        return commands;
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
            player.sendMessage(MessageUtils.translateAll(player, message));
        }
        for (String command : getCommands()){
            Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), MessageUtils.translatePlayer(player, command));
        }
    }
}
