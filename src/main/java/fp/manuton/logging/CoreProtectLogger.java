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

    /**
     * Attempts to obtain the CoreProtect API instance.
     * @return the CoreProtectAPI if available and compatible, null otherwise
     */
    private CoreProtectAPI getAPI() {
        if (api != null) {
            return api;
        }

        Plugin plugin = Bukkit.getPluginManager().getPlugin("CoreProtect");
        if (plugin == null || !(plugin instanceof CoreProtect)) {
            return null;
        }

        CoreProtectAPI coreProtectAPI = ((CoreProtect) plugin).getAPI();
        if (coreProtectAPI == null || !coreProtectAPI.isEnabled() || coreProtectAPI.APIVersion() < 11) {
            return null;
        }

        this.api = coreProtectAPI;
        return api;
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
