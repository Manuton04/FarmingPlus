package fp.manuton;

import fp.manuton.commands.MainCommand;
import fp.manuton.config.MainConfigManager;
import fp.manuton.enchantments.CustomEnchantments;
import fp.manuton.events.GuiListener;
import fp.manuton.events.PlayerListener;
import fp.manuton.events.RewardsListener;
import fp.manuton.guis.EnchantGui;
import fp.manuton.guis.FarmersStepGui;
import fp.manuton.utils.*;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class FarmingPlus extends JavaPlugin {
    public static String prefix = null;
    private final String version = getDescription().getVersion();
    private static FarmingPlus plugin;
    private MainConfigManager mainConfigManager;
    private final String link = ""; // MODRINTH LINK //
    private final int pluginIdSpigot = 1; // ADD PLUGIN ID SPIGOT //

    public void onEnable(){
        plugin = this;
        mainConfigManager = new MainConfigManager();
        prefix = getPlugin().getMainConfigManager().getPluginPrefix();
        Bukkit.getConsoleSender().sendMessage(MessageUtils.getColoredMessage("&f&l------------------------------------------------"));
        Bukkit.getConsoleSender().sendMessage(MessageUtils.getColoredMessage(prefix+"&fHas been enabled. &cVersion: "+version));
        Bukkit.getConsoleSender().sendMessage(MessageUtils.getColoredMessage("&f&l------------------------------------------------"));
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

}
