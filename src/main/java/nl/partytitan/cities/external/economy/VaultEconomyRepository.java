package nl.partytitan.cities.external.economy;

import net.milkbowl.vault.economy.Economy;
import nl.partytitan.cities.external.economy.interfaces.IEconomyRepository;
import org.bukkit.OfflinePlayer;

public class VaultEconomyRepository implements IEconomyRepository {
    private final Economy economy;


    public VaultEconomyRepository(Economy economy){
        this.economy = economy;
    }

    @Override
    public boolean add(OfflinePlayer player, double amount) {
        return this.economy.depositPlayer(player, amount).transactionSuccess();
    }

    @Override
    public boolean subtract(OfflinePlayer player, double amount) {
        return this.economy.withdrawPlayer(player, amount).transactionSuccess();
    }

    @Override
    public boolean hasAccount(OfflinePlayer player) {
        return this.economy.hasAccount(player);
    }

    @Override
    public double getBalance(OfflinePlayer player) {
        return this.economy.getBalance(player);
    }

    @Override
    public boolean hasBalance(OfflinePlayer player, double amount) {
        return this.economy.has(player, amount);
    }

    @Override
    public String formatBalance(double balance) {
        return economy.format(balance);
    }
}
