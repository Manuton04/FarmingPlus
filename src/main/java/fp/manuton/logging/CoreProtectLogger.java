package fp.manuton.logging;

import net.coreprotect.CoreProtect;
import net.coreprotect.CoreProtectAPI;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.data.BlockData;
import org.bukkit.plugin.Plugin;

/**
 * CoreProtect integration for logging block modifications made by FarmingPlus enchantments.
 * Requires CoreProtect API v11 or higher.
 */
public class CoreProtectLogger implements BlockLogger {

    private CoreProtectAPI api;
    private boolean unavailable;

    /**
     * Attempts to obtain the CoreProtect API instance.
     * @return the CoreProtectAPI if available and compatible, null otherwise
     */
    private CoreProtectAPI getAPI() {
        if (api != null) {
            return api;
        }
        if (unavailable) {
            return null;
        }

        Plugin plugin = Bukkit.getPluginManager().getPlugin("CoreProtect");
        // isEnabled() must be checked before any CoreProtect class is referenced below: a
        // CoreProtect that failed to start is still registered here, but its classes can no
        // longer be linked, and touching one would throw NoClassDefFoundError.
        if (plugin == null || !plugin.isEnabled()) {
            return null;
        }

        try {
            if (!(plugin instanceof CoreProtect)) {
                return null;
            }

            CoreProtectAPI coreProtectAPI = ((CoreProtect) plugin).getAPI();
            if (coreProtectAPI == null || !coreProtectAPI.isEnabled() || coreProtectAPI.APIVersion() < 11) {
                return null;
            }

            this.api = coreProtectAPI;
            return api;
        } catch (LinkageError error) {
            // CoreProtect is present but its classes cannot be linked, typically because it does
            // not support this server version. Give up for the rest of the session rather than
            // retrying on every logged block.
            unavailable = true;
            Bukkit.getLogger().warning("[FarmingPlus] CoreProtect is installed but its API could not be "
                    + "loaded (" + error.getMessage() + "). Block logging will be disabled.");
            return null;
        }
    }

    @Override
    public void logPlacement(String playerName, Location location, Material type, BlockData blockData) {
        CoreProtectAPI coreAPI = getAPI();
        if (coreAPI != null) {
            coreAPI.logPlacement("#farmingplus-" + playerName, location, type, blockData);
        }
    }

    @Override
    public void logRemoval(String playerName, Location location, Material type, BlockData blockData) {
        CoreProtectAPI coreAPI = getAPI();
        if (coreAPI != null) {
            coreAPI.logRemoval("#farmingplus-" + playerName, location, type, blockData);
        }
    }

    @Override
    public boolean isEnabled() {
        return getAPI() != null;
    }

    @Override
    public String getName() {
        return "CoreProtect";
    }
}
