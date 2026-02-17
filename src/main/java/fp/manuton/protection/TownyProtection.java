package fp.manuton.protection;

import com.palmergames.bukkit.towny.utils.PlayerCacheUtil;
import com.palmergames.bukkit.towny.object.TownyPermission;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

/**
 * Towny protection check. Uses PlayerCacheUtil to test if a player can build at a location.
 */
public class TownyProtection implements ProtectionCheck {

    @Override
    public boolean canBuild(Player player, Location location) {
        if (!isEnabled()) {
            return true;
        }

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
