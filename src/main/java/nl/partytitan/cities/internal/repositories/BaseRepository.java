package nl.partytitan.cities.internal.repositories;

import com.google.inject.Inject;
import nl.partytitan.cities.Cities;
import nl.partytitan.cities.internal.utils.server.SchedulerUtil;
import org.bukkit.scheduler.BukkitTask;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public abstract class BaseRepository {
    protected final Queue<Runnable> queryQueue = new ConcurrentLinkedQueue<>();
    private final BukkitTask task;
    private Cities plugin;

    @Inject
    public BaseRepository(Cities plugin) {
        task = SchedulerUtil.runTaskTimerAsynchronously(() -> {
            while (!this.queryQueue.isEmpty()) {
                Runnable operation = this.queryQueue.poll();
                operation.run();
            }
        }, 5L, 5L);

        this.plugin = plugin;
    }

    public Cities getPlugin() {
        return plugin;
    }
}
