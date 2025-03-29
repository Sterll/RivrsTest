package fr.yanis.rivrs.database.rabbitmq;

import com.rabbitmq.client.*;
import fr.yanis.rivrs.manager.ScoreManager;
import fr.yanis.rivrs.RMain;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicInteger;

public class ScoreMessageConsumer extends DefaultConsumer {

    public ScoreMessageConsumer(Channel channel) {
        super(channel);
    }

    @Override
    public void handleDelivery(String consumerTag, Envelope envelope,
                               AMQP.BasicProperties properties, byte[] body) {

        CompletableFuture.runAsync(() -> {

            String message = new String(body, StandardCharsets.UTF_8);

            String[] parts = message.split(":");
            if (parts.length == 3) {
                switch (parts[0]) {
                    case "create":
                        try {
                            UUID playerUUID = UUID.fromString(parts[1]);
                            int newScore = Integer.parseInt(parts[2]);

                            ScoreManager scoreManager = ScoreManager.getScoreManager(playerUUID);

                            if (scoreManager != null) {
                                scoreManager.setScore(new AtomicInteger(newScore));
                            } else {
                                scoreManager = new ScoreManager(playerUUID, newScore);
                            }
                            RMain.getInstance().getLogger().info("Score mis à jour pour " + playerUUID + " : " + newScore);
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                        break;
                    case "add":
                        try {
                            UUID playerUUID = UUID.fromString(parts[1]);
                            int newScore = Integer.parseInt(parts[2]);

                            ScoreManager scoreManager = ScoreManager.getScoreManager(playerUUID);

                            if (scoreManager != null) {
                                scoreManager.addScore(newScore);
                            } else {
                                scoreManager = new ScoreManager(playerUUID, newScore);
                            }

                            RMain.getInstance().getLogger().info("Score mis à jour pour " + playerUUID + " : " + newScore);
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                        break;
                }
            }

        });
    }
}
