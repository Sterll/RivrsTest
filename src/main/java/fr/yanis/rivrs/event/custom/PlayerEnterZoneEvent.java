package fr.yanis.rivrs.event.custom;

import fr.yanis.rivrs.manager.ZoneManager;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

@Getter @AllArgsConstructor
public class PlayerEnterZoneEvent extends Event {

    private static final HandlerList HANDLERS = new HandlerList();

    private final ZoneManager zone;

    @Override
    public @NotNull HandlerList getHandlers() {
        return HANDLERS;
    }
}
