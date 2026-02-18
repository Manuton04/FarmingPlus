package fp.manuton.protection;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

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
            try {
                Bukkit.getLogger().info("[FarmingPlus] Hooked into " + check.getName() + "!");
            } catch (Exception ignored) {
                // Bukkit not available (unit tests)
            }
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

    /**
     * Returns true if ALL active protection systems allow the player to build at the location,
     * considering an enchantment-specific flag key for per-enchantment overrides (e.g. Towny flags).
     *
     * @param player the player performing the action
     * @param location the block location being modified
     * @param enchantmentKey the enchantment-specific flag key (e.g. "farmingplus_replenish")
     * @return true if building is allowed by all active protections
     */
    public boolean canBuild(Player player, Location location, String enchantmentKey) {
        for (ProtectionCheck check : checks) {
            if (check.isEnabled() && !check.canBuild(player, location, enchantmentKey)) {
                return false;
            }
        }
        return true;
    }
}
