package fp.manuton.protection;

import org.bukkit.Location;
import org.bukkit.entity.Player;

/**
 * Interface for region protection systems (WorldGuard, Towny, etc.).
 * Each implementation checks whether a player can modify blocks at a given location.
 */
public interface ProtectionCheck {

    /**
     * Returns true if the player is allowed to build/break at the given location.
     * @param player the player performing the action
     * @param location the block location being modified
     * @return true if the action is allowed, false if denied by this protection system
     */
    boolean canBuild(Player player, Location location);

    /**
     * @return human-readable name of this protection system (e.g. "WorldGuard", "Towny")
     */
    String getName();

    /**
     * @return true if this protection plugin is installed and enabled on the server
     */
    boolean isEnabled();
}
