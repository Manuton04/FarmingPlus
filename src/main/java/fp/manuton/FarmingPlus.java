package fp.manuton;

import fp.manuton.SQL.ConnectionPool;
import fp.manuton.SQL.MySQLData;
import fp.manuton.commands.MainCommand;
import fp.manuton.config.CustomConfig;
import fp.manuton.config.MainConfigManager;
import fp.manuton.enchantments.CustomEnchantments;
import fp.manuton.events.*;
import fp.manuton.guis.EnchantGui;
import fp.manuton.guis.FarmersStepGui;
import fp.manuton.logging.BlockLoggerManager;
import fp.manuton.logging.CoreProtectLogger;
import fp.manuton.protection.ProtectionManager;
import fp.manuton.protection.TownyProtection;
import fp.manuton.protection.WorldGuardProtection;
import fp.manuton.utils.*;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.Connection;
import java.sql.SQLException;

public class FarmingPlus extends JavaPlugin {
    public static String prefix = null;
    private final String version = getDescription().getVersion();
    private static FarmingPlus plugin;
    private MainConfigManager mainConfigManager;
    private static ConnectionPool connectionPool;  // HikariCP connection pool
    private ProtectionManager protectionManager;
    private BlockLoggerManager blockLoggerManager;
    private final String link = "https://modrinth.com/plugin/farmingplus";
    private final int pluginIdSpigot = 114643; // ADD PLUGIN ID SPIGOT //

