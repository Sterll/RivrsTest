package fr.yanis.rivrs;

import fr.yanis.rivrs.utils.Cuboid;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Map;

public final class RMain extends JavaPlugin {

    @Override
    public void onEnable() {
        ConfigurationSerialization.registerClass(Cuboid.class);

        this.getConfig().addDefault("redis.host", "localhost");
        this.getConfig().addDefault("redis.port", 6379);
        this.getConfig().addDefault("redis.password", "");
        this.getConfig().addDefault("redis.database", 0);

        this.getConfig().addDefault("mariadb.host", "localhost");
        this.getConfig().addDefault("mariadb.port", 3306);
        this.getConfig().addDefault("mariadb.database", "rivrs");
        this.getConfig().addDefault("mariadb.user", "root");
        this.getConfig().addDefault("mariadb.password", "");

        this.getConfig().addDefault("rabbitmq.host", "localhost");
        this.getConfig().addDefault("rabbitmq.port", 5672);

        this.saveDefaultConfig();
        this.loadCuboid();

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public void loadCuboid(){
        Cuboid cuboid = this.getConfig().getSerializable("cuboid", Cuboid.class);
        if(cuboid != null){
            getLogger().info("Cuboid chargé: " + cuboid.serialize());
        }
    }

    public static RMain getInstance() {
        return getPlugin(RMain.class);
    }
}
