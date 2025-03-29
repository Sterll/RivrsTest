package fr.yanis.rivrs.manager;

import fr.yanis.rivrs.RMain;
import fr.yanis.rivrs.event.custom.PlayerEnterZoneEvent;
import fr.yanis.rivrs.event.custom.PlayerExitZoneEvent;
import fr.yanis.rivrs.utils.Cuboid;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.UUID;

@Getter
public class ZoneManager {

    @Getter
    private static ZoneManager INSTANCE = null;

    private static BukkitTask verifyTask;

    @Setter
    private int interval;

    @Setter
    private @NotNull Cuboid cuboid;

    private @NotNull BukkitTask task;

    private @NotNull HashMap<UUID, Integer> score;

    public ZoneManager(@NotNull Cuboid cuboid, int interval){
        this.cuboid = cuboid;
        this.interval = interval;
        this.score = new HashMap<>();

        verifyTask = new BukkitRunnable() {

            @Override
            public void run() {
                cuboid.getCenter().getNearbyEntitiesByType(Player.class, cuboid.getRayon() + 5).forEach(player -> {
                    if (isInZone(player) && !cuboid.contains(player.getLocation())) {
                        Bukkit.getServer().getPluginManager().callEvent(new PlayerExitZoneEvent(player, ZoneManager.getINSTANCE()));
                        return;
                    }

                    if (!isInZone(player) && cuboid.contains(player.getLocation())) {
                        Bukkit.getServer().getPluginManager().callEvent(new PlayerEnterZoneEvent(player, ZoneManager.getINSTANCE()));
                    }
                });
            }

        }.runTaskTimer(RMain.getInstance(), 5L, 5L);

        this.task = new BukkitRunnable(){

            @Override
            public void run() {
                onInterval();
            }

        }.runTaskTimer(RMain.getInstance(), 1L, interval);

        INSTANCE = this;
    }

    private void onInterval() {
        score.keySet().forEach(uuid -> score.put( uuid, score.get(uuid) + 1));
    }

    public boolean isInZone(@NotNull Player player) {
        return score.containsKey(player.getUniqueId());
    }

    public void addPlayer(@NotNull UUID uuid){
        this.score.put(uuid, 0);
    }

    public void removePlayer(@NotNull UUID uuid){
        this.score.remove(uuid);
    }

}
