package fr.yanis.rivrs.database;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.Connection;
import java.sql.SQLException;

public class DatabaseManager {

    private final HikariDataSource dataSource;

    public DatabaseManager(JavaPlugin plugin) {
        HikariConfig config = new HikariConfig();

        String host = plugin.getConfig().getString("mariadb.host");
        int port = plugin.getConfig().getInt("mariadb.port");
        String database = plugin.getConfig().getString("mariadb.database");
        String jdbcUrl = "jdbc:mariadb://" + host + ":" + port + "/" + database;

        config.setJdbcUrl(jdbcUrl);
        config.setUsername(plugin.getConfig().getString("mariadb.user"));
        config.setPassword(plugin.getConfig().getString("mariadb.password"));

        config.addDataSourceProperty("cachePrepStmts", "true");
        config.addDataSourceProperty("prepStmtCacheSize", "250");
        config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");

        dataSource = new HikariDataSource(config);
    }

    public Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }

    public void close() {
        if (dataSource != null) {
            dataSource.close();
        }
    }
}
