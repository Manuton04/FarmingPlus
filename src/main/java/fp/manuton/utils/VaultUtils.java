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

    /**
     * Drops the economy provider after it failed, so the rest of the session degrades to
     * "no economy" instead of throwing on every call.
     *
     * <p>Registering with Vault only proves a provider exists, not that it works: an economy
     * plugin that failed to enable still leaves its provider behind, and every call into it
     * throws. Latching here keeps that failure from reaching callers more than once.</p>
     *
     * @param error the failure reported by the provider
     */
    private static void economyFailed(RuntimeException error){
        economy = null;
        Bukkit.getLogger().warning("[FarmingPlus] The Vault economy provider failed (" + error.getMessage()
                + "). Money costs and rewards are disabled until the server is restarted.");
    }

    public static void deposit(Player target, double amount){
        if (!hasEconomy())
            throw new UnsupportedOperationException("Vault Economy not found.");

        try {
            economy.depositPlayer(target, amount);
        } catch (RuntimeException e) {
            economyFailed(e);
            throw new UnsupportedOperationException("Vault Economy is not usable.", e);
        }
    }

    public static void extract(Player target, double amount){
        if (!hasEconomy())
            throw new UnsupportedOperationException("Vault Economy not found.");

        EconomyResponse r;
        try {
            r = economy.withdrawPlayer(target, amount);
        } catch (RuntimeException e) {
            economyFailed(e);
            throw new UnsupportedOperationException("Vault Economy is not usable.", e);
        }
        if(!r.transactionSuccess()) {
            target.sendMessage(MessageUtils.getColoredMessage(FarmingPlus.prefix+"You don't have enough money!"));
        }
    }

    public static double getMoney(Player target){
        if (!hasEconomy())
            throw new UnsupportedOperationException("Vault Economy not found.");

        try {
            return economy.getBalance(target);
        } catch (RuntimeException e) {
            economyFailed(e);
            throw new UnsupportedOperationException("Vault Economy is not usable.", e);
        }
    }

    public static String formatCurrencySymbol(double amount){
        if (!hasEconomy())
            throw new UnsupportedOperationException("Vault Economy not found.");

        try {
            return amount + " " + (((int) amount) == 1 ? economy.currencyNameSingular() : economy.currencyNamePlural());
        } catch (RuntimeException e) {
            economyFailed(e);
            throw new UnsupportedOperationException("Vault Economy is not usable.", e);
        }
    }

    static {
        if (Bukkit.getPluginManager().getPlugin("Vault") != null){
            setupEconomy();
            setupChat();
            setupPermissions();
        }
    }
}


