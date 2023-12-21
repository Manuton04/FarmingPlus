package fp.manuton.commands;

import fp.manuton.FarmingPlus;
import fp.manuton.enchantments.CustomEnchantments;
import fp.manuton.guis.EnchantGui;
import fp.manuton.utils.ItemUtils;
import fp.manuton.utils.MessageUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
                        player.sendMessage(MessageUtils.getColoredMessage(FarmingPlus.prefix+"&cThat enchantment doesn't exist!"));
                    }
                }else {
                    sender.sendMessage(MessageUtils.getColoredMessage("&f&l------" + FarmingPlus.prefix + " &fEnchants&f&l-------"));
                    sender.sendMessage(MessageUtils.getColoredMessage("&e Replenish I"));
                    sender.sendMessage(MessageUtils.getColoredMessage("&e Farmer's Grace I"));
                    sender.sendMessage(MessageUtils.getColoredMessage("&e"));
                    sender.sendMessage(MessageUtils.getColoredMessage("&e"));
                    sender.sendMessage(MessageUtils.getColoredMessage("&e"));
                    sender.sendMessage(MessageUtils.getColoredMessage("&f&l---------------------------------"));
                }

            }else if (args[0].equalsIgnoreCase("reload")){
                subCommandReload(player);
            }else if (args[0].equalsIgnoreCase("gui")){
                EnchantGui.createGui(player);
            }else{
                sender.sendMessage(MessageUtils.getColoredMessage(FarmingPlus.prefix+"&cThat command does not exist!"));
                help(player);
            }

        }else{
            // /farmingplus or /fp //
            help(player);

        }

        return true;
    }

    // RETURNS ALL THE COMMANDS //
    public void help(CommandSender sender) {
        sender.sendMessage(MessageUtils.getColoredMessage("&f&l------" + FarmingPlus.prefix + " &fCommands&f&l-------"));
        sender.sendMessage(MessageUtils.getColoredMessage("&7(All the commands can be used with /fp)"));
        sender.sendMessage(MessageUtils.getColoredMessage("&e /fp: &7Show this page."));
        sender.sendMessage(MessageUtils.getColoredMessage("&e /fp reload: &7Reload the Configuration."));
        sender.sendMessage(MessageUtils.getColoredMessage("&e /fp enchant (enchantment): &7Enchants the item in hand."));
        sender.sendMessage(MessageUtils.getColoredMessage(""));
        sender.sendMessage(MessageUtils.getColoredMessage("&f&l---------------------------------"));
    }

    // Reload config if player has permissions //
    public void subCommandReload(CommandSender sender){
        if (!sender.hasPermission("fp.commands.reload") && !sender.isOp()){
            sender.sendMessage(MessageUtils.getColoredMessage(FarmingPlus.prefix+FarmingPlus.getPlugin().getMainConfigManager().getNoPermissionCommand()));
            return;
        }
        FarmingPlus.getPlugin().getMainConfigManager().reloadConfig();
        sender.sendMessage(MessageUtils.getColoredMessage(FarmingPlus.prefix+FarmingPlus.getPlugin().getMainConfigManager().getReloadedConfig()));
        Bukkit.getConsoleSender().sendMessage(MessageUtils.getColoredMessage(FarmingPlus.prefix+FarmingPlus.getPlugin().getMainConfigManager().getReloadedConfig()));
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {

        if (args.length == 1)
            return Arrays.asList("enchant", "reload");

        if (args[0].equalsIgnoreCase("enchant")) {
            if (args.length == 2)
                return Arrays.asList("delicate", "farmersgrace", "farmerstep", "grandtilling", "replenish");
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
            }


        }


        return new ArrayList<>(); // null = all player names
    }
}

