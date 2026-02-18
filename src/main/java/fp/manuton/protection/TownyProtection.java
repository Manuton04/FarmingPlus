package fp.manuton.protection;

import com.palmergames.bukkit.towny.TownyAPI;
import com.palmergames.bukkit.towny.exceptions.KeyAlreadyRegisteredException;
import com.palmergames.bukkit.towny.object.Town;
import com.palmergames.bukkit.towny.object.metadata.BooleanDataField;
import com.palmergames.bukkit.towny.utils.MetaDataUtil;
import com.palmergames.bukkit.towny.utils.PlayerCacheUtil;
import com.palmergames.bukkit.towny.object.TownyPermission;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

/**
 * Towny protection check. Uses PlayerCacheUtil to test if a player can build at a location.
 * Supports per-enchantment custom metadata flags that town mayors can toggle independently.
 */
public class TownyProtection implements ProtectionCheck {

    /** Metadata flag key for the Replenish enchantment. */
    public static final String FLAG_REPLENISH = "farmingplus_replenish";
    /** Metadata flag key for the Farmer's Step enchantment. */
    public static final String FLAG_FARMERSTEP = "farmingplus_farmerstep";
    /** Metadata flag key for the Grand Tilling enchantment. */
    public static final String FLAG_GRANDTILLING = "farmingplus_grandtilling";
    /** Metadata flag key for the Irrigate enchantment. */
    public static final String FLAG_IRRIGATE = "farmingplus_irrigate";

    // Pre-built fields for metadata lookups
    private static final BooleanDataField FIELD_REPLENISH = new BooleanDataField(FLAG_REPLENISH, false);
    private static final BooleanDataField FIELD_FARMERSTEP = new BooleanDataField(FLAG_FARMERSTEP, false);
    private static final BooleanDataField FIELD_GRANDTILLING = new BooleanDataField(FLAG_GRANDTILLING, false);
    private static final BooleanDataField FIELD_IRRIGATE = new BooleanDataField(FLAG_IRRIGATE, false);

    /**
     * Registers custom BooleanDataField metadata fields with Towny so mayors can toggle them
     * per town via {@code /town set meta <key> true}.
     * Must be called after Towny is loaded.
     */
    public static void registerMetadataFields() {
        registerField(new BooleanDataField(FLAG_REPLENISH, false, "FP Replenish"));
        registerField(new BooleanDataField(FLAG_FARMERSTEP, false, "FP Farmer's Step"));
        registerField(new BooleanDataField(FLAG_GRANDTILLING, false, "FP Grand Tilling"));
        registerField(new BooleanDataField(FLAG_IRRIGATE, false, "FP Irrigate"));
    }

    private static void registerField(BooleanDataField field) {
        try {
            TownyAPI.getInstance().registerCustomDataField(field);
        } catch (KeyAlreadyRegisteredException ignored) {
            // Field was already registered (e.g. plugin reload)
        }
    }

    /**
     * Returns the pre-built BooleanDataField for a given flag key, or null if unknown.
     *
     * @param flagKey the metadata flag key
     * @return the BooleanDataField instance, or null
     */
    private BooleanDataField getFieldForKey(String flagKey) {
        return switch (flagKey) {
            case FLAG_REPLENISH -> FIELD_REPLENISH;
            case FLAG_FARMERSTEP -> FIELD_FARMERSTEP;
            case FLAG_GRANDTILLING -> FIELD_GRANDTILLING;
            case FLAG_IRRIGATE -> FIELD_IRRIGATE;
            default -> null;
        };
    }

    /**
     * Checks if a specific enchantment flag is enabled for the town at the given location.
     *
     * @param location the block location to check
     * @param flagKey the metadata flag key (e.g. "farmingplus_replenish")
     * @return true if the town has the flag enabled, false otherwise
     */
    public boolean isFlagEnabled(Location location, String flagKey) {
        Town town = TownyAPI.getInstance().getTown(location);
        if (town == null) {
            return false;
        }
        BooleanDataField field = getFieldForKey(flagKey);
        if (field == null) {
            return false;
        }
        if (MetaDataUtil.hasMeta(town, field)) {
            return MetaDataUtil.getBoolean(town, field);
        }
        return false;
    }

    @Override
    public boolean canBuild(Player player, Location location) {
        if (!isEnabled()) {
            return true;
        }
        Material blockType = location.getBlock().getType();
        return PlayerCacheUtil.getCachePermission(player, location, blockType, TownyPermission.ActionType.BUILD);
    }

    /**
     * Checks if the player can build at the location, considering a per-enchantment Towny flag.
     * If the town has the specific enchantment flag enabled, the action is allowed regardless
     * of normal build permissions. Otherwise falls back to the standard build permission check.
     *
     * @param player the player performing the action
     * @param location the block location being modified
     * @param enchantmentKey the enchantment-specific flag key
     * @return true if the action is allowed
     */
    @Override
    public boolean canBuild(Player player, Location location, String enchantmentKey) {
        if (!isEnabled()) {
            return true;
        }
        // If the town has the specific enchantment flag enabled, allow
        if (isFlagEnabled(location, enchantmentKey)) {
            return true;
        }
        // Otherwise fall back to normal build check
        Material blockType = location.getBlock().getType();
        return PlayerCacheUtil.getCachePermission(player, location, blockType, TownyPermission.ActionType.BUILD);
    }

    @Override
    public String getName() {
        return "Towny";
    }

    @Override
    public boolean isEnabled() {
        Plugin plugin = Bukkit.getPluginManager().getPlugin("Towny");
        return plugin != null && plugin.isEnabled();
    }
}
