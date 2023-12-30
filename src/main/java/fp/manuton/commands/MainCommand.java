package fp.manuton.commands;

import fp.manuton.FarmingPlus;
import fp.manuton.enchantments.CustomEnchantments;
import fp.manuton.guis.EnchantGui;
import fp.manuton.rewards.Reward;
import fp.manuton.rewards.SummonReward;
import fp.manuton.rewardsCounter.RewardsCounter;
import fp.manuton.utils.ItemUtils;
import fp.manuton.utils.MessageUtils;
import io.lumine.mythic.bukkit.MythicBukkit;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import java.util.*;

import static com.sk89q.worldedit.command.util.PrintCommandHelp.help;

public class MainCommand implements CommandExecutor, TabExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String alias, String[] args) {
        if (!(sender instanceof Player)){
            // CONSOLE //
            if (args.length >= 1){
                if (args[0].equalsIgnoreCase("reload"))
                    subCommandReload(sender);
                return true;
            }
            sender.sendMessage(MessageUtils.getColoredMessage(FarmingPlus.prefix+"&fThis command can only be used by a player!"));
            return true;
        }
        // PLAYER //
        Player player = (Player) sender;

        if (args.length >= 1){
            if (args[0].equalsIgnoreCase("enchant")){ // /fp enchant (Enchantment)
                if (!player.hasPermission("fp.commands.enchant")) {
                    player.sendMessage(MessageUtils.translateAll(player, FarmingPlus.getPlugin().getMainConfigManager().getNoPermissionCommand()));
                    return true;
                }
                if (args.length >= 2) {
                    List<Material> enchantable = new ArrayList<>();
                    if (args[1].equalsIgnoreCase("replenish")) { // /fp enchant replenish
                        enchantable.addAll(ItemUtils.hoes); // HOES //
                        enchantable.addAll(ItemUtils.axes); // AXES //
                        ItemUtils.enchantItem(enchantable, player, CustomEnchantments.REPLENISH,1);
                    } else if (args[1].equalsIgnoreCase("farmersgrace")){
                        enchantable.addAll(ItemUtils.boots); // BOOTS //
                        ItemUtils.enchantItem(enchantable, player, CustomEnchantments.FARMERSGRACE,1);
                    }else if (args[1].equalsIgnoreCase("delicate")){
                        enchantable.addAll(ItemUtils.axes); // AXES //
                        ItemUtils.enchantItem(enchantable, player, CustomEnchantments.DELICATE,1);
                    }else if (args[1].equalsIgnoreCase("farmerstep")){
                        enchantable.addAll(ItemUtils.boots); // BOOTS //
                        if (args.length == 3){
                            if (args[2].equalsIgnoreCase("1"))
                                ItemUtils.enchantItem(enchantable, player, CustomEnchantments.FARMERSTEP,1);
                            else if (args[2].equalsIgnoreCase("2"))
                                ItemUtils.enchantItem(enchantable, player, CustomEnchantments.FARMERSTEP,2);
                            else if (args[2].equalsIgnoreCase("3"))
                                ItemUtils.enchantItem(enchantable, player, CustomEnchantments.FARMERSTEP,3);
                        }else
                            ItemUtils.enchantItem(enchantable, player, CustomEnchantments.FARMERSTEP,1);
                    }else if (args[1].equalsIgnoreCase("grandtilling")){
                        enchantable.addAll(ItemUtils.hoes); // HOES //
                        if (args.length == 3){
                            if (args[2].equalsIgnoreCase("1"))
                                ItemUtils.enchantItem(enchantable, player, CustomEnchantments.GRANDTILLING,1);
                            else if (args[2].equalsIgnoreCase("2"))
                                ItemUtils.enchantItem(enchantable, player, CustomEnchantments.GRANDTILLING,2);
                            else if (args[2].equalsIgnoreCase("3"))
                                ItemUtils.enchantItem(enchantable, player, CustomEnchantments.GRANDTILLING,3);
                        }else
                            ItemUtils.enchantItem(enchantable, player, CustomEnchantments.GRANDTILLING,1);
                    }else if (args[1].equalsIgnoreCase("irrigate")){
                        enchantable.add(Material.WATER_BUCKET);
                        ItemUtils.enchantItem(enchantable, player, CustomEnchantments.IRRIGATE,1);
                    }else {
                        player.sendMessage(MessageUtils.translateAll(player, FarmingPlus.getPlugin().getMainConfigManager().getNotEnchantment()));
                    }
                }else {
                    for (String line: FarmingPlus.getPlugin().getMainConfigManager().getEnchantsList()){
                        player.sendMessage(MessageUtils.translateAll(player, line));
                    }
                }

            }else if (args[0].equalsIgnoreCase("reload")){
                subCommandReload(player);
            }else if (args[0].equalsIgnoreCase("gui")){
                if (player.hasPermission("fp.gui.use")) {
                    EnchantGui.createGui(player, null);
                    return true;
                }
                else {
                    player.sendMessage(MessageUtils.translateAll(player, FarmingPlus.getPlugin().getMainConfigManager().getNoPermissionCommand()));
                    return true;
                }
            }else if (args[0].equalsIgnoreCase("reward")){
                if (player.hasPermission("fp.admin") || player.hasPermission("fp.commands.reward")){
                    if (args[1].equalsIgnoreCase("give")){
                        String target = args[2];
                        boolean isOnline = false;
                        for (String players: Bukkit.getOnlinePlayers().stream().map(Player::getName).toList()){
                            if (players.equalsIgnoreCase(target)){
                                isOnline = true;
                                break;
                            }
                        }
                        if (!isOnline) {
                            player.sendMessage(MessageUtils.getColoredMessage(FarmingPlus.prefix + "&cThat player is not online!"));
                            return true;
                        }
                        if (isOnline){
                            if (FarmingPlus.getPlugin().getMainConfigManager().getAllRewardNames().contains(args[3])) {
                                Reward reward = FarmingPlus.getPlugin().getMainConfigManager().getReward(args[3]);
                                reward.give(Bukkit.getPlayer(target));
                                boolean done = true;
                                if (reward instanceof SummonReward){
                                    if (Bukkit.getPluginManager().getPlugin("MythicMobs") == null){
                                        try{
                                            EntityType.valueOf(((SummonReward) reward).getEntity());
                                        }catch(IllegalArgumentException exp){
                                            done = false;
                                            player.sendMessage(MessageUtils.translateAll(player, FarmingPlus.getPlugin().getMainConfigManager().getEntityInvalidMythic()));
                                        }
                                    }else{
                                        boolean isMythic = MythicBukkit.inst().getMobManager().getMythicMob(((SummonReward) reward).getEntity()).orElse(null) != null;
                                        if (!isMythic){
                                            try{
                                                EntityType.valueOf(((SummonReward) reward).getEntity());
                                            }catch(IllegalArgumentException exp){
                                                done = false;
                                                player.sendMessage(MessageUtils.translateAll(player, FarmingPlus.getPlugin().getMainConfigManager().getEntityInvalid()));
                                            }
                                        }

                                    }

                                }
                                if (done) {

                                    Map<UUID, RewardsCounter> rewardsCounterMap = FarmingPlus.getPlugin().getMainConfigManager().getRewardsCounterMap();
                                    UUID playerId = player.getUniqueId();
                                    RewardsCounter rewardsCounter = new RewardsCounter(new ArrayList<>());
                                    if (!rewardsCounterMap.containsKey(playerId))
                                        rewardsCounterMap.put(playerId, rewardsCounter);
                                    rewardsCounter = rewardsCounterMap.get(playerId);
                                    rewardsCounter.addRecord(FarmingPlus.getPlugin().getMainConfigManager().getKeyFromReward(reward));

                                    String message = FarmingPlus.getPlugin().getMainConfigManager().getRewardGiveCommand();
                                    message = message.replace("%reward%", args[3]);
                                    player.sendMessage(MessageUtils.translateAll(Bukkit.getPlayer(target), message));
                                }
                            }else
                                player.sendMessage(MessageUtils.translateAll(player, FarmingPlus.getPlugin().getMainConfigManager().getNotReward()));

                        }else
                            player.sendMessage(MessageUtils.translateAll(player, FarmingPlus.getPlugin().getMainConfigManager().getPlayerOffline()));

                    }else if (args[1].equalsIgnoreCase("list")) {
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
                    }else if (args[1].equalsIgnoreCase("top")){
                        player.sendMessage(MessageUtils.translateAll(player, FarmingPlus.getPlugin().getMainConfigManager().getTopReward()));
                        return true;
                    }else {
                        player.sendMessage(MessageUtils.translateAll(player, FarmingPlus.getPlugin().getMainConfigManager().getNotCommand()));
                        help(player);
                    }
                }else
                    player.sendMessage(MessageUtils.translateAll(player, FarmingPlus.getPlugin().getMainConfigManager().getNoPermissionCommand()));


            }else{
                sender.sendMessage(MessageUtils.translateAll(player, FarmingPlus.getPlugin().getMainConfigManager().getNotCommand()));
                help(player);
            }

        }else{
            // /farmingplus or /fp //
            help(player);
        }

