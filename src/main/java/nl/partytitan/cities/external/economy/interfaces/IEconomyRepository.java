package nl.partytitan.cities.external.economy.interfaces;

import org.bukkit.OfflinePlayer;

import java.util.UUID;

public interface IEconomyRepository {
    boolean add(OfflinePlayer player, double amount);
    boolean subtract(OfflinePlayer player, double amount);
    boolean hasAccount(OfflinePlayer player);

    double getBalance(OfflinePlayer player);

    boolean hasBalance(OfflinePlayer player, double amount);

    String formatBalance(double balance);
}