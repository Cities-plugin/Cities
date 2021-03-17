package nl.partytitan.cities.internal.repositories;

import com.google.inject.AbstractModule;
import nl.partytitan.cities.internal.config.interfaces.ISettings;
import nl.partytitan.cities.internal.repositories.interfaces.ICityRepository;
import nl.partytitan.cities.internal.repositories.interfaces.IResidentRepository;

import static org.bukkit.Bukkit.getLogger;

public class DataModule extends AbstractModule
{
    private ISettings settings;

    public DataModule(ISettings settings) {
        this.settings = settings;
    }

    @Override
    protected void configure() {
        // Here we decide what data storage strategy to use
        switch (settings.getStorageType()){
            case FILE: {
                this.bind(ICityRepository.class).to(CityFlatFileRepository.class);
                this.bind(IResidentRepository.class).to(ResidentFlatFileRepository.class);
                break;
            }
            case MYSQL:
                break;
        }
    }
}
