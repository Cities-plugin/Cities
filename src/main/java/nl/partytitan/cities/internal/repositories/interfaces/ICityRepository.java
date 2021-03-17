package nl.partytitan.cities.internal.repositories.interfaces;

import nl.partytitan.cities.internal.entities.City;

import java.util.List;
import java.util.UUID;

public interface ICityRepository {
    List<City> GetCities();
    City GetCity(UUID id);
    boolean CreateCity(City city);
    boolean UpdateCity(City city);
    boolean RemoveCity(City city);
}
