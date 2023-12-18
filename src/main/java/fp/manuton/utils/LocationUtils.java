package fp.manuton.utils;

import org.bukkit.Location;

import java.util.ArrayList;
import java.util.List;

public class LocationUtils {

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
}
