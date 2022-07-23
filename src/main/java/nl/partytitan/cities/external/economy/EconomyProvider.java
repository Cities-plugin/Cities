package nl.partytitan.cities.external.economy;

import com.google.inject.Inject;
import com.google.inject.Provider;
import net.milkbowl.vault.economy.Economy;
import nl.partytitan.cities.external.economy.interfaces.IEconomyRepository;
import org.bukkit.plugin.java.JavaPlugin;

public class EconomyProvider implements Provider<IEconomyRepository> {
    @Inject
    private JavaPlugin javaPlugin;

    @Override
    public IEconomyRepository get() {
        Economy economy = javaPlugin.getServer().getServicesManager().getRegistration(Economy.class).getProvider();
        return new VaultEconomyRepository(economy);
    }
}
