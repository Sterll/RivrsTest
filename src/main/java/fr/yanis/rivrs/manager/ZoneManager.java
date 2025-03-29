package fr.yanis.rivrs.manager;

import fr.yanis.rivrs.RMain;
import fr.yanis.rivrs.utils.Cuboid;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
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

    private @NotNull HashMap<UUID, Integer> score;

    public ZoneManager(@NotNull Cuboid zone, int interval){
        this.zone = zone;
        this.interval = interval;
        this.score = new HashMap<>();

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

    public void addPlayer(@NotNull UUID uuid){
        this.score.put(uuid, 0);
    }

    public void removePlayer(@NotNull UUID uuid){
        this.score.remove(uuid);
    }

}
