package nl.partytitan.cities.internal.models.base;

import com.google.inject.Inject;
import nl.partytitan.cities.external.economy.interfaces.IEconomyRepository;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;

import java.util.UUID;

public class Management {
    @Inject
    private IEconomyRepository economyRepository;

    private UUID id;
    private String name;
    private double debtBalance = 0.0;
    private double balance = 0.0;

    private Location spawn;


    protected Management(UUID id, String name, OfflinePlayer owner){
        this.id = id;
        this.name = name;
    }

    public double getBalance(){
        return balance;
    }

    public boolean isBankrupt(){
        return debtBalance > 0;
    }

    public String getFormattedBalance() {
        return economyRepository.formatBalance(getBalance());
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Location getSpawn() {
        return spawn;
    }

    public void setSpawn(Location spawn) {
        this.spawn = spawn;
    }

    public void AddBalance(double amount) {
        balance += amount;
    }

    public void SubtractBalance(double amount) {
        balance -= amount;
    }
}
