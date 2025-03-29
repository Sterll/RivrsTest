package fr.yanis.rivrs.event;

import fr.yanis.rivrs.RMain;
import fr.yanis.rivrs.event.custom.PlayerEnterZoneEvent;
import fr.yanis.rivrs.event.custom.PlayerExitZoneEvent;
import fr.yanis.rivrs.manager.ScoreManager;
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
        loc.setY(player.getLocation().getY());

        String message = RMain.getInstance().getConfig().getString("messages.HOLOGRAM_NAME").replace("%count%", String.valueOf( ScoreManager.getScoreManager(player.getUniqueId()).getScore().get()));

        HologramUtil.spawnTextDisplay(player, loc, message);

    }

    @EventHandler
    public void onExit(PlayerExitZoneEvent e){
        final Player player = e.getPlayer();

        e.getZone().removePlayer(player.getUniqueId());
        HologramUtil.removeTextDisplay(player);
    }
}
