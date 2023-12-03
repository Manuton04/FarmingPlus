package fp.manuton.utils;

import org.bukkit.ChatColor;

public class MessageUtils {

    // Can use MessageUtils.getColoredMessage(Message);
    public static String getColoredMessage(String message){
        return ChatColor.translateAlternateColorCodes('&', message);
    }
}
