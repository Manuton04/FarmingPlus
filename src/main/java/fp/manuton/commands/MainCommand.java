package fp.manuton.commands;

import fp.manuton.FarmingPlus;
import fp.manuton.SQL.MySQLData;
import fp.manuton.enchantments.CustomEnchantments;
import fp.manuton.enchantments.EnchantFp;
import fp.manuton.guis.EnchantGui;
import fp.manuton.rewards.MoneyReward;
import fp.manuton.rewards.Reward;
import fp.manuton.rewards.SummonReward;
import fp.manuton.rewardsCounter.RewardRecord;
import fp.manuton.rewardsCounter.RewardsCounter;
import fp.manuton.utils.ItemUtils;
import fp.manuton.utils.MessageUtils;
import fp.manuton.utils.PlayerUtils;
import io.lumine.mythic.bukkit.MythicBukkit;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.awt.*;
import java.sql.Connection;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

import static com.sk89q.worldedit.command.util.PrintCommandHelp.help;

public class MainCommand implements CommandExecutor, TabExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String alias, String[] args) {
        if (!(sender instanceof Player)){
            // CONSOLE //
            if (args.length >= 1){
                if (args[0].equalsIgnoreCase("reload")) {
                    subCommandReload(sender);
                }else if (args[0].equalsIgnoreCase("enchant") || args[0].equalsIgnoreCase("en")){ // /fp enchants /fp en //
                    if (args.length >= 1) {
                        if (args.length == 1) {
                            for (String line : FarmingPlus.getPlugin().getMainConfigManager().getEnchantsList()) {
                                Bukkit.getConsoleSender().sendMessage(MessageUtils.translateAll(null, line));
                            }
                            return true;
                        }
                        if (args.length >= 2) {
                            if (PlayerUtils.doesPlayerExist(args[1])) {
                                OfflinePlayer target = Bukkit.getPlayer(args[1]);

                                if (target == null || !target.isOnline()) {
                                    Bukkit.getConsoleSender().sendMessage(MessageUtils.translateAll(null, FarmingPlus.getPlugin().getMainConfigManager().getPlayerOffline()));
                                    return true;

                                } else {
                                    if (args.length >= 3) {
                                        if (args.length == 3) {
                                            Bukkit.getConsoleSender().sendMessage(MessageUtils.translateAll(null, enchantCommandUsage()));
                                            return true;
                                        }
                                        List<String> enchantList = new ArrayList<>();
                                        enchantList.addAll(Arrays.asList("farmersgrace", "replenish", "delicate", "irrigate", "grandtilling", "farmerstep"));
                                        if (!enchantList.contains(args[2].toLowerCase())) {
                                            for (String line : FarmingPlus.getPlugin().getMainConfigManager().getEnchantsList()) {
                                                Bukkit.getConsoleSender().sendMessage(MessageUtils.translateAll(null, line));
                                            }
                                            return true;
                                        } else {
                                            if (args.length >= 4) {
                                                int level = 1;
                                                try {
                                                    level = Integer.parseInt(args[3]);
                                                } catch (NumberFormatException exp) {
                                                    Bukkit.getConsoleSender().sendMessage(MessageUtils.translateAll(null, enchantCommandUsage()));
                                                    return true;
                                                }

                                                EnchantFp enchant = null;
                                                switch (args[2].toLowerCase()) {
                                                    case "farmersgrace":
                                                        enchant = CustomEnchantments.FARMERSGRACE;
                                                        if (level != 1)
                                                            level = 1;
                                                        break;
                                                    case "replenish":
                                                        enchant = CustomEnchantments.REPLENISH;
                                                        if (level != 1)
                                                            level = 1;
                                                        break;
                                                    case "delicate":
                                                        enchant = CustomEnchantments.DELICATE;
                                                        if (level != 1)
                                                            level = 1;
                                                        break;
                                                    case "irrigate":
                                                        enchant = CustomEnchantments.IRRIGATE;
                                                        if (level != 1)
                                                            level = 1;
                                                        break;
                                                    case "grandtilling":
                                                        enchant = CustomEnchantments.GRANDTILLING;
                                                        if (level < 1)
                                                            level = 1;
                                                        if (level > 3)
                                                            level = 3;
                                                        break;
                                                    case "farmerstep":
                                                        enchant = CustomEnchantments.FARMERSTEP;
                                                        if (level < 1)
                                                            level = 1;
                                                        if (level > 3)
                                                            level = 3;
                                                        break;
                                                    default:
                                                        break;
                                                }

                                                if (enchant != null) {
                                                    if (args.length == 5) {
                                                        ItemStack item = null;
                                                        try {
                                                            item = new ItemStack(Material.valueOf(args[4].toUpperCase()));
                                                        } catch (IllegalArgumentException exp) {
                                                            Bukkit.getConsoleSender().sendMessage(MessageUtils.translateAll(null, enchantCommandUsage()));
                                                            return true;
                                                        }
                                                        if (item != null) {
                                                            if (enchant.canEnchantItem(item)) {
                                                                item = ItemUtils.enchantedItem(item, enchant, level);
                                                                Player targetOnline = (Player) target;
                                                                int emptySlot = targetOnline.getInventory().firstEmpty();
                                                                if (emptySlot != -1)
                                                                    targetOnline.getInventory().setItem(emptySlot, item);
                                                                else
                                                                    targetOnline.getWorld().dropItem(targetOnline.getLocation(), item);

                                                                Bukkit.getConsoleSender().sendMessage(MessageUtils.translateAll(targetOnline, FarmingPlus.getPlugin().getMainConfigManager().getEnchantedsuccesfully()));
                                                            } else {
                                                                Bukkit.getConsoleSender().sendMessage(MessageUtils.translateAll(null, FarmingPlus.getPlugin().getMainConfigManager().getEnchantNotAllowed()));
                                                            }
                                                            return true;
                                                        }
                                                    }
                                                }
                                                Bukkit.getConsoleSender().sendMessage(MessageUtils.translateAll(null, enchantCommandUsage()));
                                                return true;
                                            }
                                        }
                                    }
                                }
                                if (args.length == 2) {
                                    Bukkit.getConsoleSender().sendMessage(MessageUtils.translateAll(null, enchantCommandUsage()));
                                    return true;
                                }
                            } else {
                                Bukkit.getConsoleSender().sendMessage(MessageUtils.translateAll(null, FarmingPlus.getPlugin().getMainConfigManager().getNotplayedbefore()));
                                if (args.length == 2) {
                                    Bukkit.getConsoleSender().sendMessage(MessageUtils.translateAll(null, enchantCommandUsage()));
                                }
                                return true;
                            }
                        }
                    }
                        }else if (args[0].equalsIgnoreCase("reward")){
                    if (args.length == 1){
                        sender.sendMessage(MessageUtils.translateAll(null, "%farmingplus_prefix%&eAvailable subcommands: give, list, clear"));
                        return true;
                    }
                    if (args[1].equalsIgnoreCase("give")) {

                        if (args.length < 4) {
                            sender.sendMessage(MessageUtils.translateAll(null, "%farmingplus_prefix%&eUsage: /fp reward give <player> <reward>"));
                            return true;
                        }

                        String target = args[2];
                        boolean isOnline = false;
                        for (String players : Bukkit.getOnlinePlayers().stream().map(Player::getName).toList()) {
                            if (players.equalsIgnoreCase(target)) {
                                isOnline = true;
                                break;
                            }
                        }
                        if (!isOnline) {
                            sender.sendMessage(MessageUtils.translateAll(null, FarmingPlus.getPlugin().getMainConfigManager().getPlayerOffline()));
                            return true;
                        } else {
                            if (FarmingPlus.getPlugin().getMainConfigManager().getAllRewardNames().contains(args[3])) {
                                Reward reward = FarmingPlus.getPlugin().getMainConfigManager().getReward(args[3]);
                                reward.give(Bukkit.getPlayer(target));
                                boolean done = true;
                                if (reward instanceof SummonReward) {
                                    if (Bukkit.getPluginManager().getPlugin("MythicMobs") == null) {
                                        try {
                                            EntityType.valueOf(((SummonReward) reward).getEntity());
                                        } catch (IllegalArgumentException exp) {
                                            done = false;
                                            sender.sendMessage(MessageUtils.translateAll(null, FarmingPlus.getPlugin().getMainConfigManager().getEntityInvalidMythic()));
                                        }
                                    } else {
                                        boolean isMythic = MythicBukkit.inst().getMobManager().getMythicMob(((SummonReward) reward).getEntity()).orElse(null) != null;
                                        if (!isMythic) {
                                            try {
                                                EntityType.valueOf(((SummonReward) reward).getEntity());
                                            } catch (IllegalArgumentException exp) {
                                                done = false;
                                                sender.sendMessage(MessageUtils.translateAll(null, FarmingPlus.getPlugin().getMainConfigManager().getEntityInvalid()));
                                            }
                                        }

                                    }

                                }
                                if (done) {

                                    UUID playerId = Bukkit.getPlayer(target).getUniqueId();
                                    if (MySQLData.isDatabaseConnected(FarmingPlus.getConnectionMySQL())) {
                                        RewardRecord rewardRecord = new RewardRecord(new Date(), args[3]);
                                        MySQLData.saveRewardCounterToDatabase(FarmingPlus.getConnectionMySQL(), playerId, rewardRecord);
                                    }
                                    Map<UUID, RewardsCounter> rewardsCounterMap = FarmingPlus.getPlugin().getMainConfigManager().getRewardsCounterMap();
                                    RewardsCounter rewardsCounter = new RewardsCounter(new ArrayList<>());
                                    if (!rewardsCounterMap.containsKey(playerId))
                                        rewardsCounterMap.put(playerId, rewardsCounter);
                                    rewardsCounter = rewardsCounterMap.get(playerId);
                                    rewardsCounter.addRecord(FarmingPlus.getPlugin().getMainConfigManager().getKeyFromReward(reward));

                                    String message = FarmingPlus.getPlugin().getMainConfigManager().getRewardGiveCommand();
                                    message = message.replace("%reward%", args[3]);
                                    sender.sendMessage(MessageUtils.translateAll(Bukkit.getPlayer(target), message));
                                }
                            } else
                                sender.sendMessage(MessageUtils.translateAll(null, FarmingPlus.getPlugin().getMainConfigManager().getNotReward()));

                        }


                    } else if (args[1].equalsIgnoreCase("list")) {
                        List<String> rewardList = new ArrayList<>();
                        for (String reward : FarmingPlus.getPlugin().getMainConfigManager().getAllRewardNames()) {
                            rewardList.add(reward);
                        }
                        Collections.sort(rewardList);
                        sender.sendMessage(MessageUtils.translateAll(null, FarmingPlus.getPlugin().getMainConfigManager().getRewardListTitle()));
                        for (String reward : rewardList) {
                            sender.sendMessage(MessageUtils.getColoredMessage("&e- " + reward));
                        }
                        sender.sendMessage(MessageUtils.getColoredMessage("&f&l---------------------------------"));
                    }else if (args[1].equalsIgnoreCase("clear")) {
                        if (args.length == 3) {
                            OfflinePlayer targetPlayer = Bukkit.getOfflinePlayer(args[2]);
                            boolean done = false;
                            if (MySQLData.isDatabaseConnected(FarmingPlus.getConnectionMySQL()) && MySQLData.existsUUID(FarmingPlus.getConnectionMySQL(), targetPlayer.getUniqueId().toString())) {
                                MySQLData.deleteRewardsForUUID(FarmingPlus.getConnectionMySQL(), targetPlayer.getUniqueId());
                                done = true;
                            }
                            if (!targetPlayer.hasPlayedBefore() && !FarmingPlus.getPlugin().getMainConfigManager().getRewardsCounterMap().containsKey(targetPlayer.getUniqueId())) {
                                sender.sendMessage(MessageUtils.translateAll(null, FarmingPlus.getPlugin().getMainConfigManager().getNotPlayer()));
                            } else {
                                List<Map.Entry<UUID, RewardsCounter>> list = new ArrayList<>(FarmingPlus.getPlugin().getMainConfigManager().getRewardsCounterMap().entrySet());
                                for (Map.Entry<UUID, RewardsCounter> entry : list) {
                                    if (entry.getKey().equals(targetPlayer.getUniqueId())) {
                                        FarmingPlus.getPlugin().getMainConfigManager().getRewardsCounterMap().remove(entry.getKey());
                                        done = true;
                                        break;
                                    }
                                }
                            }
                            if (done)
                                sender.sendMessage(MessageUtils.translateAll(targetPlayer, FarmingPlus.getPlugin().getMainConfigManager().getRewardsCleared()));
                            else
                                sender.sendMessage(MessageUtils.translateAll(null, "%farmingplus_prefix%&eUsage: /fp reward clear <player>"));
                        } else {
                            sender.sendMessage(MessageUtils.translateAll(null, "%farmingplus_prefix%&eUsage: /fp reward clear <player>"));
                        }

                        return true;
                    }
                }
                return true;
            }
            sender.sendMessage(MessageUtils.getColoredMessage(FarmingPlus.prefix+"&fThis command can only be used by a player!"));
            return true;
        }

