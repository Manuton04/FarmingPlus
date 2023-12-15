package fp.manuton.utils;

import org.bukkit.ChatColor;

public class MessageUtils {

    // Can use MessageUtils.getColoredMessage(Message);
    public static String getColoredMessage(String message){
        return ChatColor.translateAlternateColorCodes('&', message);
    }

    public static boolean isStringEmpty(String string){
        if (string.isEmpty() || string.isBlank())
            return true;
        else
            return false;
    }
}
