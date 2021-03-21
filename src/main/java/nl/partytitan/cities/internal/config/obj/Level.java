package nl.partytitan.cities.internal.config.obj;

import org.bukkit.configuration.serialization.ConfigurationSerializable;

import java.lang.reflect.Field;
import java.util.Map;

public class Level {
    private int minPopulation;
    private String titlePreFix;
    private String titlePostFix;
    private String mayorPreFix;
    private String mayorPostFix;

    public int getMinPopulation() {
        return minPopulation;
    }

    public String getTitlePreFix() {
        return titlePreFix;
    }

    public boolean hasTitlePreFix() {
        return titlePreFix != null && !titlePreFix.isEmpty();
    }

    public String getTitlePostFix() {
        return titlePostFix;
    }

    public boolean hasTitlePostFix() {
        return titlePostFix != null && !titlePostFix.isEmpty();
    }

    public String getMayorPreFix() {
        return mayorPreFix;
    }

    public boolean hasMayorPreFix() {
        return mayorPreFix != null && !mayorPreFix.isEmpty();
    }

    public String getMayorPostFix() {
        return mayorPostFix;
    }

    public boolean hasMayorPostFix() {
        return mayorPostFix != null && !mayorPostFix.isEmpty();
    }

    public static Level newLevel(int minPopulation, String titlePreFix, String titlePostFix, String mayorPreFix, String mayorPostFix) {
        Level newLevel = new Level();

        newLevel.minPopulation = minPopulation;
        newLevel.titlePreFix = titlePreFix;
        newLevel.titlePostFix = titlePostFix;
        newLevel.mayorPreFix = mayorPreFix;
        newLevel.mayorPostFix = mayorPostFix;

        return newLevel;
    }
}
