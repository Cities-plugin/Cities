package nl.partytitan.cities.listeners.cities;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import nl.partytitan.cities.events.PlayerChangeChunkEvent;
import nl.partytitan.cities.events.PlayerChangeCityBlockEvent;
import nl.partytitan.cities.events.PlayerEnterCityEvent;
import nl.partytitan.cities.events.PlayerLeaveCityEvent;
import nl.partytitan.cities.internal.entities.City;
import nl.partytitan.cities.internal.entities.CityBlock;
import nl.partytitan.cities.internal.repositories.interfaces.ICityBlockRepository;
import nl.partytitan.cities.internal.utils.LoggingUtil;
import nl.partytitan.cities.internal.valueobjects.Coord;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.plugin.PluginManager;

@Singleton
public class PlayerChangeChunkListener implements Listener {
    @Inject
    private PluginManager pluginManager;

    @Inject
    private ICityBlockRepository cityBlockRepository;

    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerChangeChunkEvent(PlayerChangeChunkEvent e) {
        PlayerMoveEvent pme = e.getMoveEvent();
        Player player = e.getPlayer();
        Coord from = Coord.parseCoord(e.getFrom());
        Coord to = Coord.parseCoord(e.getTo());

        boolean currentChunkHasCity = cityBlockRepository.cityBlockExists(from);
        boolean nextChunkHasCity = cityBlockRepository.cityBlockExists(to);

        if(nextChunkHasCity){
            CityBlock nextCityBlock = cityBlockRepository.getCityBlock(to);

            // Check if from city is the same as to city
            if(currentChunkHasCity){
                CityBlock currentCityBlock = cityBlockRepository.getCityBlock(from);
                if(currentCityBlock.getCityId() == nextCityBlock.getCityId()) {
                    // From and to have the same city
                    // So we are moving between cityBlocks
                    PlayerChangeCityBlockEvent playerChangeCityBlockEvent = new PlayerChangeCityBlockEvent(nextCityBlock.getCity(), pme, currentCityBlock, nextCityBlock, player);
                    pluginManager.callEvent(playerChangeCityBlockEvent);

                    return; // We are moving in the same city return to prevent a city enter event.
                } else{
                    // Leaving current city to enter another
                    PlayerLeaveCityEvent playerLeaveCityEvent = new PlayerLeaveCityEvent(currentCityBlock.getCity(), pme, currentCityBlock, to, player);
                    pluginManager.callEvent(playerLeaveCityEvent);
                }
            }
            // We are either not going to a city or we are going to a different one.
            // In either case the result is the same: Entering a city
            City nextCity = nextCityBlock.getCity();

            PlayerEnterCityEvent playerEnterCityEvent = new PlayerEnterCityEvent(nextCity, pme, from, nextCityBlock, player);
            pluginManager.callEvent(playerEnterCityEvent);

            return;
        } else {
            if(currentChunkHasCity){
                CityBlock currentCityBlock = cityBlockRepository.getCityBlock(from);
                // To has no city From does so: Going to wilderness
                PlayerLeaveCityEvent playerLeaveCityEvent = new PlayerLeaveCityEvent(currentCityBlock.getCity(), pme, currentCityBlock, to, player);
                pluginManager.callEvent(playerLeaveCityEvent);

                return;
            }
        }
        // Wilderness to wilderness: Do nothing
    }
}
