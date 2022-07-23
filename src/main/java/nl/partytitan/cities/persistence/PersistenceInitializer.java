package nl.partytitan.cities.persistence;

import com.google.inject.Inject;
import com.google.inject.Injector;
import nl.partytitan.cities.dependencyinjection.interfaces.IInitializer;
import nl.partytitan.cities.internal.models.Planet;
import nl.partytitan.cities.persistence.interfaces.IPlanetRepository;
import nl.partytitan.cities.internal.services.TranslationService;
import org.bukkit.World;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class PersistenceInitializer implements IInitializer {
    @Inject
    private JavaPlugin plugin;

    @Inject
    private IPlanetRepository planetRepository;

    @Inject
    private TranslationService translationService;

    @Override
    public void init(@NotNull Injector injector) {
        loadUnloadedWorlds();
    }

    private void loadUnloadedWorlds(){
        List<World> worlds = plugin.getServer().getWorlds();

        if(worlds.size() > planetRepository.getPlanets().size()){
            for (World world : worlds) {
                if(!planetRepository.planetExists(world.getName())){
                    String planetName = world.getName();
                    planetRepository.createPlanet(new Planet(planetName));
                    plugin.getLogger().info(translationService.of("planet_created", planetName));
                }
            }
        }
    }
}
