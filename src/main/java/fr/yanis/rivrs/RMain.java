package fr.yanis.rivrs;

import fr.yanis.rivrs.event.ZoneListener;
import fr.yanis.rivrs.utils.Cuboid;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Map;

public final class RMain extends JavaPlugin {

    @Override
    public void onEnable() {
        ConfigurationSerialization.registerClass(Cuboid.class);
        this.saveDefaultConfig();
        this.loadCuboid();

        this.getServer().getPluginManager().registerEvents(new ZoneListener(), this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public void loadCuboid(){
        Cuboid cuboid = this.getConfig().getSerializable("cuboid", Cuboid.class);
        if(cuboid != null){
            getLogger().info("Zone chargée avec succès !");
        }
    }

    public static RMain getInstance() {
        return getPlugin(RMain.class);
    }
}
