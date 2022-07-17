package nl.partytitan.cities.internal.entities;

import nl.partytitan.cities.internal.utils.injection.RepositoryUtil;
import nl.partytitan.cities.internal.valueobjects.Coord;

import java.util.UUID;

public class CityBlock extends Coord {
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
        return RepositoryUtil.getCityRepository().getCity(cityId);
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
        return RepositoryUtil.getResidentRepository().getResident(residentId);
    }

    public void setResidentId(UUID residentId) {
        this.residentId = residentId;
    }

    public String getPlanetName(){
        return getWorldName();
    }

    public Planet getPlanet() {
        return RepositoryUtil.getPlanetRepository().getPlanet(getWorldName());
    }
}
