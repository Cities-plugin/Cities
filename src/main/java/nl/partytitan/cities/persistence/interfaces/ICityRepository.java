package nl.partytitan.cities.persistence.interfaces;

import nl.partytitan.cities.internal.models.City;

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

