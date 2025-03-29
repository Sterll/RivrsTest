package fr.yanis.rivrs.event;

import fr.yanis.rivrs.RMain;
import fr.yanis.rivrs.manager.ScoreManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class ScoreListener implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent e){
        final Player player = e.getPlayer();
        final UUID uuid = player.getUniqueId();
        final RMain plugin = RMain.getInstance();

        try (Connection conn = plugin.getDatabaseManager().getConnection()) {
            PreparedStatement ps = conn.prepareStatement("SELECT score FROM player_scores WHERE uuid = ?");
            ps.setString(1, uuid.toString());
            ResultSet rs = ps.executeQuery();
            int score = 0;

            if (rs.next()) {
                score = rs.getInt("score");
            } else {
                PreparedStatement insert = conn.prepareStatement("INSERT INTO player_scores (uuid, score) VALUES (?, ?)");
                insert.setString(1, uuid.toString());
                insert.setInt(2, 0);
                insert.executeUpdate();
                insert.close();
            }
            rs.close();
            ps.close();

            new ScoreManager(uuid, score);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e){
        CompletableFuture.runAsync(() -> {

            final Player player = e.getPlayer();
            final UUID uuid = player.getUniqueId();
            final RMain plugin = RMain.getInstance();
            final ScoreManager scoreManager = ScoreManager.getScoreManager(uuid);

            if (scoreManager == null) {
                return;
            }

            try (Connection conn = plugin.getDatabaseManager().getConnection()) {
                PreparedStatement ps = conn.prepareStatement("UPDATE player_scores SET score = ? WHERE uuid = ?");

                ps.setInt(1, scoreManager.getScore());
                ps.setString(2, uuid.toString());
                ps.executeUpdate();

                ps.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        });
    }

}
