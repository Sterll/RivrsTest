package fr.yanis.rivrs;

import fr.yanis.rivrs.commands.CommandCount;
import fr.yanis.rivrs.commands.CommandPos;
import fr.yanis.rivrs.commands.CommandZone;
import fr.yanis.rivrs.event.ZoneListener;
import fr.yanis.rivrs.manager.ZoneManager;
import fr.yanis.rivrs.utils.Cuboid;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

public final class RMain extends JavaPlugin {

    @Override
    public void onEnable() {
        ConfigurationSerialization.registerClass(Cuboid.class);
        this.saveDefaultConfig();
        this.loadCuboid();

        this.getServer().getPluginManager().registerEvents(new ZoneListener(), this);

        Objects.requireNonNull(this.getCommand("setpos"), "§cLa command '/setpos' n'existe pas").setExecutor(new CommandPos());
        Objects.requireNonNull(this.getCommand("createzone"), "§cLa command '/createzone' n'existe pas").setExecutor(new CommandZone());
        Objects.requireNonNull(this.getCommand("count"), "§cLa command '/count' n'existe pas").setExecutor(new CommandCount());
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public void loadCuboid(){
        Cuboid cuboid = this.getConfig().getSerializable("cuboid", Cuboid.class);
        if(cuboid != null){
            getLogger().info("Zone chargée avec succès !");

            if(ZoneManager.getINSTANCE() == null){
                new ZoneManager(cuboid, this.getConfig().getInt("game.points_interval"));
            } else {
                ZoneManager.getINSTANCE().setCuboid(cuboid);
            }
        }
    }

    public static RMain getInstance() {
        return getPlugin(RMain.class);
    }
}
