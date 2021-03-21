package nl.partytitan.cities.internal.tasks;

import com.google.inject.Inject;
import nl.partytitan.cities.api.exception.ChunkAlreadyClaimedException;
import nl.partytitan.cities.internal.entities.City;
import nl.partytitan.cities.internal.entities.CityBlock;
import nl.partytitan.cities.internal.entities.Resident;
import nl.partytitan.cities.internal.repositories.interfaces.ICityBlockRepository;
import nl.partytitan.cities.internal.repositories.interfaces.ICityRepository;
import nl.partytitan.cities.internal.repositories.interfaces.IPlanetRepository;
import nl.partytitan.cities.internal.utils.MessageUtil;
import nl.partytitan.cities.internal.utils.TranslationUtil;
import nl.partytitan.cities.internal.valueobjects.Coord;
import org.bukkit.entity.Player;

public class CityBlockClaimTask implements Runnable {
    private Player player;
    private City city;
    private Resident resident;
    private Coord coord;

    @Inject
    private IPlanetRepository planetRepository;

    @Inject
    private ICityBlockRepository cityBlockRepository;

    @Inject
    private ICityRepository cityRepository;

    public CityBlockClaimTask(Player player, City city, Resident resident, Coord coord){
        super();
        this.player = player;
        this.city = city;
        this.resident = resident;
        this.coord = coord;
    }

    @Override
    public void run() throws ChunkAlreadyClaimedException {

        if(cityBlockRepository.cityBlockExists(coord)){
            MessageUtil.sendErrorMsg(player, TranslationUtil.of("city_chunk_already_claimed"));
            return;
        }

        CityBlock cityBlock = new CityBlock(coord);
        cityBlock.setCityId(city.getId());

        cityBlockRepository.createCityBlock(cityBlock);
        city.addCityBlock(cityBlock);
        cityRepository.updateCity(city);

        MessageUtil.sendMsg(player, TranslationUtil.of("city_claimed", coord.getWorldName(), coord.getX(), coord.getZ()));
    }
}
