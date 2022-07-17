package nl.partytitan.cities.internal.utils.injection;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import nl.partytitan.cities.internal.config.SettingsConfig;

@Singleton
public class ConfigUtil {
    @Inject
    private static SettingsConfig settings;

    public static SettingsConfig getSettings() {
        return settings;
    }
}
