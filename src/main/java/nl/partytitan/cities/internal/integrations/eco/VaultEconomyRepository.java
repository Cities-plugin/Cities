package nl.partytitan.cities.internal.integrations.eco;

import net.milkbowl.vault.economy.Economy;
import nl.partytitan.cities.internal.integrations.eco.interfaces.IEconomyRepository;
import org.bukkit.OfflinePlayer;

import java.util.UUID;

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
    public boolean add(UUID cityId, double amount) {
        return this.economy.bankDeposit(cityId.toString(), amount).transactionSuccess();
    }

    @Override
    public boolean subtract(OfflinePlayer player, double amount) {
        return this.economy.withdrawPlayer(player, amount).transactionSuccess();
    }

    @Override
    public boolean subtract(UUID cityId, double amount) {
        return this.economy.bankWithdraw(cityId.toString(), amount).transactionSuccess();
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
    public double getBalance(UUID cityId) {
        return this.economy.bankBalance(cityId.toString()).balance;
    }

    @Override
    public boolean hasBalance(UUID cityId, double amount) {
        return this.economy.bankHas(cityId.toString(), amount).transactionSuccess();
    }

    @Override
    public void createBank(UUID cityId, OfflinePlayer owner) {
        this.economy.createBank(cityId.toString(), owner);
    }

    @Override
    public void removeBank(UUID cityId) {
        this.economy.deleteBank(cityId.toString());
    }

    @Override
    public String formatBalance(double balance) {
        return economy.format(balance);
    }
}
