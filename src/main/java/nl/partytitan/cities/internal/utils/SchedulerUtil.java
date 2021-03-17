package nl.partytitan.cities.internal.utils;

import com.google.inject.Inject;
import nl.partytitan.cities.Cities;
import org.bukkit.scheduler.BukkitScheduler;

public class SchedulerUtil {

    @Inject
    private static Cities plugin;

    public static BukkitScheduler getScheduler() {
        return plugin.getServer().getScheduler();
    }

    public static void scheduleTask(){

    }
}
