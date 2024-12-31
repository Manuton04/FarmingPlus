package fp.manuton.utils;

import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.Registry;
import org.bukkit.Sound;

public class SoundUtils {

    public static Sound getSoundFromString(String soundName) {
        if (soundName == null || soundName.isEmpty() || soundName.isBlank())
            return null;
        try {
            NamespacedKey key = NamespacedKey.minecraft(soundName.toUpperCase());
            Sound sound = Registry.SOUNDS.get(key);
            /*if (sound != null) {
                Bukkit.getConsoleSender().sendMessage("Sound found: " + sound.toString());
            } else {
                Bukkit.getConsoleSender().sendMessage("Sound not found: " + soundName);
            }
             */
            return sound;
        } catch (IllegalArgumentException e) {
            //Bukkit.getConsoleSender().sendMessage("Invalid sound name: " + soundName);
            return null;
        }
    }
}