package nl.partytitan.cities.messageformats;

import nl.partytitan.cities.internal.entities.City;
import nl.partytitan.cities.internal.entities.CityBlock;
import nl.partytitan.cities.internal.entities.Planet;
import nl.partytitan.cities.internal.utils.TranslationUtil;

import java.util.ArrayList;
import java.util.List;

public class NotificationFormatter {
    public static String notificationOpener = GlobalFormatters.PrimaryColor + "";
    public static String notificationSplitter = GlobalFormatters.SecondaryColor + " - ";
    public static String notificationCityNameFormat = GlobalFormatters.AccentColor + "%s";
    public static String notificationOwnerFormat = GlobalFormatters.TitleColor + "%s";

    public static String enterCityNotification(CityBlock to) {
        List<String> out = new ArrayList<String>();
        City city = to.getCity();
        out.add(String.format(notificationCityNameFormat, city.getFormattedName()));
        if (to.hasResident()) {
            out.add(String.format(notificationOwnerFormat, to.getResident().getFormattedName()));
        }
        if(city.getHomeBlockId().equals(to.getIdentifier())){
            out.add(TranslationUtil.of("city_home_notification"));
        }

        return notificationOpener + String.join(notificationSplitter, out);
    }

    public static String interCityNotifications(CityBlock from, CityBlock to){
        List<String> out = new ArrayList<String>();
        City city = to.getCity();
        if (to.hasResident() && from.hasResident()) {
            if(to.getResidentId() == from.getResidentId()){
                out.add(String.format(notificationOwnerFormat, to.getResident().getFormattedName()));
            }
        }
        if(city.getHomeBlockId().equals(to.getIdentifier())){
            out.add(TranslationUtil.of("city_home_notification"));
        }

        return notificationOpener + String.join(notificationSplitter, out);
    }

    public static String leaveCityNotification(CityBlock from) {
        List<String> out = new ArrayList<String>();

        Planet planet = from.getPlanet();
        out.add(planet.getUnclaimedZoneName());
        out.add(planet.isPVPEnabled() ? TranslationUtil.of("planet_pvp") : TranslationUtil.of("planet_nopvp"));
        return notificationOpener + String.join(notificationSplitter, out);
    }
}
