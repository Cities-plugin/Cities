package nl.partytitan.cities.internal.repositories.interfaces;

import nl.partytitan.cities.internal.entities.CityBlock;
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
