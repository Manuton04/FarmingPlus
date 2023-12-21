package fp.manuton.utils;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class LocationUtils {

    public static String getCardinalDirection(Player player){
        double rotation = (player.getLocation().getYaw() - 90) % 360;
        if (rotation < 0)
            rotation += 360.0;
        // I want to only get NORTH, SOUTH, EAST, WEST
        if (0 <= rotation && rotation < 45)
            return "W";
        else if (45 <= rotation && rotation < 135)
            return "N";
        else if (135 <= rotation && rotation < 225)
            return "E";
        else if (225 <= rotation && rotation < 315)
            return "S";
        else if (315 <= rotation && rotation < 360)
            return "W";
        else
            return null;
    }

    public static List<Location> getRadiusBlocks(Location location, int radius, int yDifference){
        List<Location> blocks = new ArrayList<>();
        int x = location.getBlockX();
        int y = location.getBlockY() + yDifference;
        int z = location.getBlockZ();
        for (int i = x - radius; i <= x + radius; i++){
            for (int k = z - radius; k <= z + radius; k++){
                blocks.add(new Location(location.getWorld(), i, y, k));
            }

        }
        return blocks;
    }

    public static List<Location> getRowBlocks(Location location, int distance, String direction, int yDifference){
        List<Location> blocks = new ArrayList<>();
        int x = location.getBlockX();
        int y = location.getBlockY() + yDifference;
        int z = location.getBlockZ();
        switch (direction){
            case "N":
                for (int i = z; i >= z - distance; i--){
                    blocks.add(new Location(location.getWorld(), x, y, i));
                }
                break;
            case "S":
                for (int i = z; i <= z + distance; i++){
                    blocks.add(new Location(location.getWorld(), x, y, i));
                }
                break;
            case "E":
                for (int i = x; i <= x + distance; i++){
                    blocks.add(new Location(location.getWorld(), i, y, z));
                }
                break;
            case "W":
                for (int i = x; i >= x - distance; i--){
                    blocks.add(new Location(location.getWorld(), i, y, z));
                }
                break;
            default:
                return null;
        }

        return blocks;
    }


}
