package nl.partytitan.cities.internal.entities;

import nl.partytitan.cities.internal.valueobjects.Coord;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class Resident {
    private UUID uuid = null;
    private String username;
    private long lastOnline;
    private long registered;

    // virtual
    private City city;
    private ConcurrentHashMap<Coord, CityBlock> cityBlocks = new ConcurrentHashMap<>();

    public UUID getUuid() {
        return uuid;
    }

    public String getUsername() {
        return username;
    }
}
