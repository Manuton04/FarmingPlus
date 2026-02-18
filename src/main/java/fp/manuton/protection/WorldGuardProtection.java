package fp.manuton.protection;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldguard.LocalPlayer;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.flags.Flags;
import com.sk89q.worldguard.protection.regions.RegionContainer;
import com.sk89q.worldguard.protection.regions.RegionQuery;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

/**
 * WorldGuard protection check. Tests the BUILD flag for a player at a given location.
 */
public class WorldGuardProtection implements ProtectionCheck {

    @Override
    public boolean canBuild(Player player, Location location) {
        if (!isEnabled()) {
            return true;
        }

        WorldGuard worldGuard = WorldGuard.getInstance();
        RegionContainer regionContainer = worldGuard.getPlatform().getRegionContainer();
        RegionQuery query = regionContainer.createQuery();

        WorldGuardPlugin pluginW = WorldGuardPlugin.inst();
        LocalPlayer localPlayer = pluginW.wrapPlayer(player);

        ApplicableRegionSet regions = query.getApplicableRegions(BukkitAdapter.adapt(location));
        return regions.testState(localPlayer, Flags.BUILD);
    }

    @Override
    public String getName() {
        return "WorldGuard";
    }

    @Override
    public boolean isEnabled() {
        Plugin plugin = Bukkit.getPluginManager().getPlugin("WorldGuard");
        return plugin != null && plugin.isEnabled();
    }
}
