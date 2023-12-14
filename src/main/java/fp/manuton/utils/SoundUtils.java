package fp.manuton.utils;

import org.bukkit.Sound;

public class SoundUtils {

    public static Sound getSoundFromString(String soundName) {
        try {
            return Sound.valueOf(soundName.toUpperCase());
        } catch (IllegalArgumentException e) {
            return null;
        }
    }
}
