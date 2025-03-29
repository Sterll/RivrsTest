package fr.yanis.rivrs.event;

import fr.yanis.rivrs.RMain;
import fr.yanis.rivrs.event.custom.PlayerEnterZoneEvent;
import fr.yanis.rivrs.event.custom.PlayerExitZoneEvent;
import fr.yanis.rivrs.utils.HologramUtil;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class ZoneListener implements Listener {

    @EventHandler
    public void onEnter(PlayerEnterZoneEvent e){

        final Player player = e.getPlayer();

        e.getZone().addPlayer(player.getUniqueId());

        final Location loc = e.getZone().getCuboid().getCenter();
        loc.setY(player.getY());

        HologramUtil.spawnTextDisplay(player, loc, RMain.getInstance().getConfig().getString("messages.HOLOGRAM_NAME"));

    }

    @EventHandler
    public void onExit(PlayerExitZoneEvent e){
        final Player player = e.getPlayer();

        e.getZone().removePlayer(player.getUniqueId());
        HologramUtil.removeTextDisplay(player);
    }
}
