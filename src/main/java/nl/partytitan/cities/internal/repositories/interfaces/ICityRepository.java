package nl.partytitan.cities.internal.repositories.interfaces;

import nl.partytitan.cities.internal.entities.City;

import java.util.List;
import java.util.UUID;

public interface ICityRepository {
    List<City> getCities();
    City getCity(UUID id);
    City getCity(String cityName);
    boolean createCity(City city);
    boolean updateCity(City city);
    boolean removeCity(City city);
}
