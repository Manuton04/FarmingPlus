package fp.manuton.guis;

import org.bukkit.entity.Player;

import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Tracks which FarmingPlus GUI each player currently has open.
 *
 * <p>This replaces the previous {@code Player#setMetadata} flags. The Bukkit metadata API was
 * deprecated in favour of {@code PersistentDataContainer} and custom caches because entries keyed
 * on a player were never released when that player disconnected. Only a boolean flag is needed
 * here — the old code stored the {@link org.bukkit.inventory.Inventory} but never read it back —
 * so a plain set of UUIDs is enough, and {@link #clear(Player)} guarantees the entry is dropped
 * on quit.</p>
 */
public final class GuiSessions {

    private static final Set<UUID> enchantMenu = ConcurrentHashMap.newKeySet();
    private static final Set<UUID> confirmMenu = ConcurrentHashMap.newKeySet();
    private static final Set<UUID> bootsMenu = ConcurrentHashMap.newKeySet();

    private GuiSessions() {
    }

    /**
     * Checks whether the player has the main enchanting GUI open.
     *
     * @param player the player to check
     * @return true if the enchanting GUI is open
     */
    public static boolean hasEnchantMenu(Player player) {
        return enchantMenu.contains(player.getUniqueId());
    }

    /**
     * Marks the main enchanting GUI as open for the player.
     *
     * @param player the player opening the GUI
     */
    public static void openEnchantMenu(Player player) {
        enchantMenu.add(player.getUniqueId());
    }

    /**
     * Clears the main enchanting GUI flag for the player.
     *
     * @param player the player closing the GUI
     */
    public static void closeEnchantMenu(Player player) {
        enchantMenu.remove(player.getUniqueId());
    }

    /**
     * Checks whether the player is on the enchant confirmation page.
     *
     * @param player the player to check
     * @return true if the confirmation page is open
     */
    public static boolean hasConfirmMenu(Player player) {
        return confirmMenu.contains(player.getUniqueId());
    }

    /**
     * Marks the enchant confirmation page as open for the player.
     *
     * @param player the player opening the page
     */
    public static void openConfirmMenu(Player player) {
        confirmMenu.add(player.getUniqueId());
    }

    /**
     * Clears the enchant confirmation page flag for the player.
     *
     * @param player the player leaving the page
     */
    public static void closeConfirmMenu(Player player) {
        confirmMenu.remove(player.getUniqueId());
    }

    /**
     * Checks whether the player has the Farmer's Step crop selector open.
     *
     * @param player the player to check
     * @return true if the crop selector is open
     */
    public static boolean hasBootsMenu(Player player) {
        return bootsMenu.contains(player.getUniqueId());
    }

    /**
     * Marks the Farmer's Step crop selector as open for the player.
     *
     * @param player the player opening the GUI
     */
    public static void openBootsMenu(Player player) {
        bootsMenu.add(player.getUniqueId());
    }

    /**
     * Clears the Farmer's Step crop selector flag for the player.
     *
     * @param player the player closing the GUI
     */
    public static void closeBootsMenu(Player player) {
        bootsMenu.remove(player.getUniqueId());
    }

    /**
     * Drops every GUI flag held for the player. Called on quit so no entry outlives the session.
     *
     * @param player the player that disconnected
     */
    public static void clear(Player player) {
        UUID uuid = player.getUniqueId();
        enchantMenu.remove(uuid);
        confirmMenu.remove(uuid);
        bootsMenu.remove(uuid);
    }
}
