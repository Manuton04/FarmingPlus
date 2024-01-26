package fp.manuton;

import fp.manuton.SQL.ConnectionMySQL;
import fp.manuton.SQL.MySQLData;
import fp.manuton.commands.MainCommand;
import fp.manuton.config.CustomConfig;
import fp.manuton.config.MainConfigManager;
import fp.manuton.enchantments.CustomEnchantments;
import fp.manuton.events.GuiListener;
import fp.manuton.events.PlayerListener;
import fp.manuton.events.RewardsListener;
import fp.manuton.guis.EnchantGui;
import fp.manuton.guis.FarmersStepGui;
import fp.manuton.utils.*;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.Connection;

public class FarmingPlus extends JavaPlugin {
    public static String prefix = null;
    private final String version = getDescription().getVersion();
    private static FarmingPlus plugin;
    private MainConfigManager mainConfigManager;
    private static Connection connectionMySQL;
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

        connectionMySQL = null;
        if (mySQLEnabled)
            connectionMySQL = new ConnectionMySQL(mySQLHost, mySQLPort, mySQLDatabase, mySQLUsername, mySQLPassword).getConnection();
        mainConfigManager = new MainConfigManager();
        prefix = getPlugin().getMainConfigManager().getPluginPrefix();
        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null){
            new PlaceholderAPIHook().register();
        }
        Bukkit.getConsoleSender().sendMessage(MessageUtils.getColoredMessage("&f&l------------------------------------------------"));
        Bukkit.getConsoleSender().sendMessage(MessageUtils.getColoredMessage(prefix+"&fHas been enabled. &cVersion: "+version));
        Bukkit.getConsoleSender().sendMessage(MessageUtils.getColoredMessage("&f&l------------------------------------------------"));
        if (connectionMySQL != null && MySQLData.isDatabaseConnected(getConnectionMySQL()))
            Bukkit.getConsoleSender().sendMessage(MessageUtils.translateAll(null, getMainConfigManager().getConnectedMySQL()));
        else
            Bukkit.getConsoleSender().sendMessage(MessageUtils.translateAll(null, getMainConfigManager().getErrorMySQL()));
        if (getMainConfigManager().isEnabledMetrics()){
            int pluginId = 20430; // ID OF PLUGIN IN BSTATS //
            Metrics metrics = new Metrics(this, pluginId);
            Bukkit.getConsoleSender().sendMessage(MessageUtils.getColoredMessage(prefix+"&aMetrics are enabled."));
        }else
            Bukkit.getConsoleSender().sendMessage(MessageUtils.getColoredMessage(prefix+"&cMetrics are not enabled :c."));
        CustomEnchantments.registerAll();
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

    public void onDisable(){
        Bukkit.getConsoleSender().sendMessage(MessageUtils.getColoredMessage("&f&l------------------------------------------------"));
        Bukkit.getConsoleSender().sendMessage(MessageUtils.getColoredMessage(prefix+"&fHas been disabled. &cVersion: "+version));
        Bukkit.getConsoleSender().sendMessage(MessageUtils.getColoredMessage("&f&l------------------------------------------------"));
        boolean saved = true;
        try {
            getMainConfigManager().saveRecordToJson();
            Bukkit.getConsoleSender().sendMessage(MessageUtils.getColoredMessage(prefix+"&aThe rewards records are being saved in a JSON."));
        }catch (Exception e) {
            Bukkit.getConsoleSender().sendMessage(MessageUtils.getColoredMessage(prefix + "&cThe rewards records could not be saved."));
            saved = false;
        }
        if (saved)
            Bukkit.getConsoleSender().sendMessage(MessageUtils.getColoredMessage(prefix+"&aThe rewards records have been saved."));
    }

    public static FarmingPlus getPlugin(){
        return plugin;
    }

    public void registerCommands(){
        this.getCommand("farmingplus").setExecutor(new MainCommand());
    }

    public void registerEvents(){
        getServer().getPluginManager().registerEvents(new PlayerListener(), this);
        getServer().getPluginManager().registerEvents(new GuiListener(), this);
        getServer().getPluginManager().registerEvents(new RewardsListener(), this);
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

    public static Connection getConnectionMySQL(){
        return connectionMySQL;
    }

}
