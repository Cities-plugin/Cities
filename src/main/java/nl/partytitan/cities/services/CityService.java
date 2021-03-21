package nl.partytitan.cities.services;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import nl.partytitan.cities.internal.entities.City;
import nl.partytitan.cities.internal.entities.Resident;
import nl.partytitan.cities.internal.repositories.interfaces.ICityBlockRepository;
import nl.partytitan.cities.internal.repositories.interfaces.ICityRepository;
import nl.partytitan.cities.internal.repositories.interfaces.IPlanetRepository;
import nl.partytitan.cities.internal.repositories.interfaces.IResidentRepository;
import nl.partytitan.cities.internal.tasks.CityBlockClaimTask;
import nl.partytitan.cities.internal.tasks.player.OnPlayerLoginTask;
import nl.partytitan.cities.internal.utils.MessageUtil;
import nl.partytitan.cities.internal.utils.SchedulerUtil;
import nl.partytitan.cities.internal.utils.TranslationUtil;
import nl.partytitan.cities.internal.valueobjects.Coord;
import org.bukkit.entity.Player;

import java.util.UUID;

@Singleton
public class CityService implements ICityService{

    private ICityRepository cityRepository;
    private IResidentRepository residentRepository;
    private IPlanetRepository planetRepository;
    private ICityBlockRepository cityBlockRepository;

    @Inject
    public CityService(ICityRepository cityRepository, IResidentRepository residentRepository, IPlanetRepository planetRepository, ICityBlockRepository cityBlockRepository) {
        this.cityRepository = cityRepository;
        this.residentRepository = residentRepository;
        this.planetRepository = planetRepository;
        this.cityBlockRepository = cityBlockRepository;
    }


    @Override
    public void createCity(String name, Resident mayor) {
        City city = new City(UUID.randomUUID(), name);
        city.setMayorId(mayor.getUuid());
        city.addResident(mayor.getUuid());
        cityRepository.createCity(city);

        mayor.setCity(city.getId());
        residentRepository.updateResident(mayor);
    }

    @Override
    public void claimChunk(Player player, City city, Resident claimer) {

        Coord coord = Coord.parseCoord(player.getLocation().getChunk());
        MessageUtil.sendMsg(player, TranslationUtil.of("city_processing_claim"));

        SchedulerUtil.scheduleSyncDelayedTask(new CityBlockClaimTask(player, city, claimer, coord), 0L);
    }
}
