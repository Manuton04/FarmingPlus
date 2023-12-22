package fp.manuton.rewards;

import org.bukkit.entity.Player;

import java.util.List;

public class CommandReward extends Reward {
    private List<String> commands;
    private List<String> messages;
    private String sound;

    public CommandReward(List<String> crops, double chance, List<String> commands, List<String> messages, String sound){
        super(crops, chance, "Command");
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
        // Implement the logic to give this reward to a player
    }
}
