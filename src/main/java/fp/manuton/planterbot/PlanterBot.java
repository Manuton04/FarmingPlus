package fp.manuton.planterbot;

import fp.manuton.FarmingPlus;
import fp.manuton.utils.ItemUtils;
import fp.manuton.utils.LocationUtils;
import fp.manuton.utils.MessageUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;

import java.util.ArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static fp.manuton.SQL.MySQLData.loadRewardsFromDatabase;
import static io.lumine.mythic.bukkit.utils.Players.spawnParticle;

public class PlanterBot {
    private int radius;
    private int cooldown;
    private Material crop;
    private Location location;
    private String headTexture;
    private Material growBlock;

    public PlanterBot(int radius, int cooldown, String cropS, Location location, String headTexture, String growBlock) {
        this.radius = radius;
        if (cooldown > 5)
            this.cooldown = 5;
        else
            this.cooldown = cooldown;
        this.crop = ItemUtils.getCrop(Material.valueOf(cropS));
        this.location = location;
        this.headTexture = headTexture;
        this.growBlock = Material.valueOf(growBlock);
    }

    public int getRadius() {
        return radius;
    }

    public int getCooldown() {
        return cooldown;
    }

    public Material getCrop() {
        return crop;
    }

    public Location getLocation() {
        return location;
    }

    public String getHeadTexture() {
        return headTexture;
    }

    public void setRadius(int radius) {
        this.radius = radius;
    }

    public void setCooldown(int cooldown) {
        this.cooldown = cooldown;
    }

    public void setCrop(String cropS) {
        ItemUtils.getCrop(Material.valueOf(cropS));
    }


    public Material getGrowBlock() {
        return growBlock;
    }

    public void InitializeBotTask() {
        ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
        executorService.scheduleAtFixedRate(() -> {
            plant();
        }, 0, cooldown, TimeUnit.SECONDS);
    }


    public void plant(){
        ArrayList<Location> blocks = new ArrayList<>(LocationUtils.getRadiusBlocksSurface(location, radius));
        for (Location block : blocks){
            if (block.getBlock().getType() == Material.AIR && block.getBlock().getRelative(0, -1, 0).getType() == growBlock){
                try {
                    block.getWorld().spawnParticle(Particle.valueOf("HAPPY_VILLAGER"), block.add(0.5, 0.5, 0.5), 1);
                } catch (Exception e) {
                }
                block.getBlock().setType(crop);
            }
        }
    }

}
