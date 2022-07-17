package nl.partytitan.cities.messageformats;

import nl.partytitan.cities.internal.entities.City;
import nl.partytitan.cities.internal.utils.TranslationUtil;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class CityFormatter {
    public static final SimpleDateFormat foundedFormat = new SimpleDateFormat("MMM d yyyy");
    public static String citySpawnFormat = "%s: %s, %s, %s";

    public static String[] getDetails(City city) {
        String foundedDate = foundedFormat.format(city.getFoundedDate());

        List<String> out = new ArrayList<>();
        out.add(GlobalFormatters.formatTitle(city.getFormattedName()));
        out.add(TranslationUtil.of("city_founded", foundedDate));
        out.add(TranslationUtil.of("city_size", city.claimCount(), city.maxClaims()));
        out.add(TranslationUtil.of("city_home", String.format(citySpawnFormat,city.getSpawn().getWorld().getName(), city.getSpawn().getBlockX(), city.getSpawn().getBlockY(), city.getSpawn().getBlockZ())));
        out.add(TranslationUtil.of("city_balance", city.getFormattedBalance()));
        out.add(TranslationUtil.of("city_mayor", city.getMayor().getFormattedName()));
        out.add(TranslationUtil.of("city_residents", city.residentcount(), String.join(", ", city.getResidentNames())));
        return out.toArray(new String[0]);
    }
}
