package fr.yanis.rivrs.manager;

import fr.yanis.rivrs.utils.Cuboid;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Getter
public class ZoneManager {

    @Getter
    private static ZoneManager INSTANCE = null;

    private int elapsedTick;

    @Setter
    private int interval;

    @Setter
    private @NotNull Cuboid zone;

    private @NotNull Set<UUID> players;

    public ZoneManager(@NotNull Cuboid zone, int interval){
        this.zone = zone;
        this.interval = interval;
        this.players = new HashSet<>();
        this.elapsedTick = 0;

        INSTANCE = this;
    }

    private void onTick(){
        if (elapsedTick % interval == 0){
            // TODO: Add Score Here
        }
        elapsedTick++;
    }

    public void addPlayer(@NotNull UUID uuid){
        this.players.add(uuid);
    }

    public void removePlayer(@NotNull UUID uuid){
        this.players.remove(uuid);
    }

}
