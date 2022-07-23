package nl.partytitan.cities.persistence.providers;

import com.google.gson.Gson;
import com.google.inject.Inject;
import com.google.inject.Provider;
import nl.partytitan.cities.config.configs.MainConfig;
import nl.partytitan.cities.internal.services.SchedulerService;
import nl.partytitan.cities.persistence.flatfile.PlanetFlatFileRepository;
import nl.partytitan.cities.persistence.interfaces.IPlanetRepository;

public class PlanetsProvider implements Provider<IPlanetRepository> {
    @Inject
    private MainConfig mainConfig;

    @Inject
    private Gson gson;

    @Inject
    private SchedulerService schedulerService;

    @Override
    public IPlanetRepository get() {
       return switch (mainConfig.getStorageType()) {
            case FILE -> new PlanetFlatFileRepository(gson, schedulerService);
            default -> new PlanetFlatFileRepository(gson, schedulerService);
        };
    }
}