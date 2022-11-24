package me.dave.meteoriteshowers;

import org.bukkit.plugin.java.JavaPlugin;

public final class MeteoriteShowers extends JavaPlugin {
    private static MeteoriteShowers plugin;
    public static ConfigManager configManager;

    @Override
    public void onEnable() {
        plugin = this;
        configManager = new ConfigManager();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public static MeteoriteShowers getInstance() {
        return plugin;
    }
}
