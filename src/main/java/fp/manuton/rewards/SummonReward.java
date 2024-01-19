package fp.manuton.rewards;

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
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Random;

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
            player.sendMessage(MessageUtils.translateAll(player, message));
        }

        int quantity = getAmount();
        int level = getLevel();
        if (quantity < 1)
            quantity = 1;
        if (level < 1)
            level = 1;

        Location playerLocation = player.getLocation();
        Random random = new Random();

        for (int i = 0; i < quantity; i++) {
            // Generate a random offset within a 5 block radius
            double offsetX = random.nextDouble() * 10 - 5;
            double offsetZ = random.nextDouble() * 10 - 5;

            // Make sure the offset is not 0
            if (offsetX == 0) offsetX = 1;
            if (offsetZ == 0) offsetZ = 1;

            // Create the new spawn location
            Location spawnLocation = playerLocation.clone().add(offsetX, 0, offsetZ);

            // If the spawn location is in the air, find the nearest solid block below
            while (spawnLocation.getBlock().getType().isAir()) {
                spawnLocation.subtract(0, 1, 0);
            }
            spawnLocation.add(0,1,0);

            // Spawn the mob
            if (Bukkit.getPluginManager().getPlugin("MythicMobs") != null) {
                MythicMob mob = MythicBukkit.inst().getMobManager().getMythicMob(getEntity()).orElse(null);
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
            }else {
                try{
                    player.getWorld().spawnEntity(spawnLocation, EntityType.valueOf(getEntity()));
                }catch(IllegalArgumentException exp){
                    break;
                }
            }
        }
    }
}
