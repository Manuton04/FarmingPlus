package fp.manuton.utils;

import fp.manuton.FarmingPlus;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class MessageUtils {

    // Can use MessageUtils.getColoredMessage(Message);
    public static String getColoredMessage(String message){
        if (message != null)
            return ChatColor.translateAlternateColorCodes('&', message);
        else
            return "";
    }

    public static String translatePlayer(Player player, String message){
        if (message != null)
            return message.replace("%player%", player.getName());
        else
            return "";
    }

    public static String translatePrefix(String message){
        if (message != null)
            return message.replace("%prefix%", FarmingPlus.getPlugin().getMainConfigManager().getPluginPrefix());
        else
            return "";
    }

    public static String translateAll(Player player, String message){
        if (message != null) {
            message = getColoredMessage(message);
            message = translatePlayer(player, message);
            message = translatePrefix(message);
            return message;
        }
        else
            return "";
    }

    public static boolean isStringEmpty(String string){
        return string.isEmpty() || string.isBlank();
    }

}
