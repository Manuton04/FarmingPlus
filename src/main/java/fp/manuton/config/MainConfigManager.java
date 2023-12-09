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
    private String replenishNameLore;
    private String farmersgraceNameLore;
    private String delicateNameLore;
    private String farmerstepNameLore1;
    private String farmerstepNameLore2;
    private String farmerstepNameLore3;
    private String grandtillingNameLore1;
    private String grandtillingNameLore2;
    private String grandtillingNameLore3;

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
        replenishNameLore = config.getString("config.enchantments.replenish.lore-name");
        farmersgraceNameLore = config.getString("config.enchantments.farmers-grace.lore-name");
        delicateNameLore = config.getString("config.enchantments.delicate.lore-name");
        farmerstepNameLore1 = config.getString("config.enchantments.farmer-step.lore-name1");
        farmerstepNameLore2 = config.getString("config.enchantments.farmer-step.lore-name2");
        farmerstepNameLore3 = config.getString("config.enchantments.farmer-step.lore-name3");
        grandtillingNameLore1 = config.getString("config.enchantments.grand-tilling.lore-name1");
        grandtillingNameLore2 = config.getString("config.enchantments.grand-tilling.lore-name2");
        grandtillingNameLore3 = config.getString("config.enchantments.grand-tilling.lore-name3");
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

    public String getReplenishNameLore() {
        return replenishNameLore;
    }

    public String getFarmersgraceNameLore() {
        return farmersgraceNameLore;
    }

    public String getDelicateNameLore() {
        return delicateNameLore;
    }

    public String getFarmerstepNameLore1() {
        return farmerstepNameLore1;
    }

    public String getFarmerstepNameLore2() {
        return farmerstepNameLore2;
    }

    public String getFarmerstepNameLore3() {
        return farmerstepNameLore3;
    }

    public String getGrandtillingNameLore1() {
        return grandtillingNameLore1;
    }

    public String getGrandtillingNameLore2() {
        return grandtillingNameLore2;
    }

    public String getGrandtillingNameLore3() {
        return grandtillingNameLore3;
    }
}
