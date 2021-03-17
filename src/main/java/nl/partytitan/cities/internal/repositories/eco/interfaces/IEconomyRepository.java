package nl.partytitan.cities.internal.repositories.eco.interfaces;

import org.bukkit.OfflinePlayer;

public interface IEconomyRepository {
    boolean add(OfflinePlayer player, double amount);
    boolean add(String bankName, double amount);
    boolean subtract(OfflinePlayer player, double amount);
    boolean subtract(String bankName, double amount);
    boolean hasAccount(OfflinePlayer player);

    double getBalance(OfflinePlayer player);
    double getBalance(String bankName);

    boolean hasBalance(String bankName, double amount);
    void createBank(String bankName, OfflinePlayer owner);
    void removeBank(String bankName);
}
