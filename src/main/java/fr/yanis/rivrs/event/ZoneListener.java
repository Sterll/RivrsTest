package fr.yanis.rivrs.event;

import fr.yanis.rivrs.event.custom.PlayerEnterZoneEvent;
import fr.yanis.rivrs.event.custom.PlayerExitZoneEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class ZoneListener implements Listener {

    @EventHandler
    public void onEnter(PlayerEnterZoneEvent e){

    }

    @EventHandler
    public void onExit(PlayerExitZoneEvent e){

    }

}
