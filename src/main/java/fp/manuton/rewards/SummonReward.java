package fp.manuton.rewards;

import fp.manuton.utils.MessageUtils;
import fp.manuton.utils.SoundUtils;
import io.lumine.mythic.api.mobs.MythicMob;
import io.lumine.mythic.bukkit.BukkitAdapter;
import io.lumine.mythic.bukkit.MythicBukkit;
import io.lumine.mythic.core.mobs.ActiveMob;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.List;

public class SummonReward extends Reward{

    private List<String> messages;
    private String entity;
    private String sound;
    private int amount;
    private int level;

    public SummonReward(List<String> crops, double chance, List<String> messages, String entity, String sound, int amount, int level){
        super(crops, chance);
        this.amount = amount;
        this.messages = messages;
        this.entity = entity;
        this.sound = sound;
        this.level = level;
    }

    public List<String> getMessages() {
        return messages;
    }

    public String getEntity() {
        return entity;
    }

    public String getSound() {
        return sound;
    }

    public int getAmount() {
        return amount;
    }

    public int getLevel() {
        return level;
    }

    @Override
    public void give(Player player) {
        Sound sound1 = SoundUtils.getSoundFromString(getSound());
        if (sound1 != null)
            player.playSound(player.getLocation(), sound1, 1, 1);
        for (String message : getMessages()){
            player.sendMessage(MessageUtils.getColoredMessage(message));
        }
        MythicMob mob = MythicBukkit.inst().getMobManager().getMythicMob("SkeletalKnight").orElse(null);
        Location spawnLocation = player.getLocation();

        for (int i = 0; i < getAmount(); i++) {
            if(mob != null){
                ActiveMob knight = mob.spawn(BukkitAdapter.adapt(spawnLocation),getLevel());
                Entity entity = knight.getEntity().getBukkitEntity();
            }else{
                player.getWorld().spawnEntity(spawnLocation, EntityType.valueOf(getEntity()));
            }
        }
    }
}
