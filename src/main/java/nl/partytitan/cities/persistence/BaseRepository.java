package nl.partytitan.cities.persistence;

import nl.partytitan.cities.internal.services.SchedulerService;
import org.bukkit.scheduler.BukkitTask;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public abstract class BaseRepository {
    private SchedulerService schedulerService;

    protected final Queue<Runnable> queryQueue = new ConcurrentLinkedQueue<>();
    private BukkitTask task;

    public BaseRepository(SchedulerService schedulerService) {
        this.schedulerService = schedulerService;
        task = schedulerService.runTaskTimerAsynchronously(() -> {
            while (!this.queryQueue.isEmpty()) {
                Runnable operation = this.queryQueue.poll();
                operation.run();
            }
        }, 5L, 5L);
    }
}
