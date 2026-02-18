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
     * Returns true if the player is allowed to build/break at the given location,
     * considering an enchantment-specific flag key (e.g. Towny custom metadata flags).
     * Default implementation ignores the key and delegates to {@link #canBuild(Player, Location)}.
     *
     * @param player the player performing the action
     * @param location the block location being modified
     * @param enchantmentKey the enchantment-specific flag key (e.g. "farmingplus_replenish")
     * @return true if the action is allowed
     */
    default boolean canBuild(Player player, Location location, String enchantmentKey) {
        return canBuild(player, location);
    }

    /**
     * @return human-readable name of this protection system (e.g. "WorldGuard", "Towny")
     */
    String getName();

    /**
     * @return true if this protection plugin is installed and enabled on the server
     */
    boolean isEnabled();
}
