package nl.partytitan.cities.internal.repositories.eco;

import net.milkbowl.vault.economy.Economy;
import nl.partytitan.cities.internal.repositories.eco.interfaces.IEconomyRepository;
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
    public boolean add(String bankName, double amount) {
        return this.economy.bankDeposit(bankName, amount).transactionSuccess();
    }

    @Override
    public boolean subtract(OfflinePlayer player, double amount) {
        return this.economy.withdrawPlayer(player, amount).transactionSuccess();
    }

    @Override
    public boolean subtract(String bankName, double amount) {
        return this.economy.bankWithdraw(bankName, amount).transactionSuccess();
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
    public double getBalance(String bankName) {
        return this.economy.bankBalance(bankName).balance;
    }

    @Override
    public boolean hasBalance(String bankName, double amount) {
        return this.economy.bankHas(bankName, amount).transactionSuccess();
    }

    @Override
    public void createBank(String bankName, OfflinePlayer owner) {
        this.economy.createBank(bankName, owner);
    }

    @Override
    public void removeBank(String bankName) {
        this.economy.deleteBank(bankName);
    }
}
