package nl.partytitan.cities.internal.config.interfaces;

import com.google.inject.ImplementedBy;
import nl.partytitan.cities.internal.config.SettingsConfig;
import nl.partytitan.cities.internal.config.enums.StorageType;

@ImplementedBy(SettingsConfig.class)
public interface ISettings {
    double getConfigVersion();
    StorageType getStorageType();
}
