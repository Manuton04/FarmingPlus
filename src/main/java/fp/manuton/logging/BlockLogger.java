package fp.manuton.logging;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.data.BlockData;

/**
 * Interface for block modification logging systems (CoreProtect, etc.).
 */
public interface BlockLogger {

    /**
     * Logs a block placement made by the plugin on behalf of a player.
     * @param playerName the name of the player who triggered the action
     * @param location the location of the placed block
     * @param type the material type placed
     * @param blockData the block data of the placed block
     */
    void logPlacement(String playerName, Location location, Material type, BlockData blockData);

    /**
     * Logs a block removal made by the plugin on behalf of a player.
     * @param playerName the name of the player who triggered the action
     * @param location the location of the removed block
     * @param type the material type that was removed
     * @param blockData the block data of the removed block
     */
    void logRemoval(String playerName, Location location, Material type, BlockData blockData);

    /**
     * @return true if this logging plugin is installed and enabled on the server
     */
    boolean isEnabled();

    /**
     * @return human-readable name of this logging system
     */
    String getName();
}
