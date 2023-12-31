package fp.manuton.utils;

import fp.manuton.FarmingPlus;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

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
        message = PlaceholderAPI.setPlaceholders(p, message);
        message = getColoredMessage(message);
        return message;

    }

    public static boolean isStringEmpty(String string){
        return string.isEmpty() || string.isBlank();
    }

}
