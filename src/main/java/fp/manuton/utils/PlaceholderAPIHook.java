package fp.manuton.utils;

import fp.manuton.FarmingPlus;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.entity.Player;

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
    public String onPlaceholderRequest(Player player, String identifier) {
        if (identifier.equals("prefix")){ // Usage: %farmingplus_prefix%
            return plugin.getMainConfigManager().getPluginPrefix();
        }
        if (identifier.contains("total_rewards_top_")){ // Usage: %farmingplus_total_rewards_top_number%
            String[] parts = identifier.split("_");
            int position = Integer.parseInt(parts[parts.length-1]);
            if (plugin.getMainConfigManager().getTopPlayer(position) == null)
                return plugin.getMainConfigManager().getNoTop().replace("%farmingplus_top_number%", String.valueOf(position));
            return plugin.getMainConfigManager().getTopPlayer(position);
        }
        if (identifier.equals("total_rewards")){ // Usage: %farmingplus_total_rewards%
            if (player != null)
                return String.valueOf(plugin.getMainConfigManager().getRewardsCounter(player.getUniqueId()).getTotal());
        }
        if (identifier.equals("top_position")){ // Usage: %farmingplus_top_position%
            if (player != null)
                return String.valueOf(plugin.getMainConfigManager().getTopPlayer(player));
        }

        return null; // Placeholder is unknown by the Expansion
    }
}
