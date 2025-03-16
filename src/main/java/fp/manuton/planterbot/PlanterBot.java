package fp.manuton.planterbot;

import fp.manuton.utils.ItemUtils;
import org.bukkit.Location;
import org.bukkit.Material;

public class PlanterBot {
    private int radius;
    private int cooldown;
    private Material crop;
    private Location location;
    private String headTexture;
    private Material growBlock;

    public PlanterBot(int radius, int cooldown, String cropS, Location location, String headTexture, String growBlock) {
        this.radius = radius;
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
}
