package nl.partytitan.cities.persistence.providers;

import com.google.gson.Gson;
import com.google.inject.Inject;
import com.google.inject.Provider;
import nl.partytitan.cities.config.configs.MainConfig;
import nl.partytitan.cities.internal.services.SchedulerService;
import nl.partytitan.cities.persistence.flatfile.CityBlockFlatFileRepository;
import nl.partytitan.cities.persistence.interfaces.ICityBlockRepository;

public class CityBlocksProvider implements Provider<ICityBlockRepository> {
    @Inject
    private MainConfig mainConfig;

    @Inject
    private Gson gson;

    @Inject
    private SchedulerService schedulerService;

    @Override
    public ICityBlockRepository get() {
        return switch (mainConfig.getStorageType()) {
            case FILE -> new CityBlockFlatFileRepository(gson, schedulerService);
            default -> new CityBlockFlatFileRepository(gson, schedulerService);
        };
    }
}