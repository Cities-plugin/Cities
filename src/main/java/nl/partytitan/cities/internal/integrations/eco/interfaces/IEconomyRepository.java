package nl.partytitan.cities.internal.integrations.eco.interfaces;

import org.bukkit.OfflinePlayer;

import java.util.UUID;

public interface IEconomyRepository {
    boolean add(OfflinePlayer player, double amount);
    boolean add(UUID cityId, double amount);
    boolean subtract(OfflinePlayer player, double amount);
    boolean subtract(UUID cityId, double amount);
    boolean hasAccount(OfflinePlayer player);

    double getBalance(OfflinePlayer player);
    double getBalance(UUID cityId);

    boolean hasBalance(UUID cityId, double amount);
    void createBank(UUID cityId, OfflinePlayer owner);
    void removeBank(UUID cityId);

    String formatBalance(double balance);
}
