package nl.partytitan.cities.services;

import nl.partytitan.cities.internal.entities.City;
import nl.partytitan.cities.internal.entities.Resident;
import org.bukkit.World;
import org.bukkit.entity.Player;

public interface ICityService {
    void createCity(String name, Resident mayor);
    void claimChunk(Player player, City city, Resident claimer);
}
