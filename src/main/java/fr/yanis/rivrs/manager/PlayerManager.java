package fr.yanis.rivrs.manager;

import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.UUID;

@Getter @Setter
public class PlayerManager {

    private static HashMap<UUID, PlayerManager> datas = new HashMap<>();

    private UUID uuid;
    private int score;

    public PlayerManager(UUID uuid) {
        this.uuid = uuid;
        this.score = 0;
        datas.put(uuid, this);
    }

    public static PlayerManager get(UUID uuid) {
        return datas.get(uuid);
    }

    public static void remove(UUID uuid) {
        datas.remove(uuid);
    }

    public void addScore(int score) {
        this.score += score;
    }

    public void removeScore(int score) {
        this.score -= score;
    }

}
