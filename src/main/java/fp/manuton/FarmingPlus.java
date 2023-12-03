package fp.manuton;

import fp.manuton.commands.MainCommand;
import fp.manuton.enchantments.CustomEnchantments;
import fp.manuton.events.PlayerListener;
import fp.manuton.utils.MessageUtils;
import fp.manuton.utils.Metrics;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class FarmingPlus extends JavaPlugin {
    public static String prefix = "&6&l[&2&lFarmingPlus&6&l]";
    private String version = getDescription().getVersion();

    public void onEnable(){
        CustomEnchantments.registerAll();
        int pluginId = 20430; // <-- Replace with the id of your plugin!
        Metrics metrics = new Metrics(this, pluginId);
        registerEvents();
        registerCommands();


        Bukkit.getConsoleSender().sendMessage(MessageUtils.getColoredMessage("&f&l------------------------------------------------"));
        Bukkit.getConsoleSender().sendMessage(MessageUtils.getColoredMessage(prefix+" &fHas been enabled. &cVersion: "+version));
        Bukkit.getConsoleSender().sendMessage(MessageUtils.getColoredMessage("&f&l------------------------------------------------"));
    }

    public void onDisable(){
        Bukkit.getConsoleSender().sendMessage(MessageUtils.getColoredMessage("&f&l------------------------------------------------"));
        Bukkit.getConsoleSender().sendMessage(MessageUtils.getColoredMessage(prefix+" &fHas been disabled. &cVersion: "+version));
        Bukkit.getConsoleSender().sendMessage(MessageUtils.getColoredMessage("&f&l------------------------------------------------"));
    }

    public void registerCommands(){
        this.getCommand("farmingplus").setExecutor(new MainCommand());
    }

    public void registerEvents(){
        getServer().getPluginManager().registerEvents(new PlayerListener(), this);
    }


}
