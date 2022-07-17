package nl.partytitan.cities.internal.entities.base;

import nl.partytitan.cities.internal.utils.injection.IntegrationsUtil;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;

import java.util.UUID;

public class Management {
    private UUID id;
    private String name;
    private double debtBalance = 0.0;

    private Location spawn;


    protected Management(UUID id, String name, OfflinePlayer owner){
        this.id = id;
        this.name = name;
        IntegrationsUtil.getEconomyRepository().createBank(id, owner);
    }

    public double getBalance(){
        return IntegrationsUtil.getEconomyRepository().getBalance(getId());
    }

    public boolean isBankrupt(){
        return debtBalance > 0;
    }

    public String getFormattedBalance() {
        return IntegrationsUtil.getEconomyRepository().formatBalance(getBalance());
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
}
