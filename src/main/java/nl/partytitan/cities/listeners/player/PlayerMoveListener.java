package nl.partytitan.cities.listeners.player;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import nl.partytitan.cities.events.PlayerChangeChunkEvent;
import nl.partytitan.cities.events.PlayerChangeCityBlockEvent;
import nl.partytitan.cities.events.PlayerEnterCityEvent;
import nl.partytitan.cities.events.PlayerLeaveCityEvent;
import nl.partytitan.cities.internal.entities.City;
import nl.partytitan.cities.internal.entities.CityBlock;
import nl.partytitan.cities.internal.entities.Planet;
import nl.partytitan.cities.internal.repositories.interfaces.ICityBlockRepository;
import nl.partytitan.cities.internal.repositories.interfaces.IPlanetRepository;
import nl.partytitan.cities.internal.repositories.interfaces.IResidentRepository;
import nl.partytitan.cities.internal.utils.LoggingUtil;
import nl.partytitan.cities.internal.utils.MessageUtil;
import nl.partytitan.cities.internal.utils.TranslationUtil;
import nl.partytitan.cities.internal.valueobjects.Coord;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.plugin.PluginManager;

@Singleton
public class PlayerMoveListener implements Listener {
    @Inject
    private PluginManager pluginManager;

    @Inject
    private IPlanetRepository planetRepository;

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onPlayerMove(PlayerMoveEvent e) {
        Location fromLocation = e.getFrom();
        Location toLocation = e.getTo();

        // Skip if still in same block
        if (fromLocation.getBlock() == toLocation.getBlock()) {
            return;
        }

        Chunk fromChunk = fromLocation.getChunk();
        Chunk toChunk = toLocation.getChunk();

        Planet currentPlanet = planetRepository.getPlanet(toLocation.getWorld().getName());

        // If cities is not active in the world skip
        if(!currentPlanet.isCitiesAllowedInWorld())
            return;

        if(fromChunk != toChunk){
            PlayerChangeChunkEvent changeChunkEvent = new PlayerChangeChunkEvent(e.getPlayer(), fromChunk, toChunk, e);
            pluginManager.callEvent(changeChunkEvent);
        }
    }
}
