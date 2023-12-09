package fp.manuton.config;

import fp.manuton.FarmingPlus;
import org.bukkit.configuration.file.FileConfiguration;

public class MainConfigManager {

    private CustomConfig configFile;
    private FarmingPlus plugin;
    private boolean enabledMetrics;
    private String noPermissionCommand;
    private String noPermissionAction;
    private String reloadedConfig;

    public MainConfigManager(FarmingPlus plugin){
        this.plugin = plugin;
        configFile = new CustomConfig("config.yml", null, plugin);
        configFile.registerConfig();
        loadConfig();
    }

    public void loadConfig(){
        FileConfiguration config = configFile.getConfig();
        enabledMetrics = config.getBoolean("config.enabled_metrics");
        noPermissionCommand = config.getString("messages.no-permission-command");
        noPermissionAction = config.getString("messages.no-permission-action");
        reloadedConfig = config.getString("messages.reloaded-config");
    }

    public void reloadConfig(){
        configFile.reloadConfig();
        loadConfig();
    }

    public boolean isEnabledMetrics() {
        return enabledMetrics;
    }

    public String getNoPermissionCommand() {
        return noPermissionCommand;
    }

    public String getNoPermissionAction() {
        return noPermissionAction;
    }

    public String getReloadedConfig() {
        return reloadedConfig;
    }
}
