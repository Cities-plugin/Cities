package nl.partytitan.cities.events;

import nl.partytitan.cities.internal.utils.server.ServerUtils;
import org.bukkit.Chunk;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerMoveEvent;

public class PlayerChangeChunkEvent extends Event {
    private static final HandlerList handlers = new HandlerList();

    private final Player player;
    private final Chunk from;
    private final Chunk to;
    private final PlayerMoveEvent moveEvent;

    public PlayerChangeChunkEvent(Player player, Chunk from, Chunk to, PlayerMoveEvent moveEvent) {
        super(!ServerUtils.isPrimaryThread());
        this.player = player;
        this.from = from;
        this.to = to;
        this.moveEvent = moveEvent;
    }

    @Override
    public HandlerList getHandlers() {

        return handlers;
    }

    public static HandlerList getHandlerList() {

        return handlers;
    }

    public Player getPlayer() {
        return player;
    }

    public Chunk getFrom() {
        return from;
    }

    public Chunk getTo() {
        return to;
    }

    public PlayerMoveEvent getMoveEvent() {
        return moveEvent;
    }
}
