package fp.manuton.config;

import com.google.gson.Gson;
import fp.manuton.FarmingPlus;
import fp.manuton.costs.Cost;
import fp.manuton.rewards.*;
import fp.manuton.rewardsCounter.RewardRecord;
import fp.manuton.rewardsCounter.RewardsCounter;
import fp.manuton.utils.MessageUtils;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import java.io.*;
import java.util.*;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;

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
    private String pluginPrefix;
    private String guiConfirm;
    private String guiCancel;
    private List<String> enchantsList;
    private List<String> commandList;
    private String rewardListTitle;
    private String notCommand;
    private String notEnchantment;
    private String notReward;
    private String playerOffline;
    private String entityInvalid;
    private String entityInvalidMythic;
    private String rewardGiveCommand;

    private long saveInterval;
    private List<String> replenishLore;
    private List<String> farmersgraceLore;
    private List<String> delicateLore;
    private List<String> irrigateLore;
    private List<String> farmerstepLore1;
    private List<String> farmerstepLore2;
    private List<String> farmerstepLore3;
    private List<String> grandtillingLore1;
    private List<String> grandtillingLore2;
    private List<String> grandtillingLore3;

    private Map<String, Reward> rewards;
    private Map<String, Cost> costs;
    private Map<UUID, RewardsCounter> rewardsCounter;

    public MainConfigManager(){
        configFile = new CustomConfig("config.yml", null, FarmingPlus.getPlugin());
        configFile.registerConfig();
        messagesFile = new CustomConfig("messages.yml", null, FarmingPlus.getPlugin());
        messagesFile.registerConfig();
        rewardsFile = new CustomConfig("rewards.yml", null, FarmingPlus.getPlugin());
        rewardsFile.registerConfig();
        loadConfig();
        loadRewards();
        loadCosts();
        initializeRewardsCounter();
        setSaveInterval(20L * 60 * getSaveInterval());
    }

    public void initializeRewardsCounter() {
        File file = new File(FarmingPlus.getPlugin().getDataFolder() + File.separator + "Data", "rewardsRecords.json");
        if (!file.exists()) {
            rewardsCounter = new HashMap<>();
        } else {
            loadRecordFromJson();
        }
    }

    public void saveRecordToJson() {
        Gson gson = new Gson();

        // Convertir los UUIDs a strings antes de guardar
        Map<String, RewardsCounter> stringKeyedMap = new HashMap<>();
        for (Map.Entry<UUID, RewardsCounter> entry : rewardsCounter.entrySet()) {
            stringKeyedMap.put(entry.getKey().toString(), entry.getValue());
        }

        String json = gson.toJson(stringKeyedMap);

        File file = new File(FarmingPlus.getPlugin().getDataFolder() + File.separator + "Data" + File.separator + "rewardsRecords.json");
        file.getParentFile().mkdirs();

        try (FileWriter writer = new FileWriter(file)) {
            writer.write(json);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadRecordFromJson() {
        Gson gson = new Gson();
        Type recordType = new TypeToken<Map<String, RewardsCounter>>(){}.getType();

        try (FileReader reader = new FileReader(FarmingPlus.getPlugin().getDataFolder() + File.separator + "Data" + File.separator + "rewardsRecords.json")) {
            Map<String, RewardsCounter> stringKeyedMap = gson.fromJson(reader, recordType);

            rewardsCounter = new HashMap<>();
            for (Map.Entry<String, RewardsCounter> entry : stringKeyedMap.entrySet()) {
                rewardsCounter.put(UUID.fromString(entry.getKey()), entry.getValue());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
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
                Bukkit.getConsoleSender().sendMessage(MessageUtils.getColoredMessage(getPluginPrefix()+"&fLoaded reward: " + key));
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

    public String getKeyFromReward(Reward reward) {
        for (Map.Entry<String, Reward> entry : rewards.entrySet()) {
            if (entry.getValue().equals(reward)){
                return entry.getKey();
            }
        }
        return null;
    }

    public void loadCosts(){
        costs = new HashMap<>();
        ConfigurationSection enchants = configFile.getConfig().getConfigurationSection("config.enchantments");
        for (String key : enchants.getKeys(false)) {
            ConfigurationSection enchant = enchants.getConfigurationSection(key);

            Cost cost = null;
            if (key.equals("grand-tilling") || key.equals("farmers-step")){
                for(int i = 1; i <= 3; i++){
                    double money = enchant.getDouble("cost"+i+".money");
                    List<String> items = enchant.getStringList("cost"+i+".items");
                    int xpLevels = enchant.getInt("cost"+i+".exp");
                    cost = new Cost(xpLevels, money, items);

                    if (cost != null) {
                        Bukkit.getConsoleSender().sendMessage(MessageUtils.getColoredMessage(getPluginPrefix()+"&fLoaded cost: " + key+i));
                        costs.put(key+i, cost);
                    }
                }
            }else{
                double money = enchant.getDouble("cost.money");
                List<String> items = enchant.getStringList("cost.items");
                int xpLevels = enchant.getInt("cost.exp");
                cost = new Cost(xpLevels, money, items);

                if (cost != null) {
                    Bukkit.getConsoleSender().sendMessage(MessageUtils.getColoredMessage(getPluginPrefix()+"&fLoaded cost: " + key));
                    costs.put(key, cost);
                }
            }
        }
    }

    public Cost getCost(String key) {
        return costs.get(key);
    }

    public Collection<Cost> getAllCosts() {
        return costs.values();
    }

    public List<String> getAllCostsNames() {
        return new ArrayList<>(costs.keySet());
    }

    public String getKeyFromCost(Cost cost) {
        for (Map.Entry<String, Cost> entry : costs.entrySet()) {
            if (entry.getValue().equals(cost)){
                return entry.getKey();
            }
        }
        return null;
    }

    public RewardsCounter getRewardsCounter(UUID key) {
        return rewardsCounter.get(key);
    }

    public Collection<RewardsCounter> getAllRewardsCounter() {
        return rewardsCounter.values();
    }

    public List<UUID> getAllRewardsCounterUuids() {
        return new ArrayList<>(rewardsCounter.keySet());
    }

    public UUID getKeyFromRewardCounter(RewardsCounter rewardCounter) {
        for (Map.Entry<UUID, RewardsCounter> entry : rewardsCounter.entrySet()) {
            if (entry.getValue().equals(rewardCounter)){
                return entry.getKey();
            }
        }
        return null;
    }

    public void loadConfig(){
        FileConfiguration config = configFile.getConfig();
        FileConfiguration messages = messagesFile.getConfig();
        enabledMetrics = config.getBoolean("config.enabled-metrics");
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
        farmerstepName = config.getString("config.enchantments.farmers-step.name");
        grandtillingName = config.getString("config.enchantments.grand-tilling.name");
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
        pluginPrefix = config.getString("config.prefix");
        guiConfirm = config.getString("config.gui.confirm");
        guiCancel = config.getString("config.gui.cancel");

        replenishLore = config.getStringList("config.enchantments.replenish.lore");
        farmersgraceLore = config.getStringList("config.enchantments.farmers-grace.lore");
        delicateLore = config.getStringList("config.enchantments.delicate.lore");
        irrigateLore = config.getStringList("config.enchantments.irrigate.lore");
        farmerstepLore1 = config.getStringList("config.enchantments.farmers-step.lore1");
        farmerstepLore2 = config.getStringList("config.enchantments.farmers-step.lore2");
        farmerstepLore3 = config.getStringList("config.enchantments.farmers-step.lore3");
        grandtillingLore1 = config.getStringList("config.enchantments.grand-tilling.lore1");
        grandtillingLore2 = config.getStringList("config.enchantments.grand-tilling.lore2");
        grandtillingLore3 = config.getStringList("config.enchantments.grand-tilling.lore3");

        noPermissionCommand = messages.getString("messages.no-permission-command");
        noPermissionAction = messages.getString("messages.no-permission-action");
        reloadedConfig = messages.getString("messages.reloaded-config");
        enchantsList = messages.getStringList("messages.enchants-list");
        commandList = messages.getStringList("messages.command-list");
        rewardListTitle = messages.getString("messages.reward-list-title");
        notCommand = messages.getString("messages.not-command");
        notEnchantment = messages.getString("messages.not-enchantment");
        notReward = messages.getString("messages.not-reward");
        playerOffline = messages.getString("messages.player-offline");
        entityInvalid = messages.getString("messages.entity-invalid");
        entityInvalidMythic = messages.getString("messages.entity-invalid-mythic");
        rewardGiveCommand = messages.getString("messages.reward-give-command");

        saveInterval = config.getLong("config.save-interval");
        if (saveInterval <= 0) {
            saveInterval = 3;
        }
        
    }

    // If there isn't a file, it will create it and load the default values
    public void reloadConfig(){
        saveRecordToJson();
        configFile.registerConfig();
        messagesFile.registerConfig();
        rewardsFile.registerConfig();
        configFile.reloadConfig();
        loadConfig();
        rewardsFile.reloadConfig();
        loadRewards();
        loadCosts();
        initializeRewardsCounter();
        setSaveInterval(20L * 60 * getSaveInterval());
    }

    public void startSaveTask() {
        Bukkit.getScheduler().runTaskTimerAsynchronously(FarmingPlus.getPlugin(), this::saveRecordToJson, 0L, getSaveInterval());
    }

    public void setSaveInterval(long interval) {
        this.saveInterval = interval;
        Bukkit.getScheduler().cancelTasks(FarmingPlus.getPlugin());
        startSaveTask();
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

    public String getPluginPrefix() {
        return pluginPrefix;
    }

    public List<String> getReplenishLore() {
        return replenishLore;
    }

    public List<String> getFarmersgraceLore() {
        return farmersgraceLore;
    }

    public List<String> getDelicateLore() {
        return delicateLore;
    }

    public List<String> getIrrigateLore() {
        return irrigateLore;
    }

    public List<String> getFarmerstepLore1() {
        return farmerstepLore1;
    }

    public List<String> getFarmerstepLore2() {
        return farmerstepLore2;
    }

    public List<String> getFarmerstepLore3() {
        return farmerstepLore3;
    }

    public List<String> getGrandtillingLore1() {
        return grandtillingLore1;
    }

    public List<String> getGrandtillingLore2() {
        return grandtillingLore2;
    }

    public List<String> getGrandtillingLore3() {
        return grandtillingLore3;
    }

    public String getGuiConfirm() {
        return guiConfirm;
    }

    public String getGuiCancel() {
        return guiCancel;
    }

    public List<String> getEnchantsList() {
        return enchantsList;
    }

    public List<String> getCommandList() {
        return commandList;
    }

    public String getRewardListTitle() {
        return rewardListTitle;
    }

    public String getNotCommand() {
        return notCommand;
    }

    public String getNotEnchantment() {
        return notEnchantment;
    }

    public String getNotReward() {
        return notReward;
    }

    public String getPlayerOffline() {
        return playerOffline;
    }

    public String getEntityInvalid() {
        return entityInvalid;
    }

    public String getEntityInvalidMythic() {
        return entityInvalidMythic;
    }

    public String getRewardGiveCommand() {
        return rewardGiveCommand;
    }

    public long getSaveInterval() {
        return saveInterval;
    }

    public Map<UUID, RewardsCounter> getRewardsCounterMap() {
        return rewardsCounter;
    }
}