        // PLAYER //
        Player player = (Player) sender;

        if (args.length == 0){ // /fp //
            if (player.hasPermission("fp.gui.use")) {
                EnchantGui.createGui(player, null);
            }else {
                player.sendMessage(MessageUtils.translateAll(player, FarmingPlus.getPlugin().getMainConfigManager().getNoPermissionCommand()));
                help(player);
            }
            return true;
        }

        if (args.length >= 1){
            if (args[0].equalsIgnoreCase("enchant") || args[0].equalsIgnoreCase("en")){ // /fp enchants /fp en //
                if (args.length >= 1) {
                    if (!player.hasPermission("fp.commands.enchant")) {
                        player.sendMessage(MessageUtils.translateAll(player, FarmingPlus.getPlugin().getMainConfigManager().getNoPermissionCommand()));
                        return true;
                    }
                    if (args.length == 1){
                        for (String line : FarmingPlus.getPlugin().getMainConfigManager().getEnchantsList()) {
                            player.sendMessage(MessageUtils.translateAll(player, line));
                        }
                        return true;
                    }
                    if (args.length >=2){
                        if (PlayerUtils.doesPlayerExist(args[1])){
                            OfflinePlayer target = Bukkit.getPlayer(args[1]);

                            if (target == null || !target.isOnline()){
                                player.sendMessage(MessageUtils.translateAll(player, FarmingPlus.getPlugin().getMainConfigManager().getPlayerOffline()));
                                return true;

                            }else{
                                if (args.length >=3){
                                    if (args.length == 3){
                                        player.sendMessage(MessageUtils.translateAll(player, enchantCommandUsage()));
                                        return true;
                                    }
                                    List<String> enchantList = new ArrayList<>();
                                    enchantList.addAll(Arrays.asList("farmersgrace", "replenish", "delicate", "irrigate", "grandtilling", "farmerstep"));
                                    if (!enchantList.contains(args[2].toLowerCase())){
                                        for (String line : FarmingPlus.getPlugin().getMainConfigManager().getEnchantsList()) {
                                            player.sendMessage(MessageUtils.translateAll(player, line));
                                        }
                                        return true;
                                    }else{
                                        if (args.length >=4){
                                            int level = 1;
                                            try {
                                                level = Integer.parseInt(args[3]);
                                            }catch (NumberFormatException exp){
                                                player.sendMessage(MessageUtils.translateAll(player, enchantCommandUsage()));
                                                return true;
                                            }

                                            EnchantFp enchant = null;
                                            switch (args[2].toLowerCase()){
                                                case "farmersgrace":
                                                    enchant = CustomEnchantments.FARMERSGRACE;
                                                    if (level != 1)
                                                        level = 1;
                                                    break;
                                                case "replenish":
                                                    enchant = CustomEnchantments.REPLENISH;
                                                    if (level != 1)
                                                        level = 1;
                                                    break;
                                                case "delicate":
                                                    enchant = CustomEnchantments.DELICATE;
                                                    if (level != 1)
                                                        level = 1;
                                                    break;
                                                case "irrigate":
                                                    enchant = CustomEnchantments.IRRIGATE;
                                                    if (level != 1)
                                                        level = 1;
                                                    break;
                                                case "grandtilling":
                                                    enchant = CustomEnchantments.GRANDTILLING;
                                                    if (level < 1)
                                                        level = 1;
                                                    if (level > 3)
                                                        level = 3;
                                                    break;
                                                case "farmerstep":
                                                    enchant = CustomEnchantments.FARMERSTEP;
                                                    if (level < 1)
                                                        level = 1;
                                                    if (level > 3)
                                                        level = 3;
                                                    break;
                                                default:
                                                    break;
                                            }

                                            if (enchant !=null){
                                                if (args.length == 5){
                                                    ItemStack item = null;
                                                    try{
                                                        item = new ItemStack(Material.valueOf(args[4].toUpperCase()));
                                                    }catch (IllegalArgumentException exp){
                                                        player.sendMessage(MessageUtils.translateAll(player, enchantCommandUsage()));
                                                        return true;
                                                    }
                                                    if (item != null){
                                                        if (enchant.canEnchantItem(item)) {
                                                            item = ItemUtils.enchantedItem(item, enchant, level);
                                                            Player targetOnline = (Player) target;
                                                            int emptySlot = targetOnline.getInventory().firstEmpty();
                                                            if (emptySlot != -1)
                                                                targetOnline.getInventory().setItem(emptySlot, item);
                                                            else
                                                                targetOnline.getWorld().dropItem(targetOnline.getLocation(), item);

                                                            player.sendMessage(MessageUtils.translateAll(target, FarmingPlus.getPlugin().getMainConfigManager().getEnchantedsuccesfully()));
                                                        }else{
                                                            player.sendMessage(MessageUtils.translateAll(player, FarmingPlus.getPlugin().getMainConfigManager().getEnchantNotAllowed()));
                                                        }
                                                        return true;
                                                    }
                                                }
                                            }
                                            player.sendMessage(MessageUtils.translateAll(player, enchantCommandUsage()));
                                            return true;
                                        }
                                    }
                                }
                            }
                            if (args.length == 2){
                                player.sendMessage(MessageUtils.translateAll(player, enchantCommandUsage()));
                                return true;
                            }
                        }else{
                            player.sendMessage(MessageUtils.translateAll(player, FarmingPlus.getPlugin().getMainConfigManager().getNotplayedbefore()));
                            if (args.length == 2){
                                player.sendMessage(MessageUtils.translateAll(player, enchantCommandUsage()));
                            }
                            return true;
                        }
                    }
                }

            }else if (args[0].equalsIgnoreCase("reload")){
                subCommandReload(player);
            }else if (args[0].equalsIgnoreCase("help")){
                if (!player.hasPermission("fp.commands.help") && !player.hasPermission("fp.admin")){
                    player.sendMessage(MessageUtils.translateAll(player, FarmingPlus.getPlugin().getMainConfigManager().getNoPermissionCommand()));
                    return true;
                }
                help(player);
            }else if (args[0].equalsIgnoreCase("reward")){
                if (args.length == 1){
                    player.sendMessage(MessageUtils.translateAll(player, FarmingPlus.getPlugin().getMainConfigManager().getUseHelp()));
                    return true;
                }
                if (args[1].equalsIgnoreCase("give")) {
                    if (!player.hasPermission("fp.commands.reward.give") && !player.hasPermission("fp.admin")) {
                        player.sendMessage(MessageUtils.translateAll(player, FarmingPlus.getPlugin().getMainConfigManager().getNoPermissionCommand()));
                        return true;
                    }

                    if (args.length < 4) {
                        player.sendMessage(MessageUtils.translateAll(player, "%farmingplus_prefix%&eUsage: /fp reward give <player> <reward>"));
                        return true;
                    }

                    String target = args[2];
                    boolean isOnline = false;
                    for (String players : Bukkit.getOnlinePlayers().stream().map(Player::getName).toList()) {
                        if (players.equalsIgnoreCase(target)) {
                            isOnline = true;
                            break;
                        }
                    }
                    if (!isOnline) {
                        player.sendMessage(MessageUtils.translateAll(player, FarmingPlus.getPlugin().getMainConfigManager().getPlayerOffline()));
                        return true;
                    } else {
                        if (FarmingPlus.getPlugin().getMainConfigManager().getAllRewardNames().contains(args[3])) {
                            Reward reward = FarmingPlus.getPlugin().getMainConfigManager().getReward(args[3]);
                            boolean done = true;
                            if (reward instanceof SummonReward) {
                                if (Bukkit.getPluginManager().getPlugin("MythicMobs") == null) {
                                    try {
                                        EntityType.valueOf(((SummonReward) reward).getEntity());
                                    } catch (IllegalArgumentException exp) {
                                        done = false;
                                        player.sendMessage(MessageUtils.translateAll(player, FarmingPlus.getPlugin().getMainConfigManager().getEntityInvalidMythic()));
                                    }
                                } else {
                                    boolean isMythic = MythicBukkit.inst().getMobManager().getMythicMob(((SummonReward) reward).getEntity()).orElse(null) != null;
                                    if (!isMythic) {
                                        try {
                                            EntityType.valueOf(((SummonReward) reward).getEntity());
                                        } catch (IllegalArgumentException exp) {
                                            done = false;
                                            player.sendMessage(MessageUtils.translateAll(player, FarmingPlus.getPlugin().getMainConfigManager().getEntityInvalid()));
                                        }
                                    }

                                }

                            }else if (reward instanceof MoneyReward){
                                if (Bukkit.getPluginManager().getPlugin("Vault") == null) {
                                    player.sendMessage(MessageUtils.translateAll(null, FarmingPlus.prefix+" &cYou need Vault to use this reward."));
                                    done = false;
                                }
                            }
                            if (done){
                                reward.give(Bukkit.getPlayer(target));

                                UUID playerId = Bukkit.getPlayer(target).getUniqueId();
                                if (MySQLData.isDatabaseConnected(FarmingPlus.getConnectionMySQL())) {
                                    RewardRecord rewardRecord = new RewardRecord(new Date(), args[3]);
                                    MySQLData.saveRewardCounterToDatabase(FarmingPlus.getConnectionMySQL(), playerId, rewardRecord);
                                }
                                Map<UUID, RewardsCounter> rewardsCounterMap = FarmingPlus.getPlugin().getMainConfigManager().getRewardsCounterMap();
                                RewardsCounter rewardsCounter = new RewardsCounter(new ArrayList<>());
                                if (!rewardsCounterMap.containsKey(playerId))
                                    rewardsCounterMap.put(playerId, rewardsCounter);
                                rewardsCounter = rewardsCounterMap.get(playerId);
                                rewardsCounter.addRecord(FarmingPlus.getPlugin().getMainConfigManager().getKeyFromReward(reward));

                                String message = FarmingPlus.getPlugin().getMainConfigManager().getRewardGiveCommand();
                                message = message.replace("%reward%", args[3]);
                                player.sendMessage(MessageUtils.translateAll(Bukkit.getPlayer(target), message));
                            }
                        } else
                            player.sendMessage(MessageUtils.translateAll(player, FarmingPlus.getPlugin().getMainConfigManager().getNotReward()));

                    }


                } else if (args[1].equalsIgnoreCase("list")) {
                    if (!player.hasPermission("fp.commands.reward.list") && !player.hasPermission("fp.admin")) {
                        player.sendMessage(MessageUtils.translateAll(player, FarmingPlus.getPlugin().getMainConfigManager().getNoPermissionCommand()));
                        return true;
                    }
                    List<String> rewardList = new ArrayList<>();
                    for (String reward : FarmingPlus.getPlugin().getMainConfigManager().getAllRewardNames()) {
                        rewardList.add(reward);
                    }
                    Collections.sort(rewardList);
                    player.sendMessage(MessageUtils.translateAll(player, FarmingPlus.getPlugin().getMainConfigManager().getRewardListTitle()));
                    for (String reward : rewardList) {
                        player.sendMessage(MessageUtils.getColoredMessage("&e- " + reward));
                    }
                    player.sendMessage(MessageUtils.getColoredMessage("&f&l---------------------------------"));
                } else if (args[1].equalsIgnoreCase("top")) {
                    if (!player.hasPermission("fp.commands.reward.top") && !player.hasPermission("fp.admin")) {
                        player.sendMessage(MessageUtils.translateAll(player, FarmingPlus.getPlugin().getMainConfigManager().getNoPermissionCommand()));
                        return true;
                    }
                    if (args.length == 2) {
                        int page = 1;

                        List<String> topPlayers = FarmingPlus.getPlugin().getMainConfigManager().getTopRewardsLeaderboardList();
                        int totalPage = (int) Math.ceil(topPlayers.size() / 10.0);

                        int startIndex = (page - 1) * 10;
                        int endIndex = Math.min(startIndex + 10, topPlayers.size());

                        player.sendMessage(MessageUtils.translateAll(player, "&ePage " + page + " of " + totalPage));
                        player.sendMessage(MessageUtils.translateAll(player, FarmingPlus.getPlugin().getMainConfigManager().getTopRewardsLeaderboard()));
                        for (int i = startIndex; i < endIndex; i++) {
                            player.sendMessage(MessageUtils.translateAll(Bukkit.getOfflinePlayer(topPlayers.get(i)), FarmingPlus.getPlugin().getMainConfigManager().getTopRewardsLeaderboardLine()));
                        }
                        player.sendMessage(MessageUtils.getColoredMessage("&f&l---------------------------------"));

                        TextComponent previousPage = new TextComponent(MessageUtils.translateAll(player, FarmingPlus.getPlugin().getMainConfigManager().getTopPrevious()));
                        previousPage.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/fp reward top " + (page - 1)));
                        TextComponent nextPage = new TextComponent(MessageUtils.translateAll(player, FarmingPlus.getPlugin().getMainConfigManager().getTopNext()));
                        nextPage.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/fp reward top " + (page + 1)));

                        player.spigot().sendMessage(previousPage, new TextComponent(" | "), nextPage);
                    } else if (args.length > 2) {
                        boolean isPage;
                        try {
                            Integer.parseInt(args[2]);
                            isPage = true;
                        } catch (NumberFormatException e) {
                            isPage = false;
                        }

                        if (isPage) {
                            int page = Integer.parseInt(args[2]);
                            if (page <= 0) {
                                page = 1;
                            }
                            List<String> topPlayers = FarmingPlus.getPlugin().getMainConfigManager().getTopRewardsLeaderboardList();
                            int totalPage = (int) Math.ceil(topPlayers.size() / 10.0);

                            if (page > totalPage) {
                                page = totalPage;
                            }

                            int startIndex = (page - 1) * 10;
                            int endIndex = Math.min(startIndex + 10, topPlayers.size());

                            player.sendMessage(MessageUtils.translateAll(player, "&ePage " + page + " of " + totalPage));
                            player.sendMessage(MessageUtils.translateAll(player, FarmingPlus.getPlugin().getMainConfigManager().getTopRewardsLeaderboard()));
                            for (int i = startIndex; i < endIndex; i++) {
                                player.sendMessage(MessageUtils.translateAll(Bukkit.getOfflinePlayer(topPlayers.get(i)), FarmingPlus.getPlugin().getMainConfigManager().getTopRewardsLeaderboardLine()));
                            }
                            player.sendMessage(MessageUtils.getColoredMessage("&f&l-----------------------------------"));

                            TextComponent previousPage = new TextComponent(MessageUtils.translateAll(player, FarmingPlus.getPlugin().getMainConfigManager().getTopPrevious()));
                            previousPage.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/fp reward top " + (page - 1)));
                            TextComponent nextPage = new TextComponent(MessageUtils.translateAll(player, FarmingPlus.getPlugin().getMainConfigManager().getTopNext()));
                            nextPage.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/fp reward top " + (page + 1)));

                            player.spigot().sendMessage(previousPage, new TextComponent(" | "), nextPage);

                            return true;

                        } else {
                            OfflinePlayer targetPlayer = Bukkit.getOfflinePlayer(args[2]);
                            if (!targetPlayer.hasPlayedBefore()) {
                                player.sendMessage(MessageUtils.translateAll(player, FarmingPlus.getPlugin().getMainConfigManager().getNotPlayer()));
                            } else {
                                int pos = FarmingPlus.getPlugin().getMainConfigManager().getTopPlayer(targetPlayer);
                                int startIndex = (1 - 1) * 10;
                                int page = Math.min(startIndex + 10, pos);
                                if (player.equals(targetPlayer))
                                    player.sendMessage(MessageUtils.translateAll(player, FarmingPlus.getPlugin().getMainConfigManager().getTopReward()));
                                Bukkit.getServer().dispatchCommand(player, "fp reward top " + page);
                                return true;
                            }

                        }
                    }

                    return true;
                } else if (args[1].equalsIgnoreCase("clear")) {
                    if (player.hasPermission("fp.admin") || player.hasPermission("fp.commands.reward.clear")) {
                        if (args.length == 3) {
                            OfflinePlayer targetPlayer = Bukkit.getOfflinePlayer(args[2]);
                            boolean done = false;
                            if (MySQLData.isDatabaseConnected(FarmingPlus.getConnectionMySQL()) && MySQLData.existsUUID(FarmingPlus.getConnectionMySQL(), targetPlayer.getUniqueId().toString())) {
                                MySQLData.deleteRewardsForUUID(FarmingPlus.getConnectionMySQL(), targetPlayer.getUniqueId());
                                done = true;
                            }
                            if (!targetPlayer.hasPlayedBefore() && !FarmingPlus.getPlugin().getMainConfigManager().getRewardsCounterMap().containsKey(targetPlayer.getUniqueId())) {
                                player.sendMessage(MessageUtils.translateAll(player, FarmingPlus.getPlugin().getMainConfigManager().getNotPlayer()));
                            } else {
                                List<Map.Entry<UUID, RewardsCounter>> list = new ArrayList<>(FarmingPlus.getPlugin().getMainConfigManager().getRewardsCounterMap().entrySet());
                                for (Map.Entry<UUID, RewardsCounter> entry : list) {
                                    if (entry.getKey().equals(targetPlayer.getUniqueId())) {
                                        FarmingPlus.getPlugin().getMainConfigManager().getRewardsCounterMap().remove(entry.getKey());
                                        done = true;
                                        break;
                                    }
                                }
                            }
                            if (done)
                                player.sendMessage(MessageUtils.translateAll(targetPlayer, FarmingPlus.getPlugin().getMainConfigManager().getRewardsCleared()));
                            else
                                player.sendMessage(MessageUtils.translateAll(player, "%farmingplus_prefix%&eUsage: /fp reward clear <player>"));
                        }else{
                            player.sendMessage(MessageUtils.translateAll(player, "%farmingplus_prefix%&eUsage: /fp reward clear <player>"));
                        }

                        return true;
                    } else
                        player.sendMessage(MessageUtils.translateAll(player, FarmingPlus.getPlugin().getMainConfigManager().getNoPermissionCommand()));
                } else {
                    player.sendMessage(MessageUtils.translateAll(player, FarmingPlus.getPlugin().getMainConfigManager().getNotCommand()));
                    player.sendMessage(MessageUtils.translateAll(player, FarmingPlus.getPlugin().getMainConfigManager().getUseHelp()));
                }
            }else{
                player.sendMessage(MessageUtils.translateAll(player, FarmingPlus.getPlugin().getMainConfigManager().getNotCommand()));
                player.sendMessage(MessageUtils.translateAll(player, FarmingPlus.getPlugin().getMainConfigManager().getUseHelp()));
            }

        }

