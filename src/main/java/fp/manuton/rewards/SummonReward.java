package fp.manuton.rewards;

import fp.manuton.FarmingPlus;
import fp.manuton.utils.MessageUtils;
import fp.manuton.utils.SoundUtils;
import io.lumine.mythic.api.mobs.MythicMob;
import io.lumine.mythic.bukkit.BukkitAdapter;
import io.lumine.mythic.bukkit.MythicBukkit;
import io.lumine.mythic.core.mobs.ActiveMob;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Mob;
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

        Location spawnLocation = player.getLocation();
        int quantity = getAmount();
        int level = getLevel();
        if (quantity < 1)
            quantity = 1;
        if (level < 1)
            level = 1;

        if (Bukkit.getPluginManager().getPlugin("MythicMobs") != null) {
            MythicMob mob = MythicBukkit.inst().getMobManager().getMythicMob(getEntity()).orElse(null);
            for (int i = 0; i < quantity; i++) {
                if(mob != null){
                    ActiveMob knight = mob.spawn(BukkitAdapter.adapt(spawnLocation),level);
                    Entity entity = knight.getEntity().getBukkitEntity();
                }else{
                    try{
                        player.getWorld().spawnEntity(spawnLocation, EntityType.valueOf(getEntity()));
                    }catch(IllegalArgumentException exp){
                        break;
                    }
                }
            }
        }else {
            for (int i = 0; i < quantity; i++) {
                try{
                    player.getWorld().spawnEntity(spawnLocation, EntityType.valueOf(getEntity()));
                }catch(IllegalArgumentException exp){
                    break;
                }
            }
        }
    }
}
