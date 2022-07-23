package nl.partytitan.cities.persistence.providers;

import com.google.gson.Gson;
import com.google.inject.Inject;
import com.google.inject.Provider;
import nl.partytitan.cities.config.configs.MainConfig;
import nl.partytitan.cities.internal.services.SchedulerService;
import nl.partytitan.cities.persistence.flatfile.CityFlatFileRepository;
import nl.partytitan.cities.persistence.interfaces.ICityRepository;

public class CitiesProvider implements Provider<ICityRepository> {
    @Inject
    private MainConfig mainConfig;

    @Inject
    private Gson gson;

    @Inject
    private SchedulerService schedulerService;

    @Override
    public ICityRepository get() {
        return switch (mainConfig.getStorageType()) {
            case FILE -> new CityFlatFileRepository(gson, schedulerService);
            default -> new CityFlatFileRepository(gson, schedulerService);
        };
    }
}