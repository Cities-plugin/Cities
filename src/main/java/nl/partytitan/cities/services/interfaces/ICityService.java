package nl.partytitan.cities.services.interfaces;

import nl.partytitan.cities.internal.entities.City;
import nl.partytitan.cities.internal.entities.Resident;
import nl.partytitan.cities.internal.valueobjects.Coord;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public interface ICityService {
    void createCity(String name, Player player);
    void claimChunk(Player player, City city, Resident claimer);
}
