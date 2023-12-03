package fp.manuton.commands;

import fp.manuton.FarmingPlus;
import fp.manuton.enchantments.CustomEnchantments;
import fp.manuton.utils.MessageUtils;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class MainCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String alias, String[] args) {
        if (!(sender instanceof Player)){
            // CONSOLE //
            sender.sendMessage(MessageUtils.getColoredMessage(FarmingPlus.prefix+" &fThis command can only be used by a player!"));
            return true;
        }
        // PLAYER //
        Player player = (Player) sender;

        if (args.length >= 1){
            if (args[0].equalsIgnoreCase("enchant")){
                if (args.length >= 2) {
                    if (args[1].equalsIgnoreCase("replenish")) {

                        List<Material> enchantable = new ArrayList<Material>();
                        enchantable.add(Material.WOODEN_HOE);
                        enchantable.add(Material.STONE_HOE);
                        enchantable.add(Material.IRON_HOE);
                        enchantable.add(Material.GOLDEN_HOE);
                        enchantable.add(Material.DIAMOND_HOE);
                        enchantable.add(Material.NETHERITE_HOE);
                        enchantable.add(Material.WOODEN_AXE);
                        enchantable.add(Material.STONE_AXE);
                        enchantable.add(Material.IRON_AXE);
                        enchantable.add(Material.GOLDEN_AXE);
                        enchantable.add(Material.DIAMOND_AXE);
                        enchantable.add(Material.NETHERITE_AXE);
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
                            player.sendMessage(MessageUtils.getColoredMessage(FarmingPlus.prefix+" &cThat enchantment can only be applied on hoes or axes!"));
                    } else {
                        player.sendMessage(MessageUtils.getColoredMessage(FarmingPlus.prefix+" &cThat enchantment doesn't exist!"));
                    }
                }else {
                    sender.sendMessage(MessageUtils.getColoredMessage(" &f&l------" + FarmingPlus.prefix + " &fEnchants&f&l-------"));
                    sender.sendMessage(MessageUtils.getColoredMessage("&e Replenish I"));
                    sender.sendMessage(MessageUtils.getColoredMessage("&e"));
                    sender.sendMessage(MessageUtils.getColoredMessage("&e"));
                    sender.sendMessage(MessageUtils.getColoredMessage("&e"));
                    sender.sendMessage(MessageUtils.getColoredMessage("&e"));
                    sender.sendMessage(MessageUtils.getColoredMessage("&f&l---------------------------------"));
                }

            }else if (args[0].equalsIgnoreCase("3")){

            }else{
                sender.sendMessage(MessageUtils.getColoredMessage(FarmingPlus.prefix+" &cThat command does not exist!"));
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

        if (sender.isOp() || sender.hasPermission("farmingplus.adminhelp")) {
            // WITH PERMS OR OPS //
            sender.sendMessage(MessageUtils.getColoredMessage(" &f&l------" + FarmingPlus.prefix + " &fCommands&f&l-------"));
            sender.sendMessage(MessageUtils.getColoredMessage("&7(All the commands can be used with /fp)"));
            sender.sendMessage(MessageUtils.getColoredMessage("&e /farmingplus : &7 Show this page."));
            sender.sendMessage(MessageUtils.getColoredMessage(""));
            sender.sendMessage(MessageUtils.getColoredMessage(""));
            sender.sendMessage(MessageUtils.getColoredMessage(""));
            sender.sendMessage(MessageUtils.getColoredMessage(""));
            sender.sendMessage(MessageUtils.getColoredMessage("&f&l---------------------------------"));
        } else {
            // WITHOUT PERMS //
            sender.sendMessage(MessageUtils.getColoredMessage(" &f&l------" + FarmingPlus.prefix + " &fCommands&f&l-------"));
            sender.sendMessage(MessageUtils.getColoredMessage("&7(All the commands can be used with /fp)"));
            sender.sendMessage(MessageUtils.getColoredMessage("&e /farmingplus : &7 Show this page."));
            sender.sendMessage(MessageUtils.getColoredMessage(""));
            sender.sendMessage(MessageUtils.getColoredMessage(""));
            sender.sendMessage(MessageUtils.getColoredMessage(""));
            sender.sendMessage(MessageUtils.getColoredMessage(""));
            sender.sendMessage(MessageUtils.getColoredMessage("&f&l---------------------------------"));
        }

    }

}

