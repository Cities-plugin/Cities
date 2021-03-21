package nl.partytitan.cities.internal.entities;

import com.google.inject.Inject;
import nl.partytitan.cities.internal.repositories.interfaces.ICityRepository;
import nl.partytitan.cities.internal.repositories.interfaces.IPlanetRepository;
import nl.partytitan.cities.internal.repositories.interfaces.IResidentRepository;
import nl.partytitan.cities.internal.valueobjects.Coord;

import java.util.UUID;

public class CityBlock {
    private String planetName;
    private UUID cityId = null;
    private UUID residentId = null;
    private String name = "";
    private int x, z;
    private double plotPrice = -1;

    @Inject
    private transient ICityRepository cityRepository;

    @Inject
    private transient IResidentRepository residentRepository;

    @Inject
    private transient IPlanetRepository planetRepository;

    public CityBlock(Coord coord){

        this.planetName = coord.getWorldName();
        this.x = coord.getX();
        this.z = coord.getZ();
    }

    public Coord getCoord() {

        return new Coord(x, z, planetName);
    }

    public String getPlanetName(){
        return planetName;
    }

    public Planet getPlanet() {
        return planetRepository.getPlanet(planetName);
    }

    public String getIdentifier() { return planetName + "_" + x + "_" + z; }

    public UUID getCityId() {
        return cityId;
    }

    public void setCityId(UUID townId) {
        this.cityId = townId;
    }

    public City getCity() {
        return cityRepository.getCity(cityId);
    }

    public int getX() {
        return x;
    }

    public int getZ() {
        return z;
    }

    public boolean hasName() { return name.isEmpty(); }

    public String getName() {
        return name;
    }

    public void setName(String cityBlockName) {
        this.name = cityBlockName;
    }

    public boolean hasResident(){
        return residentId != null;
    }

    public UUID getResidentId() {
        return residentId;
    }

    public Resident getResident() {
        return residentRepository.getResident(residentId);
    }

    public void setResidentId(UUID residentId) {
        this.residentId = residentId;
    }
}
