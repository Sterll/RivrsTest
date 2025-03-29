package fr.yanis.rivrs.database.mariadb;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseManager {

    private final HikariDataSource dataSource;

    public DatabaseManager(JavaPlugin plugin) {
        // Récupérer les paramètres depuis la config
        String host = plugin.getConfig().getString("mariadb.host");
        int port = plugin.getConfig().getInt("mariadb.port");
        String database = plugin.getConfig().getString("mariadb.database");
        String user = plugin.getConfig().getString("mariadb.user");
        String password = plugin.getConfig().getString("mariadb.password");

        String baseJdbcUrl = "jdbc:mariadb://" + host + ":" + port;

        try {
            Class.forName("fr.yanis.rivrs.shaded.mariadb.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        try (Connection conn = DriverManager.getConnection(baseJdbcUrl, user, password); Statement stmt = conn.createStatement()) {
            stmt.executeUpdate("CREATE DATABASE IF NOT EXISTS " + database);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        String jdbcUrl = baseJdbcUrl + "/" + database;

        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(jdbcUrl);
        config.setUsername(user);
        config.setPassword(password);

        config.setDriverClassName("fr.yanis.rivrs.shaded.mariadb.Driver");

        config.addDataSourceProperty("cachePrepStmts", "true");
        config.addDataSourceProperty("prepStmtCacheSize", "250");
        config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");

        dataSource = new HikariDataSource(config);

        createTables();
    }

    public Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }

    private void createTables() {
        String sql = "CREATE TABLE IF NOT EXISTS player_scores (" +
                "uuid VARCHAR(36) NOT NULL, " +
                "score INT DEFAULT 0, " +
                "PRIMARY KEY (uuid)" +
                ")";
        try (Connection conn = getConnection(); Statement stmt = conn.createStatement()) {
            stmt.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void close() {
        if (dataSource != null) {
            dataSource.close();
        }
    }
}
