package nl.partytitan.cities.internal.entities;

import nl.partytitan.cities.internal.valueobjects.Coord;

import javax.xml.stream.Location;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class City {
    private UUID id;
    private String name;
    private Resident mayor;
    private String homeBlockId;
    private Location spawn;

    // virtual
    private final List<Resident> residents = new ArrayList<>();
    private CityBlock homeBlock;
    private final ConcurrentHashMap<Coord, CityBlock> cityBlocks = new ConcurrentHashMap<>();

    public City(UUID id, String name) {
        this.id = id;
        this.name = name;
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
