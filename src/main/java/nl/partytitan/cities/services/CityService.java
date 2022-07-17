package nl.partytitan.cities.services;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import nl.partytitan.cities.internal.config.SettingsConfig;
import nl.partytitan.cities.internal.entities.City;
import nl.partytitan.cities.internal.entities.Resident;
import nl.partytitan.cities.internal.integrations.eco.interfaces.IEconomyRepository;
import nl.partytitan.cities.internal.repositories.interfaces.ICityBlockRepository;
import nl.partytitan.cities.internal.repositories.interfaces.ICityRepository;
import nl.partytitan.cities.internal.repositories.interfaces.IPlanetRepository;
import nl.partytitan.cities.internal.repositories.interfaces.IResidentRepository;
import nl.partytitan.cities.internal.tasks.CityBlockClaimTask;
import nl.partytitan.cities.internal.utils.MessageUtil;
import nl.partytitan.cities.internal.utils.server.SchedulerUtil;
import nl.partytitan.cities.internal.utils.TranslationUtil;
import nl.partytitan.cities.internal.valueobjects.Coord;
import nl.partytitan.cities.services.interfaces.ICityService;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.UUID;

@Singleton
public class CityService implements ICityService {

    private ICityRepository cityRepository;
    private IResidentRepository residentRepository;
    private IPlanetRepository planetRepository;
    private ICityBlockRepository cityBlockRepository;
    private IEconomyRepository economyRepository;

    private SettingsConfig settings;

    @Inject
    public CityService(ICityRepository cityRepository, IResidentRepository residentRepository, IPlanetRepository planetRepository, ICityBlockRepository cityBlockRepository, IEconomyRepository economyRepository, SettingsConfig settings) {
        this.cityRepository = cityRepository;
        this.residentRepository = residentRepository;
        this.planetRepository = planetRepository;
        this.cityBlockRepository = cityBlockRepository;
        this.economyRepository = economyRepository;
        this.settings = settings;
    }


    @Override
    public void createCity(String name, Player player) {
        if(!planetRepository.getPlanet(player.getWorld().getName()).isCitiesAllowedInWorld()){
            MessageUtil.sendErrorMsg(player, TranslationUtil.of("planet_err_cities_not_enabled"));
            return;
        }

        if(!economyRepository.hasBalance(player, settings.getNewCityPrice())){
            MessageUtil.sendErrorMsg(player, TranslationUtil.of("city_err_no_funds_new_city", economyRepository.formatBalance(settings.getNewCityPrice())));
            return;
        }

        Coord coord = Coord.parseCoord(player.getLocation().getChunk());
        if(cityBlockRepository.cityBlockExists(coord)){
            MessageUtil.sendErrorMsg(player, TranslationUtil.of("city_err_chunk_already_claimed"));
            return;
        }

        Resident mayorToBe = residentRepository.getResident(player.getUniqueId());

        if(mayorToBe.hasCity())
        {
            MessageUtil.sendErrorMsg(player, TranslationUtil.of("resident_err_already_in_city"));
            return;
        }
        Location spawnLocation = player.getLocation();

        economyRepository.subtract(player, settings.getNewCityPrice());

        City city = new City(UUID.randomUUID(), name, player);
        city.setSpawn(spawnLocation);
        city.setHomeBlockId(coord.getIdentifier());
        cityRepository.createCity(city);

        SchedulerUtil.scheduleSyncDelayedTask(new CityBlockClaimTask(player, city, mayorToBe, coord), 0L);

        mayorToBe.setCity(city.getId());
        residentRepository.updateResident(mayorToBe);

        MessageUtil.sendGlobalMessage(TranslationUtil.of("city_created", mayorToBe.getUsername(), name));
    }

    @Override
    public void claimChunk(Player player, City city, Resident claimer) {
        if(city.isBankrupt()){
            MessageUtil.sendErrorMsg(player, TranslationUtil.of("city_err_bankrupt_cannot_claim"));
            return;
        }

        if(city.availableClaims() == 0){
            MessageUtil.sendErrorMsg(player, TranslationUtil.of("city_err_not_enough_claims"));
            return;
        }

        Coord coord = Coord.parseCoord(player.getLocation().getChunk());
        MessageUtil.sendMsg(player, TranslationUtil.of("city_processing_claim"));

        SchedulerUtil.scheduleSyncDelayedTask(new CityBlockClaimTask(player, city, claimer, coord), 0L);
    }
}
