package nl.partytitan.cities.events;

import nl.partytitan.cities.internal.entities.City;
import nl.partytitan.cities.internal.entities.CityBlock;
import nl.partytitan.cities.internal.utils.ServerUtils;
import nl.partytitan.cities.internal.valueobjects.Coord;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerMoveEvent;

public class PlayerLeaveCityEvent extends Event {
    private static final HandlerList handlers = new HandlerList();

    private final City city;
    private final PlayerMoveEvent pme;
    private final CityBlock from;
    private final Coord to;
    private final Player player;

    public PlayerLeaveCityEvent(City city, PlayerMoveEvent pme, CityBlock from, Coord to, Player player) {
        super(!ServerUtils.isPrimaryThread());
        this.city = city;
        this.pme = pme;
        this.from = from;
        this.to = to;
        this.player = player;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {

        return handlers;
    }

    public City getCity() {
        return city;
    }

    public Player getPlayer() {
        return player;
    }

    public CityBlock getFrom() {
        return from;
    }

    public Coord getTo() {
        return to;
    }

    public PlayerMoveEvent getMoveEvent() {
        return pme;
    }
}
