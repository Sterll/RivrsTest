package fr.yanis.rivrs.manager;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.UUID;

@Getter
public class ScoreManager {

    private static HashMap<UUID, ScoreManager> scoreMap = new HashMap<>();

    @Setter
    private int score;
    private UUID uuid;

    public static ScoreManager getScoreManager(UUID player) {
        if (scoreMap.containsKey(player)) {
            return scoreMap.get(player);
        } else {
            ScoreManager scoreManager = new ScoreManager();
            scoreMap.put(player, scoreManager);
            return scoreManager;
        }
    }

    public ScoreManager(UUID uuid) {
        this.uuid = uuid;
        this.score = 0;

        scoreMap.put(uuid, this);
    }

    public ScoreManager(UUID uuid, int score) {
        this.uuid = uuid;
        this.score = score;

        scoreMap.put(uuid, this);
    }

    public void addScore(int score) {
        this.score += score;
    }

    public void removeScore(int score) {
        this.score -= score;
    }

}
