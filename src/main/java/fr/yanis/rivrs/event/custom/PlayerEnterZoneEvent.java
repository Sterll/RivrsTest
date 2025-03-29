package fr.yanis.rivrs.event.custom;

import fr.yanis.rivrs.manager.ZoneManager;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

@Getter @AllArgsConstructor
public class PlayerEnterZoneEvent extends Event {

    private static final HandlerList HANDLERS = new HandlerList();

    private final Player player;
    private final ZoneManager zone;

    /**
     * Retrieves the list of handlers for this event.
     *
     * @return the list of handlers
     */
    public @NotNull HandlerList getHandlers() {
        return HANDLERS;
    }

    /**
     * Retrieves the list of handlers for this event.
     *
     * @return the list of handlers
     */
    public static HandlerList getHandlerList() {
        return HANDLERS;
    }
}
