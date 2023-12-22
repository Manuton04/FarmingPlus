package fp.manuton.utils;

import org.bukkit.Sound;

public class SoundUtils {

    public static Sound getSoundFromString(String soundName) {

        if (soundName == null || soundName.isEmpty() || soundName.isBlank())
            return null;
        try {
            return Sound.valueOf(soundName.toUpperCase());
        } catch (IllegalArgumentException e) {
            return null;
        }
    }
}
