package fp.manuton.config;

import fp.manuton.FarmingPlus;
import fp.manuton.rewards.*;
import fp.manuton.utils.MessageUtils;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainConfigManager {

    private final CustomConfig configFile;
    private final CustomConfig messagesFile;
    private final CustomConfig rewardsFile;
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
    private String guiTitle;
    private String replenishName;
    private String farmersgraceName;
    private String delicateName;
    private String farmerstepName;
    private String grandtillingName;
    private String guiEmptySlot;
    private String guiSoundOpen;
    private String guiSoundClose;
    private String replenishSoundBreak;
    private float volumeGuiSoundOpen;
    private float volumeGuiSoundClose;
    private float volumeReplenishSoundBreak;
    private String farmerstepGuiTitle;
    private String farmerstepGuiEmptySlot;
    private String farmerstepGuiSoundOnSet;
    private float volumeFarmerstepGuiSoundOnSet;
    private int grandTilling3Blocks;
    private String irrigateName;
    private String irrigateNameLore;
    private int irrigateMaxBlocks;

    private Map<String, Reward> rewards;

    public MainConfigManager(){
        configFile = new CustomConfig("config.yml", null, FarmingPlus.getPlugin());
        configFile.registerConfig();
        messagesFile = new CustomConfig("messages.yml", null, FarmingPlus.getPlugin());
        messagesFile.registerConfig();
        rewardsFile = new CustomConfig("rewards.yml", null, FarmingPlus.getPlugin());
        rewardsFile.registerConfig();
        loadRewards();
        loadConfig();
    }

    private void loadRewards() {
        rewards = new HashMap<>();
    
        ConfigurationSection rewardsSection = rewardsFile.getConfig().getConfigurationSection("Rewards");
    
        for (String key : rewardsSection.getKeys(false)) {
            ConfigurationSection rewardSection = rewardsSection.getConfigurationSection(key);
    
            List<String> crops = rewardSection.getStringList("crops");
            String type = rewardSection.getString("type");
            double chance = rewardSection.getDouble("chance");
    
            Reward reward = null;
            if (type.equals("Command")) {
                List<String> commands = rewardSection.getStringList("commands");
                List<String> messages = rewardSection.getStringList("messages");
                String sound = rewardSection.getString("sound");
    
                reward = new CommandReward(crops, chance, commands, messages, sound);
            } else if (type.equals("Money")) {
                double amount = rewardSection.getDouble("amount");
                List<String> messages = rewardSection.getStringList("messages");
                String sound = rewardSection.getString("sound");

                reward = new MoneyReward(crops, chance, amount, messages, sound);
            } else if (type.equals("Item")) {
                List<String> items = rewardSection.getStringList("items");
                List<String> messages = rewardSection.getStringList("messages");
                String sound = rewardSection.getString("sound");

                reward = new ItemReward(crops, chance, items, messages, sound);
            }else if (type.equals("Summon")) {
                String mob = rewardSection.getString("mob");
                List<String> messages = rewardSection.getStringList("messages");
                String sound = rewardSection.getString("sound");
                int amount = rewardSection.getInt("amount");
                int level = rewardSection.getInt("level");

                reward = new SummonReward(crops, chance, messages, mob, sound, amount, level);
            } else {
                continue;
            }
    
            if (reward != null) {
                Bukkit.getConsoleSender().sendMessage(MessageUtils.getColoredMessage(FarmingPlus.prefix+"&fLoaded reward: " + key));
                rewards.put(key, reward);
            }
        }
    }

    public Reward getReward(String key) {
        return rewards.get(key);
    }

    public Collection<Reward> getAllRewards() {
        return rewards.values();
    }

    public List<String> getAllRewardNames() {
        return new ArrayList<>(rewards.keySet());
    }

    public void loadConfig(){
        FileConfiguration config = configFile.getConfig();
        FileConfiguration messages = messagesFile.getConfig();
        enabledMetrics = config.getBoolean("config.enabled_metrics");
        replenishNameLore = config.getString("config.enchantments.replenish.lore-name");
        farmersgraceNameLore = config.getString("config.enchantments.farmers-grace.lore-name");
        delicateNameLore = config.getString("config.enchantments.delicate.lore-name");
        farmerstepNameLore1 = config.getString("config.enchantments.farmers-step.lore-name1");
        farmerstepNameLore2 = config.getString("config.enchantments.farmers-step.lore-name2");
        farmerstepNameLore3 = config.getString("config.enchantments.farmers-step.lore-name3");
        grandtillingNameLore1 = config.getString("config.enchantments.grand-tilling.lore-name1");
        grandtillingNameLore2 = config.getString("config.enchantments.grand-tilling.lore-name2");
        grandtillingNameLore3 = config.getString("config.enchantments.grand-tilling.lore-name3");
        guiTitle = config.getString("config.gui.title");
        replenishName = config.getString("config.enchantments.replenish.name");
        farmersgraceName = config.getString("config.enchantments.farmers-grace.name");
        delicateName = config.getString("config.enchantments.delicate.name");
        farmerstepName = config.getString("config.enchantments.farmers-step.name1");
        grandtillingName = config.getString("config.enchantments.grand-tilling.name1");
        guiEmptySlot = config.getString("config.gui.empty-slot");
        guiSoundOpen = config.getString("config.gui.sound-on-open");
        guiSoundClose = config.getString("config.gui.sound-on-close");
        replenishSoundBreak = config.getString("config.enchantments.replenish.sound-on-break");
        volumeGuiSoundOpen = (float) config.getDouble("config.gui.volume-on-open");
        volumeGuiSoundClose = (float) config.getDouble("config.gui.volume-on-close");
        volumeReplenishSoundBreak = (float) config.getDouble("config.enchantments.replenish.volume-on-break");
        farmerstepGuiTitle = config.getString("config.enchantments.farmers-step.gui.title");
        farmerstepGuiEmptySlot = config.getString("config.enchantments.farmers-step.gui.empty-slot");
        farmerstepGuiSoundOnSet = config.getString("config.enchantments.farmers-step.gui.sound-on-set");
        volumeFarmerstepGuiSoundOnSet = (float) config.getDouble("config.enchantments.farmers-step.gui.volume-on-set");
        grandTilling3Blocks = config.getInt("config.enchantments.grand-tilling.level3-max-blocks");
        irrigateName = config.getString("config.enchantments.irrigate.name");
        irrigateNameLore = config.getString("config.enchantments.irrigate.lore-name");
        irrigateMaxBlocks = config.getInt("config.enchantments.irrigate.max-blocks");

        noPermissionCommand = messages.getString("messages.no-permission-command");
        noPermissionAction = messages.getString("messages.no-permission-action");
        reloadedConfig = messages.getString("messages.reloaded-config");
        
    }

    public void reloadConfig(){
        configFile.reloadConfig();
        loadConfig();
        rewardsFile.reloadConfig();
        loadRewards();
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

    public String getGuiTitle() {
        return guiTitle;
    }

    public String getReplenishName() {
        return replenishName;
    }

    public String getFarmersgraceName() {
        return farmersgraceName;
    }

    public String getDelicateName() {
        return delicateName;
    }

    public String getFarmerstepName() {
        return farmerstepName;
    }

    public String getGrandtillingName() {
        return grandtillingName;
    }
    public String getGuiEmptySlot() {
        return guiEmptySlot;
    }
    public String getGuiSoundOpen() {
        return guiSoundOpen;
    }
    public String getGuiSoundClose() {
        return guiSoundClose;
    }
    public String getReplenishSoundBreak() {
        return replenishSoundBreak;
    }

    public float getVolumeGuiSoundOpen() {
        return volumeGuiSoundOpen;
    }

    public float getVolumeGuiSoundClose() {
        return volumeGuiSoundClose;
    }

    public float getVolumeReplenishSoundBreak() {
        return volumeReplenishSoundBreak;
    }

    public String getFarmerstepGuiTitle() {
        return farmerstepGuiTitle;
    }

    public String getFarmerstepGuiEmptySlot() {
        return farmerstepGuiEmptySlot;
    }

    public String getFarmerstepGuiSoundOnSet() {
        return farmerstepGuiSoundOnSet;
    }

    public float getVolumeFarmerstepGuiSoundOnSet() {
        return volumeFarmerstepGuiSoundOnSet;
    }


    public int getGrandTilling3Blocks() {
        return grandTilling3Blocks;
    }

    public String getIrrigateName() {
        return irrigateName;
    }

    public String getIrrigateNameLore() {
        return irrigateNameLore;
    }

    public int getIrrigateMaxBlocks() {
        return irrigateMaxBlocks;
    }
}
