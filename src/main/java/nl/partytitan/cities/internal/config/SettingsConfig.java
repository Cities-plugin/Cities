package nl.partytitan.cities.internal.config;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import nl.partytitan.cities.internal.config.enums.Languages;
import nl.partytitan.cities.internal.config.enums.StorageType;
import nl.partytitan.cities.internal.config.obj.Level;

import javax.inject.Named;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Singleton
public class SettingsConfig {

    private final transient BaseConfig config;

    private double configVersion = 1.0;
    private StorageType storageType = StorageType.FILE;
    private Languages language = Languages.ENGLISH;
    private List<Level> cityLevels;

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

    public Languages getLanguage() {
        return language;
    }

    private Languages _getLanguage() {
        final String value = config.getString("language");
        try {
            return Languages.valueOf(value.toUpperCase());
        } catch (final IllegalArgumentException e) {
            return Languages.ENGLISH;
        }
    }

    public List<Level> getCityLevels() {
        return cityLevels;
    }

    private List<Level> _getCityLevels() {
        final List<Level> levels = new ArrayList<>();

        List<Map<?, ?>> levelsMap = config.getMapList("cityLevels");
        for (Map<?, ?> level : levelsMap) {
            levels.add(Level.newLevel(
                Integer.parseInt(level.get("minPopulation").toString()),
                String.valueOf(level.get("titlePreFix")),
                String.valueOf(level.get("titlePostFix")),
                String.valueOf(level.get("mayorPreFix")),
                String.valueOf(level.get("mayorPostFix"))
            ));
        }
        return levels;
    }

    private void reloadConfig() {
        config.load();
        configVersion = _getConfigVersion();
        storageType = _getStorageType();
        language = _getLanguage();
        cityLevels = _getCityLevels();
    }

}
