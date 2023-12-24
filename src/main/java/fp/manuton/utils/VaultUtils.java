package fp.manuton.utils;

import fp.manuton.FarmingPlus;
import net.milkbowl.vault.chat.Chat;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;
import net.milkbowl.vault.permission.Permission;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;

public class VaultUtils {
    private static Economy economy = null;
    private static Permission permission = null;
    private static Chat chat = null;

    private VaultUtils(){
    }

    public static void setupEconomy(){
        RegisteredServiceProvider<Economy> rsp = Bukkit.getServicesManager().getRegistration(Economy.class);

        if (rsp != null)
            economy = rsp.getProvider();
    }

    public static void setupChat(){
        RegisteredServiceProvider<Chat> rsp = Bukkit.getServicesManager().getRegistration(Chat.class);

        if (rsp != null)
            chat = rsp.getProvider();
    }

    public static void setupPermissions(){
        RegisteredServiceProvider<net.milkbowl.vault.permission.Permission> rsp = Bukkit.getServicesManager().getRegistration(net.milkbowl.vault.permission.Permission.class);

        if (rsp != null)
            permission = rsp.getProvider();
    }

    public static boolean hasEconomy(){
        return economy != null;
    }

    public static void deposit(Player target, double amount){
        if (!hasEconomy())
            throw new UnsupportedOperationException("Vault Economy not found.");

        economy.depositPlayer(target, amount);
    }

    public static void extract(Player target, double amount){
        if (!hasEconomy())
            throw new UnsupportedOperationException("Vault Economy not found.");

        EconomyResponse r = economy.withdrawPlayer(target, amount);
        if(!r.transactionSuccess()) {
            target.sendMessage(MessageUtils.getColoredMessage(FarmingPlus.prefix+"You don't have enough money!"));
        }
    }

    public static double getMoney(Player target){
        if (!hasEconomy())
            throw new UnsupportedOperationException("Vault Economy not found.");

        return economy.getBalance(target);
    }

    public static String formatCurrencySymbol(double amount){
        if (!hasEconomy())
            throw new UnsupportedOperationException("Vault Economy not found.");

        return amount + " " + (((int) amount) == 1 ? economy.currencyNameSingular() : economy.currencyNamePlural());
    }

    static {
        if (Bukkit.getPluginManager().getPlugin("Vault") != null){
            setupEconomy();
            setupChat();
            setupPermissions();
        }
    }
}


