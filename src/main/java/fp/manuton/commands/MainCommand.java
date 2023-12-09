package fp.manuton.commands;

import fp.manuton.FarmingPlus;
import fp.manuton.enchantments.CustomEnchantments;
import fp.manuton.utils.ItemUtils;
import fp.manuton.utils.MessageUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class MainCommand implements CommandExecutor {

    private FarmingPlus plugin;

    public MainCommand(FarmingPlus plugin) {
        this.plugin = plugin;
    }

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
                        ItemStack itemUser = new ItemStack(player.getItemInHand());
                        boolean enchanted = false;
                        for (Material type : enchantable) {
                            if (itemUser.getType() == type) {
                                player.getInventory().remove(itemUser);
                                itemUser.addUnsafeEnchantment(CustomEnchantments.REPLENISH, 1);
                                ItemMeta meta = itemUser.getItemMeta();
                                List<String> lore = new ArrayList<String>();
                                lore.add(MessageUtils.getColoredMessage("&7Replenish I"));
                                if (meta.hasLore())
                                    lore.addAll(meta.getLore());
                                meta.setLore(lore);
                                itemUser.setItemMeta(meta);

                                player.getInventory().addItem(itemUser);
                                return true;
                            }
                        }
                        if (!enchanted)
                            player.sendMessage(MessageUtils.getColoredMessage(FarmingPlus.prefix+"&cThat enchantment can only be applied on hoes or axes!"));
                    } else {
                        player.sendMessage(MessageUtils.getColoredMessage(FarmingPlus.prefix+"&cThat enchantment doesn't exist!"));
                    }
                }else {
                    sender.sendMessage(MessageUtils.getColoredMessage("&f&l------" + FarmingPlus.prefix + " &fEnchants&f&l-------"));
                    sender.sendMessage(MessageUtils.getColoredMessage("&e Replenish I"));
                    sender.sendMessage(MessageUtils.getColoredMessage("&e"));
                    sender.sendMessage(MessageUtils.getColoredMessage("&e"));
                    sender.sendMessage(MessageUtils.getColoredMessage("&e"));
                    sender.sendMessage(MessageUtils.getColoredMessage("&e"));
                    sender.sendMessage(MessageUtils.getColoredMessage("&f&l---------------------------------"));
                }

            }else if (args[0].equalsIgnoreCase("reload")){
                subCommandReload(player);
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
            sender.sendMessage(MessageUtils.getColoredMessage(FarmingPlus.prefix+plugin.getMainConfigManager().getNoPermissionCommand()));
            return;
        }
        plugin.getMainConfigManager().reloadConfig();
        sender.sendMessage(MessageUtils.getColoredMessage(FarmingPlus.prefix+plugin.getMainConfigManager().getReloadedConfig()));
        Bukkit.getConsoleSender().sendMessage(MessageUtils.getColoredMessage(FarmingPlus.prefix+plugin.getMainConfigManager().getReloadedConfig()));
    }

}

