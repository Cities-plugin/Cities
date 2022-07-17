package nl.partytitan.cities.internal.utils.server;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import nl.partytitan.cities.Cities;
import org.bukkit.plugin.PluginManager;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.scheduler.BukkitTask;

@Singleton
public class SchedulerUtil {

    @Inject
    private static Cities plugin;

    @Inject
    private static BukkitScheduler scheduler;

    @Inject
    private static PluginManager pluginManager;

    /**
     * Accepts a Runnable object and a delay (-1 for no delay)
     *
     * @param task runnable object
     * @param delay ticks to delay starting
     * @return -1 if unable to schedule or an index to the task is successful.
     */
    public static int scheduleSyncDelayedTask(Runnable task, long delay) {
        plugin.getInjector().injectMembers(task);
        return scheduler.scheduleSyncDelayedTask(plugin, task, delay);
    }

    public static BukkitTask runTaskLater(Runnable task, long delay) {
        return scheduler.runTaskLater(plugin, task, delay);
    }

    public static BukkitTask runTaskTimerAsynchronously(Runnable task, long l, long l1){
        return scheduler.runTaskTimerAsynchronously(plugin, task, l, l1);
    }

    public static void cancelTask(int taskId) {
        scheduler.cancelTask(taskId);
    }

    public static BukkitTask runAsyncTask(Runnable task) {
        plugin.getInjector().injectMembers(task);
        return scheduler.runTaskAsynchronously(plugin, task);
    }

    public static BukkitTask runTask(Runnable task) {
        plugin.getInjector().injectMembers(task);
        return scheduler.runTask(plugin, task);
    }
}
