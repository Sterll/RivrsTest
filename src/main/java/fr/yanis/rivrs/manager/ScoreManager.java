package fr.yanis.rivrs.manager;

import lombok.Getter;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.UUID;

@Getter
public class ScoreManager {

    private static HashMap<UUID, ScoreManager> scoreMap = new HashMap<>();

    private int score;

    public static ScoreManager getScoreManager(UUID player) {
        if (scoreMap.containsKey(player)) {
            return scoreMap.get(player);
        } else {
            ScoreManager scoreManager = new ScoreManager();
            scoreMap.put(player, scoreManager);
            return scoreManager;
        }
    }

    public ScoreManager() {
        this.score = 0;
    }

    public ScoreManager(int score) {
        this.score = score;
    }

    public void addScore(int score) {
        this.score += score;
    }

    public void removeScore(int score) {
        this.score -= score;
    }

}
