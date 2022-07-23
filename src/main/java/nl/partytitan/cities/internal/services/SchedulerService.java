package nl.partytitan.cities.internal.services;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.scheduler.BukkitTask;

@Singleton
public class SchedulerService {

    @Inject
    private JavaPlugin javaPlugin;

    @Inject
    private BukkitScheduler scheduler;

    /**
     * Accepts a Runnable object and a delay (-1 for no delay)
     *
     * @param task runnable object
     * @param delay ticks to delay starting
     * @return -1 if unable to schedule or an index to the task is successful.
     */
    public int scheduleSyncDelayedTask(Runnable task, long delay) {
        return scheduler.scheduleSyncDelayedTask(javaPlugin, task, delay);
    }

    public BukkitTask runTaskLater(Runnable task, long delay) {
        return scheduler.runTaskLater(javaPlugin, task, delay);
    }

    public BukkitTask runTaskTimerAsynchronously(Runnable task, long l, long l1){
        return scheduler.runTaskTimerAsynchronously(javaPlugin, task, l, l1);
    }

    public void cancelTask(int taskId) {
        scheduler.cancelTask(taskId);
    }

    public BukkitTask runAsyncTask(Runnable task) {
        return scheduler.runTaskAsynchronously(javaPlugin, task);
    }

    public BukkitTask runTask(Runnable task) {
        return scheduler.runTask(javaPlugin, task);
    }
}
