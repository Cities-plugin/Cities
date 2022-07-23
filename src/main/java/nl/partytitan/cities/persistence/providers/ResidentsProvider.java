package nl.partytitan.cities.persistence.providers;

import com.google.gson.Gson;
import com.google.inject.Inject;
import com.google.inject.Provider;
import nl.partytitan.cities.config.configs.MainConfig;
import nl.partytitan.cities.internal.services.SchedulerService;
import nl.partytitan.cities.persistence.flatfile.ResidentFlatFileRepository;
import nl.partytitan.cities.persistence.interfaces.IResidentRepository;

public class ResidentsProvider implements Provider<IResidentRepository> {
    @Inject
    private MainConfig mainConfig;

    @Inject
    private Gson gson;

    @Inject
    private SchedulerService schedulerService;

    @Override
    public IResidentRepository get() {
        return switch (mainConfig.getStorageType()) {
            case FILE -> new ResidentFlatFileRepository(gson, schedulerService);
            default -> new ResidentFlatFileRepository(gson, schedulerService);
        };
    }
}