        return true;
    }

    // RETURNS ALL THE COMMANDS //
    public void help(Player player) {
        if (!FarmingPlus.getPlugin().getMainConfigManager().getCommandList().isEmpty())
            for (String line: FarmingPlus.getPlugin().getMainConfigManager().getCommandList()){
                player.sendMessage(MessageUtils.translateAll(player, line));
            }
    }

    // Reload config if player has permissions //
    public void subCommandReload(CommandSender sender){
        if (!sender.hasPermission("fp.commands.reload") && !sender.hasPermission("fp.admin")){
            sender.sendMessage(MessageUtils.translateAll((Player) sender, FarmingPlus.getPlugin().getMainConfigManager().getNoPermissionCommand()));
            return;
        }
        FarmingPlus.getPlugin().getMainConfigManager().reloadConfig();
        if (sender instanceof Player)
            sender.sendMessage(MessageUtils.translateAll((Player) sender, FarmingPlus.getPlugin().getMainConfigManager().getReloadedConfig()));
        Bukkit.getConsoleSender().sendMessage(MessageUtils.translateAll(null, FarmingPlus.getPlugin().getMainConfigManager().getReloadedConfig()));
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {

        if (args.length == 1) {
            List<String> list = new ArrayList<>();
            if (sender.hasPermission("fp.admin"))
                return Arrays.asList("enchant","reload", "reward", "help");

            if (sender.hasPermission("fp.commands.enchant"))
                list.add("enchant");
            if (sender.hasPermission("fp.commands.reload"))
                list.add("reload");
            if (sender.hasPermission("fp.commands.reward.top") || sender.hasPermission("fp.commands.reward.give") || sender.hasPermission("fp.commands.reward.list") || sender.hasPermission("fp.commands.reward.clear"))
                list.add("reward");
            if (sender.hasPermission("fp.commands.help"))
                list.add("help");
            return list;
        }

        if (args[0].equalsIgnoreCase("reward")) {
            if (args.length == 2){
                List<String> list = new ArrayList<>();
                if (sender.hasPermission("fp.admin"))
                    return Arrays.asList("give", "list", "top", "clear");
                if (sender.hasPermission("fp.commands.reward.give"))
                    list.add("give");
                if (sender.hasPermission("fp.commands.reward.list"))
                    list.add("list");
                if (sender.hasPermission("fp.commands.reward.top"))
                    list.add("top");
                if (sender.hasPermission("fp.commands.reward.clear"))
                    list.add("clear");
                return list;
                }
            if (args.length == 3) {
                if (args[1].equalsIgnoreCase("give") && sender.hasPermission("fp.commands.reward.give"))
                    return Bukkit.getOnlinePlayers().stream().map(Player::getName).toList();
                else if (args[1].equalsIgnoreCase("top") && sender.hasPermission("fp.commands.reward.top")) {
                    List<UUID> uuidList = FarmingPlus.getPlugin().getMainConfigManager().getAllRewardsCounterUuids();
                    List<String> playersNames = new ArrayList<>();
                    for (UUID uuid : uuidList) {
                        playersNames.add(Bukkit.getOfflinePlayer(uuid).getName());
                    }

                    return playersNames;
                }
                else if (args[1].equalsIgnoreCase("clear") && sender.hasPermission("fp.commands.reward.clear")) {
                    List<UUID> uuidList = FarmingPlus.getPlugin().getMainConfigManager().getAllRewardsCounterUuids();
                    List<String> playersNames = new ArrayList<>();
                    for (UUID uuid : uuidList) {
                        playersNames.add(Bukkit.getOfflinePlayer(uuid).getName());
                    }
                    return playersNames;
                }
            }
            if (args.length == 4) {
                if (args[1].equalsIgnoreCase("give") && sender.hasPermission("fp.commands.reward.give"))
                    return FarmingPlus.getPlugin().getMainConfigManager().getAllRewardNames();
            }
        } else if (args[0].equalsIgnoreCase("enchant") || args[0].equalsIgnoreCase("en")) {
            if (args.length == 2) {
                List<String> list = new ArrayList<>();
                if (sender.hasPermission("fp.commands.enchant"))
                    return Bukkit.getOnlinePlayers().stream().map(Player::getName).toList();
            }
            if (args.length == 3) {
                if (sender.hasPermission("fp.commands.enchant")) {
                    List<String> list = new ArrayList<>();
                    list.addAll(Arrays.asList("farmersgrace", "replenish", "delicate", "irrigate", "grandtilling", "farmerstep"));
                    return list;
                }
            }
            if (args.length == 4) {
                if (sender.hasPermission("fp.commands.enchant")) {
                    if (args[2].equalsIgnoreCase("grandtilling") || args[2].equalsIgnoreCase("farmerstep")) {
                        List<String> list = new ArrayList<>();
                        list.add("1");
                        list.add("2");
                        list.add("3");
                        return list;
                    } else {
                        List<String> list = new ArrayList<>();
                        list.add("1");
                        return list;
                    }
                }
            }
            if (args.length == 5) {
                if (sender.hasPermission("fp.commands.enchant")) {
                    List<String> list = new ArrayList<>();
                    switch (args[2].toLowerCase()) {
                        case "farmersgrace", "farmerstep":
                            for (Material line : ItemUtils.boots)
                                list.add(line.toString());
                            return list;
                        case "replenish":
                            for (Material line : ItemUtils.hoes)
                                list.add(line.toString());
                            for (Material line : ItemUtils.axes)
                                list.add(line.toString());
                            return list;
                        case "delicate":
                            for (Material line : ItemUtils.axes)
                                list.add(line.toString());
                            return list;
                        case "irrigate":
                            list.add("WATER_BUCKET");
                            return list;
                        case "grandtilling":
                            for (Material line : ItemUtils.hoes)
                                list.add(line.toString());
                            return list;
                    }
                }

            }
        }


        return new ArrayList<>();
    }

    public String enchantCommandUsage(){
        return "&cUsage: /fp enchant <player> <enchant> <level> <itemId>";
    }
}

