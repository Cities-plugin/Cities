package nl.partytitan.cities.internal.models;

import java.util.UUID;

public class Resident {
    private UUID uuid = null;
    private String username;
    private long lastOnline;
    private long registered;

    private String title;
    private String surname;

    private UUID cityId;

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

    public UUID getCityId() {
        return cityId;
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
}
