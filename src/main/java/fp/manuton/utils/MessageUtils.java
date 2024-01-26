package fp.manuton.utils;

import fp.manuton.FarmingPlus;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.UUID;

public class MessageUtils {

    // Can use MessageUtils.getColoredMessage(Message);
    public static String getColoredMessage(String message){
        if (message != null)
            return ChatColor.translateAlternateColorCodes('&', message);
        else
            return "";
    }

    public static String translateAll(OfflinePlayer p, String message){
        if (message == null)
            return "";

        if (p != null)
            message = message.replace("%player%", p.getName());
        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
            message = PlaceholderAPI.setPlaceholders(p, message);
        }else{
            if (message.contains("%farmingplus_prefix%"))
                message = message.replace("%farmingplus_prefix%", FarmingPlus.prefix);
            if (message.contains("%farmingplus_total_rewards%"))
                message = message.replace("%farmingplus_total_rewards%", String.valueOf(FarmingPlus.getPlugin().getMainConfigManager().getRewardsCounter(p.getUniqueId()).getTotal()));
            if (message.contains("%farmingplus_top_position%"))
                message = message.replace("%farmingplus_top_position%", String.valueOf(FarmingPlus.getPlugin().getMainConfigManager().getTopPlayer(p)));
            if (message.contains("%farmingplus_total_rewards_top_")){ // Usage: %farmingplus_total_rewards_top_number%
                String[] parts = message.split("_");
                String positionString = parts[parts.length-1].replace("%", "");
                int position = Integer.parseInt(positionString);
                boolean hasPlayer = false;
                if (p != null) {
                    for (UUID u : FarmingPlus.getPlugin().getMainConfigManager().getAllRewardsCounterUuids()) {
                        if (p.getUniqueId().equals(u)) {
                            hasPlayer = true;
                            break;
                        }
                    }
                }
                if (!hasPlayer)
                    message = FarmingPlus.getPlugin().getMainConfigManager().getNoTop().replace("%farmingplus_top_number%", String.valueOf(position));
                if (FarmingPlus.getPlugin().getMainConfigManager().getTopPlayer(position) == null)
                    message = FarmingPlus.getPlugin().getMainConfigManager().getNoTop().replace("%farmingplus_top_number%", String.valueOf(position));
                else
                    message = FarmingPlus.getPlugin().getMainConfigManager().getTopPlayer(position);
            }
        }
        message = getColoredMessage(message);
        return message;
    }

    public static boolean isStringEmpty(String string){
        return string.isEmpty() || string.isBlank();
    }

}
