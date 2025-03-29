package fr.yanis.rivrs.manager;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Getter
public class ScoreManager {

    private static ConcurrentHashMap<UUID, ScoreManager> scoreMap = new ConcurrentHashMap<>();

    @Setter
    private AtomicInteger score;
    private final UUID uuid;

    public static ScoreManager getScoreManager(UUID player) {

        if (scoreMap.containsKey(player)) {
            return scoreMap.get(player);
        }

        return null;
    }

    public ScoreManager(UUID uuid) {
        this.uuid = uuid;
        this.score = new AtomicInteger(0);

        scoreMap.put(uuid, this);
    }

    public ScoreManager(UUID uuid, int score) {
        this.uuid = uuid;
        this.score = new AtomicInteger(score);

        scoreMap.put(uuid, this);
    }

    public void addScore(int score) {
        this.score.addAndGet(score);
    }

    public void removeScore(int score) {
        this.score.addAndGet(-score);
    }

}
