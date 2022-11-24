package me.dave.meteoriteshowers;

import me.dave.meteoriteshowers.events.FallingBlockEvents;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public final class MeteoriteShowers extends JavaPlugin {
    private static MeteoriteShowers plugin;
    public static ConfigManager configManager;

    @Override
    public void onEnable() {
        plugin = this;
        configManager = new ConfigManager();

        getCommand("meteoriteshowers").setExecutor(new MeteoriteShowersCmd());

        Listener[] listeners = new Listener[] {
                new FallingBlockEvents()
        };
        registerEvents(listeners);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public static MeteoriteShowers getInstance() {
        return plugin;
    }

    public void registerEvents(Listener[] listeners) {
        for (Listener listener : listeners) {
            getServer().getPluginManager().registerEvents(listener, this);
        }
    }
}
