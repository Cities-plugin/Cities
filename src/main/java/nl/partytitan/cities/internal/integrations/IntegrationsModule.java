package nl.partytitan.cities.internal.integrations;

import com.google.inject.AbstractModule;
import net.milkbowl.vault.economy.Economy;
import nl.partytitan.cities.Cities;
import nl.partytitan.cities.internal.config.SettingsConfig;
import nl.partytitan.cities.internal.integrations.eco.VaultEconomyRepository;
import nl.partytitan.cities.internal.integrations.eco.interfaces.IEconomyRepository;

public class IntegrationsModule extends AbstractModule
{
    private Cities plugin;
    private SettingsConfig settings;

    public IntegrationsModule(Cities plugin, SettingsConfig settings) {
        this.plugin = plugin;
        this.settings = settings;
    }

    @Override
    protected void configure() {
        // Vault
        Economy economy = plugin.getServer().getServicesManager().getRegistration(Economy.class).getProvider();
        this.bind(IEconomyRepository.class).toInstance(new VaultEconomyRepository(economy));
    }
}
