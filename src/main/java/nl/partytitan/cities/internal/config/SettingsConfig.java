package nl.partytitan.cities.internal.config;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import nl.partytitan.cities.internal.config.enums.StorageType;
import nl.partytitan.cities.internal.config.interfaces.ISettings;

import javax.inject.Named;
import java.io.File;

@Singleton
public class SettingsConfig implements ISettings {

    private final transient BaseConfig config;

    private double configVersion = 1.0;
    private StorageType storageType = StorageType.FILE;

    @Inject
    public SettingsConfig(File dataFolder) {
        config = new BaseConfig(new File(dataFolder, "config.yml"));
        config.setTemplateName("/config.yml");
        reloadConfig();
    }

    public double getConfigVersion() { return configVersion; }

    private double _getConfigVersion(){
        return config.getDouble("version");
    }

    public StorageType getStorageType() {
        return storageType;
    }

    private StorageType _getStorageType() {
        final String value = config.getString("storageType");
        try {
            return StorageType.valueOf(value.toUpperCase());
        } catch (final IllegalArgumentException e) {
            return StorageType.FILE;
        }
    }

    private void reloadConfig() {
        config.load();
        configVersion = _getConfigVersion();
        storageType = _getStorageType();
    }
}
