package nl.partytitan.cities.internal.repositories.interfaces;

import nl.partytitan.cities.internal.entities.Planet;

import java.util.List;

public interface IPlanetRepository {
    List<Planet> getPlanets();
    Planet getPlanet(String planetname);
    boolean planetExists(String planetname);
    boolean createPlanet(Planet planet);
    boolean updatePlanet(Planet planet);
}
