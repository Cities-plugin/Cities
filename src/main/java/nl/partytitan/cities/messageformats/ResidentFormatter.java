package nl.partytitan.cities.messageformats;

import nl.partytitan.cities.internal.entities.Resident;
import nl.partytitan.cities.internal.utils.ServerUtils;
import nl.partytitan.cities.internal.utils.TranslationUtil;
import org.bukkit.entity.Player;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class ResidentFormatter {

    public static final SimpleDateFormat lastOnlineFormat = new SimpleDateFormat("MMMMM dd '@' HH:mm");
    public static final SimpleDateFormat lastOnlineFormatIncludeYear = new SimpleDateFormat("MMMMM dd yyyy");
    public static final SimpleDateFormat registeredFormat = new SimpleDateFormat("MMM d yyyy");

    public static String[] getDetails(Resident resident, Player player) {
        Player residentPlayer = ServerUtils.getPlayer(resident.getUuid());
        boolean isOnline = player.isOnline() && residentPlayer != null && (player.canSee(residentPlayer));
        String registeredTime = registeredFormat.format(resident.getRegistered());
        String lastOnlineTime = lastOnlineThisYear(resident.getLastOnline()) ? lastOnlineFormat.format(resident.getLastOnline()) : lastOnlineFormatIncludeYear.format(resident.getLastOnline());

        List<String> out = new ArrayList<>();

        out.add(GlobalFormatters.formatTitle(resident.getFormattedName() + (isOnline ? " " + TranslationUtil.of("resident_online") : "")));
        out.add(TranslationUtil.of("resident_registered_last_online", registeredTime, lastOnlineTime));
        out.add(TranslationUtil.of("resident_balance", resident.getFormattedBalance()));
        return out.toArray(new String[0]);
    }

    private static boolean lastOnlineThisYear(long lastOnlineTime){
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(lastOnlineTime);
        int currentYear = cal.get(Calendar.YEAR);
        cal.setTimeInMillis(System.currentTimeMillis());
        int lastOnlineYear = cal.get(Calendar.YEAR);

        return currentYear == lastOnlineYear;
    }
}
