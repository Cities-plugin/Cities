package nl.partytitan.cities.persistence.interfaces;

import nl.partytitan.cities.internal.models.CityBlock;
import nl.partytitan.cities.internal.valueobjects.Coord;

import java.util.List;

public interface ICityBlockRepository {
    List<CityBlock> getCityBlocks();
    CityBlock getCityBlock(Coord coord);
    boolean cityBlockExists(Coord coord);
    boolean createCityBlock(CityBlock cityBlock);
    boolean updateCityBlock(CityBlock cityBlock);
    boolean removeCityBlock(CityBlock cityBlock);
}

