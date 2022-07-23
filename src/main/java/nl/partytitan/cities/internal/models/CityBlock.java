package nl.partytitan.cities.internal.models;

import com.google.inject.Inject;
import nl.partytitan.cities.internal.valueobjects.Coord;
import nl.partytitan.cities.persistence.interfaces.ICityRepository;
import nl.partytitan.cities.persistence.interfaces.IPlanetRepository;
import nl.partytitan.cities.persistence.interfaces.IResidentRepository;

import java.util.UUID;

public class CityBlock extends Coord {
    @Inject
    private IResidentRepository residentRepository;

    @Inject
    private ICityRepository cityRepository;

    @Inject
    private IPlanetRepository planetRepository;

    private UUID cityId = null;
    private UUID residentId = null;
    private String name = "";
    private double plotPrice = -1;

    public CityBlock(Coord coord){
        super(coord);
    }

    public Coord getCoord() {

        return this;
    }

    public UUID getCityId() {
        return cityId;
    }

    public void setCityId(UUID townId) {
        this.cityId = townId;
    }

    public City getCity() {
        return cityRepository.getCity(cityId);
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

    public String getPlanetName(){
        return getWorldName();
    }

    public Planet getPlanet() {
        return planetRepository.getPlanet(getWorldName());
    }
}
