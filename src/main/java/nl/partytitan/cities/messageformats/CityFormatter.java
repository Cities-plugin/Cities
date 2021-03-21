package nl.partytitan.cities.messageformats;

import nl.partytitan.cities.internal.entities.City;
import nl.partytitan.cities.internal.utils.TranslationUtil;

import java.util.ArrayList;
import java.util.List;

public class CityFormatter {
    public static String[] getDetails(City city) {
        List<String> out = new ArrayList<>();

        out.add(GlobalFormatters.formatTitle(city.getFormattedName()));
        out.add(TranslationUtil.of("city_balance", city.getFormattedBalance()));
        return out.toArray(new String[0]);
    }
}
