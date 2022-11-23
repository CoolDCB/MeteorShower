package me.dave.meteorshower;

import org.bukkit.plugin.java.JavaPlugin;

public final class MeteorShower extends JavaPlugin {
    private static MeteorShower plugin;
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

    public static MeteorShower getInstance() {
        return plugin;
    }
}
