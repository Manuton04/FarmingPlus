package fp.manuton.protection;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

/**
 * Aggregates all registered protection checks and provides a single entry point
 * for verifying whether a player can modify blocks at a given location.
 */
public class ProtectionManager {

    private final List<ProtectionCheck> checks = new ArrayList<>();

    /**
     * Registers a protection check. Called during plugin startup.
     * @param check the protection implementation to register
     */
    public void register(ProtectionCheck check) {
        checks.add(check);
        if (check.isEnabled()) {
            Bukkit.getConsoleSender().sendMessage("[FarmingPlus] Hooked into " + check.getName() + "!");
        }
    }

    /**
     * Returns true if ALL active protection systems allow the player to build at the location.
     * If a protection plugin is not installed/enabled, it is skipped (defaults to allow).
     *
     * @param player the player performing the action
     * @param location the block location being modified
     * @return true if building is allowed by all active protections
     */
    public boolean canBuild(Player player, Location location) {
        for (ProtectionCheck check : checks) {
            if (check.isEnabled() && !check.canBuild(player, location)) {
                return false;
            }
        }
        return true;
    }
}
