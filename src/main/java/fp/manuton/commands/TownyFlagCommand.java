package fp.manuton.commands;

import com.palmergames.bukkit.towny.TownyAPI;
import com.palmergames.bukkit.towny.object.Resident;
import com.palmergames.bukkit.towny.object.Town;
import com.palmergames.bukkit.towny.object.metadata.BooleanDataField;
import com.palmergames.bukkit.towny.utils.MetaDataUtil;
import fp.manuton.FarmingPlus;
import fp.manuton.utils.MessageUtils;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Handles the /fp towny subcommand, allowing town mayors and assistants
 * to toggle per-enchantment FarmingPlus flags on their town.
 * <p>
 * This class is only loaded when Towny is confirmed present to avoid NoClassDefFoundError.
 */
public class TownyFlagCommand {

    private static final Map<String, BooleanDataField> ENCHANTMENT_FIELDS = Map.of(
            "replenish", new BooleanDataField("farmingplus_replenish", false),
            "farmerstep", new BooleanDataField("farmingplus_farmerstep", false),
            "grandtilling", new BooleanDataField("farmingplus_grandtilling", false),
            "irrigate", new BooleanDataField("farmingplus_irrigate", false)
    );

    public static final List<String> ENCHANTMENT_NAMES = Arrays.asList(
            "replenish", "farmerstep", "grandtilling", "irrigate"
    );

    /**
     * Handles the /fp towny command.
     *
     * @param player the player executing the command
     * @param args the full command args (args[0] = "towny")
     */
    public static void handle(Player player, String[] args) {
        // Permission check
        if (!player.hasPermission("fp.town.flags") && !player.hasPermission("fp.admin")) {
            player.sendMessage(MessageUtils.translateAll(player,
                    FarmingPlus.getPlugin().getMainConfigManager().getNoPermissionCommand()));
            return;
        }

        // Usage check: /fp towny <enchantment> <on|off>
        if (args.length < 3) {
            player.sendMessage(MessageUtils.translateAll(player,
                    FarmingPlus.getPlugin().getMainConfigManager().getTownyUsage()));
            return;
        }

        // Validate enchantment name
        String enchantmentArg = args[1].toLowerCase();
        BooleanDataField field = ENCHANTMENT_FIELDS.get(enchantmentArg);
        if (field == null) {
            player.sendMessage(MessageUtils.translateAll(player,
                    FarmingPlus.getPlugin().getMainConfigManager().getTownyInvalidEnchantment()));
            return;
        }

        // Validate on/off value
        String valueArg = args[2].toLowerCase();
        boolean enable;
        if (valueArg.equals("on")) {
            enable = true;
        } else if (valueArg.equals("off")) {
            enable = false;
        } else {
            player.sendMessage(MessageUtils.translateAll(player,
                    FarmingPlus.getPlugin().getMainConfigManager().getTownyInvalidValue()));
            return;
        }

        // Check player is a Towny resident with a town
        Resident resident = TownyAPI.getInstance().getResident(player);
        if (resident == null || !resident.hasTown()) {
            player.sendMessage(MessageUtils.translateAll(player,
                    FarmingPlus.getPlugin().getMainConfigManager().getTownyNotInTown()));
            return;
        }

        // Check player is mayor or assistant
        if (!resident.isMayor() && !resident.hasTownRank("assistant")) {
            player.sendMessage(MessageUtils.translateAll(player,
                    FarmingPlus.getPlugin().getMainConfigManager().getTownyNotMayor()));
            return;
        }

        // Set the metadata flag on the town
        Town town = resident.getTownOrNull();
        if (town == null) {
            player.sendMessage(MessageUtils.translateAll(player,
                    FarmingPlus.getPlugin().getMainConfigManager().getTownyNotInTown()));
            return;
        }

        // Check if the flag is already set to the desired value
        boolean currentValue = MetaDataUtil.hasMeta(town, field) && MetaDataUtil.getBoolean(town, field);
        if (currentValue == enable) {
            String message = FarmingPlus.getPlugin().getMainConfigManager().getTownyFlagAlready();
            message = message.replace("%enchantment%", enchantmentArg);
            message = message.replace("%status%", enable ? "enabled" : "disabled");
            player.sendMessage(MessageUtils.translateAll(player, message));
            return;
        }

        MetaDataUtil.setBoolean(town, field, enable, true);

        // Send confirmation
        String message;
        if (enable) {
            message = FarmingPlus.getPlugin().getMainConfigManager().getTownyFlagEnabled();
        } else {
            message = FarmingPlus.getPlugin().getMainConfigManager().getTownyFlagDisabled();
        }
        message = message.replace("%enchantment%", enchantmentArg);
        player.sendMessage(MessageUtils.translateAll(player, message));
    }
}
