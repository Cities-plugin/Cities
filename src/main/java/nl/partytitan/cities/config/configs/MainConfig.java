package nl.partytitan.cities.config.configs;

import nl.partytitan.cities.config.BaseConfiguration;
import nl.partytitan.cities.internal.enums.Languages;
import nl.partytitan.cities.internal.enums.StorageType;
import nl.partytitan.cities.dependencyinjection.annotations.PostConstruct;
import nl.partytitan.cities.internal.objects.Level;
import nl.partytitan.cities.internal.Constants;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MainConfig extends BaseConfiguration {
    @PostConstruct
    public void initConfig() {
        super.init();
    }

    @Override
    protected String filePath() {
        return Constants.CONFIG_PATH;
    }

    @Override
    protected String templatePath() {
        return Constants.CONFIG_TEMPLATE_PATH;
    }

    public double GetConfigVersion() {return getDouble("version");}

    public Languages getLanguage() {
        final String value = getString("language");
        try {
            return Languages.valueOf(value.toUpperCase());
        } catch (final IllegalArgumentException e) {
            return Languages.ENGLISH;
        }
    }

    public StorageType getStorageType() {
        final String value = getString("plugin.storageType");
        try {
            return StorageType.valueOf(value.toUpperCase());
        } catch (final IllegalArgumentException e) {
            return StorageType.FILE;
        }
    }

    public List<Level> getCityLevels() {
        final List<Level> levels = new ArrayList<>();

        List<Map<?, ?>> levelsMap = getMapList("cityLevels");
        for (Map<?, ?> level : levelsMap) {
            levels.add(Level.newLevel(
                    Integer.parseInt(level.get("minPopulation").toString()),
                    String.valueOf(level.get("titlePreFix")),
                    String.valueOf(level.get("titlePostFix")),
                    String.valueOf(level.get("mayorPreFix")),
                    String.valueOf(level.get("mayorPostFix")),
                    Integer.parseInt(level.get("cityBlockLimit").toString())
            ));
        }
        return levels;
    }
}
