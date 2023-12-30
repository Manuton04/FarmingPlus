package fp.manuton.utils;

import fp.manuton.FarmingPlus;
import me.clip.placeholderapi.PlaceholderAPI;
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

    public static String translateAll(Player p, String message){
        if (message == null)
            return "";

        message = PlaceholderAPI.setPlaceholders(p, message);
        message = getColoredMessage(message);
        return message;

    }

    public static boolean isStringEmpty(String string){
        return string.isEmpty() || string.isBlank();
    }

}
