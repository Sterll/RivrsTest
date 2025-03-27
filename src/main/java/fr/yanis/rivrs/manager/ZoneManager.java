package fr.yanis.rivrs.manager;

import fr.yanis.rivrs.utils.Cuboid;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;

import java.util.Set;
import java.util.UUID;

@Getter @AllArgsConstructor
public class ZoneManager {

    @Setter
    private @NotNull Cuboid zone;

    private @NotNull Set<UUID> players;

}