        return true;
    }

    // RETURNS ALL THE COMMANDS //
    public void help(Player player) {
        player.sendMessage("1");
        if (FarmingPlus.getPlugin().getMainConfigManager().getCommandList().isEmpty())
            player.sendMessage("empty");
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
        Bukkit.getConsoleSender().sendMessage(MessageUtils.translateAll((Player) sender, FarmingPlus.getPlugin().getMainConfigManager().getReloadedConfig()));
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {

        if (args.length == 1)
            return Arrays.asList("enchant", "reload", "gui", "reward");

        if (args[0].equalsIgnoreCase("enchant") && (sender.hasPermission("fp.admin") || sender.hasPermission("fp.commands.enchant"))) {
            if (args.length == 2)
                return Arrays.asList("delicate", "farmersgrace", "farmerstep", "grandtilling", "replenish", "irrigate");
            if (args.length == 3) {
                if (args[1].equalsIgnoreCase("delicate"))
                    return List.of("1");
                if (args[1].equalsIgnoreCase("farmersgrace"))
                    return List.of("1");
                if (args[1].equalsIgnoreCase("farmerstep"))
                    return Arrays.asList("1", "2", "3");
                if (args[1].equalsIgnoreCase("grandtilling"))
                    return Arrays.asList("1", "2", "3");
                if (args[1].equalsIgnoreCase("replenish"))
                    return List.of("1");
                if (args[1].equalsIgnoreCase("irrigate"))
                    return List.of("1");
            }
        }

        if (args[0].equalsIgnoreCase("reward") && (sender.hasPermission("fp.admin") || sender.hasPermission("fp.commands.reward"))) {
            if (args.length == 2)
                return Arrays.asList("give", "list", "top");
            if (args.length == 3) {
                if (args[1].equalsIgnoreCase("give"))
                    return Bukkit.getOnlinePlayers().stream().map(Player::getName).toList();
            }
            if (args.length == 4) {
                if (args[1].equalsIgnoreCase("give"))
                    return FarmingPlus.getPlugin().getMainConfigManager().getAllRewardNames();
            }
        }


        return new ArrayList<>(); // null = all player names
    }
}

