package nl.partytitan.cities.internal.entities;

import com.google.inject.Inject;
import nl.partytitan.cities.internal.integrations.eco.interfaces.IEconomyRepository;
import nl.partytitan.cities.internal.repositories.interfaces.ICityRepository;
import org.bukkit.Server;

import java.util.UUID;

public class Resident {
    private UUID uuid = null;
    private String username;
    private long lastOnline;
    private long registered;

    private String title;
    private String surname;

    private UUID cityId;

    @Inject
    private transient ICityRepository cityRepository;

    @Inject
    private transient IEconomyRepository economyRepository;

    @Inject
    private transient Server server;

    public Resident(UUID playerId, String playerName) {
        this.uuid = playerId;
        this.username = playerName;
    }

    public UUID getUuid() {
        return uuid;
    }

    public String getUsername() {
        return username;
    }

    public long getLastOnline() {
        return lastOnline;
    }

    public void setLastOnline(long lastOnline) {
        this.lastOnline = lastOnline;
    }

    public long getRegistered() {
        return registered;
    }

    public void setRegistered(long registered) {
        this.registered = registered;
    }

    public boolean hasCity() {
        return cityId != null;
    }

    public City getCity() {
        return cityRepository.getCity(cityId);
    }

    public void setCity(UUID cityId) { this.cityId = cityId; }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean hasTitle() {
        return title != null && !title.isEmpty();
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public boolean hasSurname() {
        return surname != null && !surname.isEmpty();
    }

    public String getFormattedName(){
        String title = hasTitle() ? getTitle() + " " : "";
        String surName = hasSurname() ? " " + getSurname() : "";

        return title + getUsername() + surName;
    }

    public double getBalance(){
        return economyRepository.getBalance(server.getOfflinePlayer(uuid));
    }

    public String getFormattedBalance() {
        return economyRepository.formatBalance(getBalance());
    }
}
