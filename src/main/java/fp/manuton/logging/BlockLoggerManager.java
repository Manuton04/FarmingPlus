package fp.manuton.logging;

import fp.manuton.FarmingPlus;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.data.BlockData;

import java.util.ArrayList;
import java.util.List;

/**
 * Aggregates all registered block loggers and provides config-aware logging methods.
 * Each enchantment's logging can be toggled independently via config.yml.
 */
public class BlockLoggerManager {

    private final List<BlockLogger> loggers = new ArrayList<>();

    /**
     * Registers a block logger. Called during plugin startup.
     * @param logger the logging implementation to register
     */
    public void register(BlockLogger logger) {
        loggers.add(logger);
        if (logger.isEnabled()) {
            try {
                Bukkit.getLogger().info("[FarmingPlus] Hooked into " + logger.getName() + " for block logging!");
            } catch (Exception ignored) {
                // Bukkit not available (unit tests)
            }
        }
    }

    /**
     * Logs a block placement if the corresponding enchantment logging is enabled in config.
     * @param enchantmentKey the config key (e.g. "replenish", "farmers-step")
     * @param playerName the player who triggered the action
     * @param location the location of the placed block
     * @param type the material type placed
     * @param blockData the block data of the placed block
     */
    public void logPlacement(String enchantmentKey, String playerName, Location location, Material type, BlockData blockData) {
        if (!isLoggingEnabled(enchantmentKey)) {
            return;
        }
        for (BlockLogger logger : loggers) {
            if (logger.isEnabled()) {
                logger.logPlacement(playerName, location, type, blockData);
            }
        }
    }

    /**
     * Logs a block removal if the corresponding enchantment logging is enabled in config.
     * @param enchantmentKey the config key (e.g. "replenish", "farmers-step")
     * @param playerName the player who triggered the action
     * @param location the location of the removed block
     * @param type the material type that was removed
     * @param blockData the block data of the removed block
     */
    public void logRemoval(String enchantmentKey, String playerName, Location location, Material type, BlockData blockData) {
        if (!isLoggingEnabled(enchantmentKey)) {
            return;
        }
        for (BlockLogger logger : loggers) {
            if (logger.isEnabled()) {
                logger.logRemoval(playerName, location, type, blockData);
            }
        }
    }

    /**
     * Checks if logging is enabled for a specific enchantment via config.yml.
     * @param enchantmentKey the enchantment key matching config path "coreprotect.log-{key}"
     * @return true if logging is enabled for this enchantment
     */
    private boolean isLoggingEnabled(String enchantmentKey) {
        return FarmingPlus.getPlugin().getMainConfigManager().isCoreProtectLoggingEnabled(enchantmentKey);
    }
}
