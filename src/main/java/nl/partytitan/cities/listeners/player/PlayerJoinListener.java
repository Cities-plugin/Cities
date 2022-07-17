package nl.partytitan.cities.listeners.player;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import nl.partytitan.cities.Cities;
import nl.partytitan.cities.internal.repositories.interfaces.IResidentRepository;
import nl.partytitan.cities.internal.tasks.player.OnPlayerLoginTask;
import nl.partytitan.cities.internal.utils.LoggingUtil;
import nl.partytitan.cities.internal.utils.server.SchedulerUtil;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

@Singleton
public class PlayerJoinListener implements Listener {

    @Inject
    private Cities plugin;

    @Inject
    private IResidentRepository residentRepository;

    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerJoin(PlayerJoinEvent e) {
        Player player = e.getPlayer();

        if (!player.isOnline()) {
            return;
        }

        // Perform login code in it's own thread
        if (SchedulerUtil.scheduleSyncDelayedTask(new OnPlayerLoginTask(plugin, player, residentRepository), 0L) == -1) {
            LoggingUtil.sendErrorMsg("Could not schedule OnLogin.");
        }
    }
}