    public void onEnable(){
        plugin = this;
        CustomConfig configFile;
        configFile = new CustomConfig("config.yml", null, FarmingPlus.getPlugin());
        configFile.registerConfig();
        FileConfiguration config = configFile.getConfig();
        boolean mySQLEnabled = config.getBoolean("config.mysql.enabled");
        String mySQLHost = config.getString("config.mysql.host");
        int mySQLPort = config.getInt("config.mysql.port");
        String mySQLDatabase = config.getString("config.mysql.database");
        String mySQLUsername = config.getString("config.mysql.username");
        String mySQLPassword = config.getString("config.mysql.password");

        connectionPool = null;
        if (mySQLEnabled) {
            connectionPool = new ConnectionPool(mySQLHost, mySQLPort, mySQLDatabase, mySQLUsername, mySQLPassword);

            if (connectionPool.isInitialized()) {
                // Create table and index once at startup
                try (Connection conn = connectionPool.getConnection()) {
                    MySQLData.createTableIfNotExists(conn);
                } catch (SQLException e) {
                    Bukkit.getLogger().severe("[FarmingPlus] Failed to create rewards table: " + e.getMessage());
                }

                // Start periodic connection health check (every 3 minutes)
                Bukkit.getScheduler().runTaskTimerAsynchronously(this, () -> {
                    if (connectionPool != null) {
                        boolean isHealthy = connectionPool.validateConnection();
                        if (!isHealthy) {
                            Bukkit.getLogger().warning("[FarmingPlus] Database connection validation failed.");
                        }
                    }
                }, 3600L, 3600L); // 3 minutes (20 ticks/sec * 60 sec * 3 min)
            } else {
                Bukkit.getLogger().severe("[FarmingPlus] MySQL is enabled but connection failed. Plugin will continue without database.");
                connectionPool = null;
            }
        }
        mainConfigManager = new MainConfigManager();
        prefix = getPlugin().getMainConfigManager().getPluginPrefix();
        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null){
            new PlaceholderAPIHook().register();
        }
        Bukkit.getConsoleSender().sendMessage(MessageUtils.getColoredMessage("&f&l------------------------------------------------"));
        Bukkit.getConsoleSender().sendMessage(MessageUtils.getColoredMessage(prefix+"&fHas been enabled. &cVersion: "+version));
        Bukkit.getConsoleSender().sendMessage(MessageUtils.getColoredMessage("&f&l------------------------------------------------"));
        if (isMySQLConnected())
            Bukkit.getConsoleSender().sendMessage(MessageUtils.translateAll(null, getMainConfigManager().getConnectedMySQL()));
        else
            Bukkit.getConsoleSender().sendMessage(MessageUtils.translateAll(null, getMainConfigManager().getErrorMySQL()));
        if (getMainConfigManager().isEnabledMetrics()){
            int pluginId = 20430; // ID OF PLUGIN IN BSTATS //
            Metrics metrics = new Metrics(this, pluginId);
            Bukkit.getConsoleSender().sendMessage(MessageUtils.getColoredMessage(prefix+"&aMetrics are enabled."));
        }else
            Bukkit.getConsoleSender().sendMessage(MessageUtils.getColoredMessage(prefix+"&cMetrics are not enabled :c."));
        //CustomEnchantments.registerAll();
        registerEvents();
        registerCommands();
        registerItemUtils();
        registerEnchantGui();
        registerFarmerStepGui();
        ItemUtils.getMaterials();
        ItemUtils.getCropsStep();
        ItemUtils.getCropsRewards();
        new UpdateChecker(this, pluginIdSpigot).getVersion(versionn -> {
            if (version.equals(versionn)) {
                Bukkit.getConsoleSender().sendMessage(MessageUtils.getColoredMessage(prefix+"&fThere is not a new update available."));
            } else {
                Bukkit.getConsoleSender().sendMessage(MessageUtils.getColoredMessage(prefix+"&fThere is a new update available."));
                Bukkit.getConsoleSender().sendMessage(MessageUtils.getColoredMessage(prefix+"&cVersion: "+versionn+ " &eLink: "+link));
            }
        });
    }

    public void onDisable() {
        Bukkit.getConsoleSender().sendMessage(MessageUtils.getColoredMessage("&f&l------------------------------------------------"));
        Bukkit.getConsoleSender().sendMessage(MessageUtils.getColoredMessage(prefix + "&fHas been disabled. &cVersion: " + version));
        Bukkit.getConsoleSender().sendMessage(MessageUtils.getColoredMessage("&f&l------------------------------------------------"));

        // Shutdown database executor service to prevent memory leaks
        if (mainConfigManager != null) {
            mainConfigManager.shutdown();
        }

        // Close MySQL connection pool if enabled
        if (connectionPool != null) {
            try {
                connectionPool.close();
                Bukkit.getConsoleSender().sendMessage(MessageUtils.getColoredMessage(prefix + "&aMySQL connection pool closed successfully."));
            } catch (Exception e) {
                Bukkit.getConsoleSender().sendMessage(MessageUtils.getColoredMessage(prefix + "&cError closing MySQL connection pool: " + e.getMessage()));
            }
        }

        boolean saved = true;
        if (mainConfigManager != null && mainConfigManager.getEnabledRewards()) {
            try {
                getMainConfigManager().saveRecordToJson();
                Bukkit.getConsoleSender().sendMessage(MessageUtils.getColoredMessage(prefix + "&aThe rewards records are being saved in a JSON."));
            } catch (Exception e) {
                Bukkit.getConsoleSender().sendMessage(MessageUtils.getColoredMessage(prefix + "&cThe rewards records could not be saved."));
                saved = false;
            }
            if (saved)
                Bukkit.getConsoleSender().sendMessage(MessageUtils.getColoredMessage(prefix + "&aThe rewards records have been saved."));
        }
    }

    public static FarmingPlus getPlugin(){
        return plugin;
    }

    public void registerCommands(){
        this.getCommand("farmingplus").setExecutor(new MainCommand());
    }

    public void registerEvents(){
        // Initialize protection system — only load classes if the plugin is present
        // to avoid NoClassDefFoundError when the dependency is not installed
        protectionManager = new ProtectionManager();
        if (Bukkit.getPluginManager().getPlugin("WorldGuard") != null) {
            protectionManager.register(new WorldGuardProtection());
        }
        if (Bukkit.getPluginManager().getPlugin("Towny") != null) {
            protectionManager.register(new TownyProtection());
            // Register custom Towny flags for per-enchantment control
            TownyProtection.registerMetadataFields();
        }

        // Initialize block logging system — same pattern
        blockLoggerManager = new BlockLoggerManager();
        if (Bukkit.getPluginManager().getPlugin("CoreProtect") != null) {
            blockLoggerManager.register(new CoreProtectLogger());
        }

        getServer().getPluginManager().registerEvents(new PlayerListener(protectionManager, blockLoggerManager), this);
        getServer().getPluginManager().registerEvents(new RewardsListener(protectionManager), this);
        getServer().getPluginManager().registerEvents(new GuiListener(), this);
    }

    public void registerItemUtils(){
        new ItemUtils();
    }

    public MainConfigManager getMainConfigManager(){
        return mainConfigManager;
    }

    public void registerEnchantGui(){
        new EnchantGui();

    }

    public void registerFarmerStepGui(){
        new FarmersStepGui();
    }

    /**
     * Get a connection from the pool.
     * IMPORTANT: This throws SQLException, caller must handle it.
     *
     * @return Connection from HikariCP pool
     */
    public static Connection getConnectionMySQL() throws SQLException {
        if (connectionPool == null || !connectionPool.isInitialized()) {
            return null;
        }
        return connectionPool.getConnection();
    }

    public static boolean isMySQLConnected() {
        return connectionPool != null && connectionPool.isInitialized();
    }

    public ProtectionManager getProtectionManager() {
        return protectionManager;
    }

    public BlockLoggerManager getBlockLoggerManager() {
        return blockLoggerManager;
    }

}
