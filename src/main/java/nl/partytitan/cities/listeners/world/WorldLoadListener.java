package nl.partytitan.cities.listeners.world;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import nl.partytitan.cities.internal.entities.Planet;
import nl.partytitan.cities.internal.repositories.interfaces.IPlanetRepository;
import nl.partytitan.cities.internal.utils.LoggingUtil;
import nl.partytitan.cities.internal.utils.TranslationUtil;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.world.WorldInitEvent;
import org.bukkit.event.world.WorldLoadEvent;

@Singleton
public class WorldLoadListener implements Listener {
    @Inject
    private IPlanetRepository planetRepository;


    @EventHandler(priority = EventPriority.NORMAL)
    public void onWorldLoad(WorldLoadEvent event) {
        String planetName = event.getWorld().getName();

        if(!planetRepository.planetExists(planetName)){
            LoggingUtil.sendMsg(TranslationUtil.of("planet_created", planetName));
            planetRepository.createPlanet(new Planet(planetName));
        }
    }
}
