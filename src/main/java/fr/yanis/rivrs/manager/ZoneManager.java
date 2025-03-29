package fr.yanis.rivrs.manager;

import fr.yanis.rivrs.RMain;
import fr.yanis.rivrs.utils.Cuboid;
import jdk.jfr.consumer.RecordedMethod;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Getter
public class ZoneManager {

    @Getter
    private static ZoneManager INSTANCE = null;

    @Setter
    private int interval;

    @Setter
    private @NotNull Cuboid zone;

    private @NotNull BukkitTask task;

    private @NotNull Set<UUID> players;

    public ZoneManager(@NotNull Cuboid zone, int interval){
        this.zone = zone;
        this.interval = interval;
        this.players = new HashSet<>();

        this.task = new BukkitRunnable(){

            @Override
            public void run() {
                onInterval();
            }

        }.runTaskTimer(RMain.getInstance(), 1L, interval);

        INSTANCE = this;
    }

    private void onInterval() {
        // TODO: Add Score Here
    }

    public void addPlayer(@NotNull UUID uuid){
        this.players.add(uuid);
    }

    public void removePlayer(@NotNull UUID uuid){
        this.players.remove(uuid);
    }

}
