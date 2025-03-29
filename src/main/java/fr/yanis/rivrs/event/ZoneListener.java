package fr.yanis.rivrs.event;

import fr.yanis.rivrs.event.custom.PlayerEnterZoneEvent;
import fr.yanis.rivrs.event.custom.PlayerExitZoneEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class ZoneListener implements Listener {

    @EventHandler
    public void onEnter(PlayerEnterZoneEvent e){
        e.getZone().addPlayer(e.getPlayer().getUniqueId());
    }

    @EventHandler
    public void onExit(PlayerExitZoneEvent e){
        e.getZone().removePlayer(e.getPlayer().getUniqueId());
    }
}
