package fr.yanis.rivrs.database.rabbitmq;

import com.rabbitmq.client.*;
import fr.yanis.rivrs.manager.ScoreManager;
import fr.yanis.rivrs.RMain;
import fr.yanis.rivrs.utils.HologramUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

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

                            final Player target =Bukkit.getPlayer(playerUUID);

                            if (target != null) {
                                String holo_message = RMain.getInstance().getConfig().getString("messages.HOLOGRAM_NAME").replace("%count%", String.valueOf( ScoreManager.getScoreManager(target.getUniqueId()).getScore().get()));
                                if (HologramUtil.hasTextDisplay(target))
                                    HologramUtil.updateTextDisplay(target, holo_message);
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

                            final Player target =Bukkit.getPlayer(playerUUID);

                            if (target != null) {
                                String holo_message = RMain.getInstance().getConfig().getString("messages.HOLOGRAM_NAME").replace("%count%", String.valueOf( ScoreManager.getScoreManager(target.getUniqueId()).getScore().get()));
                                if (HologramUtil.hasTextDisplay(target))
                                    HologramUtil.updateTextDisplay(target, holo_message);
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
