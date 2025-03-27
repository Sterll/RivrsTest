package fr.yanis.rivrs;

import org.bukkit.plugin.java.JavaPlugin;

public final class RMain extends JavaPlugin {

    @Override
    public void onEnable() {
        // Plugin startup logic

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public static RMain getInstance() {
        return getPlugin(RMain.class);
    }
}
