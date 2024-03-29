package nl.partytitan.cities.listeners.cities;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import nl.partytitan.cities.events.PlayerEnterCityEvent;
import nl.partytitan.cities.events.PlayerLeaveCityEvent;
import nl.partytitan.cities.internal.repositories.interfaces.IResidentRepository;
import nl.partytitan.cities.internal.utils.MessageUtil;
import nl.partytitan.cities.internal.utils.TranslationUtil;
import nl.partytitan.cities.messageformats.NotificationFormatter;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

@Singleton
public class PlayerLeaveCityListener implements Listener {
    @Inject
    private IResidentRepository residentRepository;

    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerLeaveCity(PlayerLeaveCityEvent e) {
        MessageUtil.sendActionBarMessage(e.getPlayer(), NotificationFormatter.leaveCityNotification(e.getFrom()));
    }
}
