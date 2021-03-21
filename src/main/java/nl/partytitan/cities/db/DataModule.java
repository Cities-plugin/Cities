package nl.partytitan.cities.db;

import com.google.inject.AbstractModule;
import net.milkbowl.vault.economy.Economy;
import nl.partytitan.cities.Cities;
import nl.partytitan.cities.db.flatfile.CityBlockFlatFileRepository;
import nl.partytitan.cities.db.flatfile.CityFlatFileRepository;
import nl.partytitan.cities.db.flatfile.PlanetFlatFileRepository;
import nl.partytitan.cities.db.flatfile.ResidentFlatFileRepository;
import nl.partytitan.cities.internal.config.SettingsConfig;
import nl.partytitan.cities.internal.integrations.eco.VaultEconomyRepository;
import nl.partytitan.cities.internal.integrations.eco.interfaces.IEconomyRepository;
import nl.partytitan.cities.internal.repositories.interfaces.ICityBlockRepository;
import nl.partytitan.cities.internal.repositories.interfaces.ICityRepository;
import nl.partytitan.cities.internal.repositories.interfaces.IPlanetRepository;
import nl.partytitan.cities.internal.repositories.interfaces.IResidentRepository;

public class DataModule extends AbstractModule
{
    private Cities plugin;
    private SettingsConfig settings;

    public DataModule(Cities plugin, SettingsConfig settings) {
        this.plugin = plugin;
        this.settings = settings;
    }

    @Override
    protected void configure() {
        // Here we decide what data storage strategy to use
        switch (settings.getStorageType()){
            case FILE: {
                this.bind(ICityRepository.class).to(CityFlatFileRepository.class);
                this.bind(IResidentRepository.class).to(ResidentFlatFileRepository.class);
                this.bind(IPlanetRepository.class).to(PlanetFlatFileRepository.class);
                this.bind(ICityBlockRepository.class).to(CityBlockFlatFileRepository.class);
                break;
            }
            case MYSQL:
                break;
        }
    }
}
