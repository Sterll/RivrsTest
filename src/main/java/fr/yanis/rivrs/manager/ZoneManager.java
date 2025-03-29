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
import org.bukkit.scoreboard.Score;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
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

    private @NotNull Set<UUID> players;

    public ZoneManager(@NotNull Cuboid cuboid, int interval){
        this.cuboid = cuboid;
        this.interval = interval;
        this.players = new HashSet<>();

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
        players.forEach(player -> {

            ScoreManager scoreManager = ScoreManager.getScoreManager(player);

            if(scoreManager != null) {
                scoreManager.addScore(1);
                RMain.getInstance().getRabbitMQManager().publishMessage("add:" + player + ":" + 1);
            }
            else {
                new ScoreManager(player, 1);
            }
        });
    }

    public boolean isInZone(@NotNull Player player) {
        return players.contains(player.getUniqueId());
    }

    public void addPlayer(@NotNull UUID uuid){
        this.players.add(uuid);
    }

    public void removePlayer(@NotNull UUID uuid){
        this.players.remove(uuid);
    }

    public void saveCuboid() {
        RMain.getInstance().getConfig().set("cuboid", this.cuboid);
        RMain.getInstance().saveConfig();
    }

}
