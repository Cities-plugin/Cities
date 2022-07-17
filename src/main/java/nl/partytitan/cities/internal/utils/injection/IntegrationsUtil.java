package nl.partytitan.cities.internal.utils.injection;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import nl.partytitan.cities.internal.integrations.eco.interfaces.IEconomyRepository;

@Singleton
public class IntegrationsUtil {
    @Inject
    private static IEconomyRepository economyRepository;

    public static IEconomyRepository getEconomyRepository() {
        return economyRepository;
    }
}
