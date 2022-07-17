package nl.partytitan.cities.listeners.cities;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import nl.partytitan.cities.events.PlayerChangeCityBlockEvent;
import nl.partytitan.cities.events.PlayerLeaveCityEvent;
import nl.partytitan.cities.internal.repositories.interfaces.IResidentRepository;
import nl.partytitan.cities.internal.utils.MessageUtil;
import nl.partytitan.cities.internal.utils.TranslationUtil;
import nl.partytitan.cities.messageformats.NotificationFormatter;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

@Singleton
public class PlayerChangeCityBlockListener implements Listener {
    @Inject
    private IResidentRepository residentRepository;

    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerChangeCityBlock(PlayerChangeCityBlockEvent e) {
        MessageUtil.sendActionBarMessage(e.getPlayer(), NotificationFormatter.interCityNotifications(e.getFrom(), e.getTo()));
    }
}
