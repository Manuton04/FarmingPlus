package fp.manuton.utils;

import fp.manuton.FarmingPlus;
import fp.manuton.rewardsCounter.RewardsCounter;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.UUID;

public class PlaceholderAPIHook extends PlaceholderExpansion {

    private final FarmingPlus plugin = FarmingPlus.getPlugin();

    @Override
    public String getIdentifier() {
        return "farmingplus";
    }

    @Override
    public String getAuthor() {
        return "Manuton";
    }

    @Override
    public String getVersion() {
        return plugin.getDescription().getVersion();
    }

    @Override
    public boolean persist() {
        return true; // This is required or else PlaceholderAPI will unregister the Expansion on reload
    }

    @Override
    public String onRequest(OfflinePlayer player, String identifier) {
        if (player == null)
            return "";
        if (identifier.equals("prefix")){ // Usage: %farmingplus_prefix%
            return plugin.getMainConfigManager().getPluginPrefix();
        }
        if (identifier.contains("total_rewards_top_")){ // Usage: %farmingplus_total_rewards_top_number%
            String[] parts = identifier.split("_");
            int position = Integer.parseInt(parts[parts.length-1]);
            boolean hasPlayer = false;
            for (UUID u : plugin.getMainConfigManager().getAllRewardsCounterUuids()){
                if (player.getUniqueId().equals(u)){
                    hasPlayer = true;
                    break;
                }
            }
            if (!hasPlayer)
                return plugin.getMainConfigManager().getNoTop().replace("%farmingplus_top_number%", String.valueOf(position));
            if (plugin.getMainConfigManager().getTopPlayer(position) == null)
                return plugin.getMainConfigManager().getNoTop().replace("%farmingplus_top_number%", String.valueOf(position));
            return plugin.getMainConfigManager().getTopPlayer(position);
        }
        if (identifier.equals("total_rewards")){ // Usage: %farmingplus_total_rewards%
            boolean hasPlayer = false;
            for (UUID u : plugin.getMainConfigManager().getAllRewardsCounterUuids()){
                if (player.getUniqueId().equals(u)){
                    hasPlayer = true;
                    break;
                }
            }
            if (!hasPlayer)
                return "0";
            if (player != null)
                return String.valueOf(plugin.getMainConfigManager().getRewardsCounter(player.getUniqueId()).getTotal());
        }
        if (identifier.equals("top_position")){ // Usage: %farmingplus_top_position%
            boolean hasPlayer = false;
            for (UUID u : plugin.getMainConfigManager().getAllRewardsCounterUuids()){
                if (player.getUniqueId().equals(u)){
                    hasPlayer = true;
                    break;
                }
            }
            if (!hasPlayer)
                return "0";
            if (player != null)
                return String.valueOf(plugin.getMainConfigManager().getTopPlayer(player));
        }

        return null; // Placeholder is unknown by the Expansion
    }
}
