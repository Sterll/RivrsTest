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
