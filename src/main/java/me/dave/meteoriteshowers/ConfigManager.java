package me.dave.meteoriteshowers;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.ArrayList;
import java.util.List;

public class ConfigManager {
    private final MeteoriteShowers plugin = MeteoriteShowers.getInstance();
    private final List<World> enabledWorlds = new ArrayList<>();
    private double meteoritePlayerChance;
    private int spawnHeight;

    public ConfigManager() {
        plugin.saveDefaultConfig();
        reloadConfig();
    }

    public void reloadConfig() {
        plugin.reloadConfig();
        FileConfiguration config = plugin.getConfig();

        for (String world : config.getStringList("enabled-worlds")) { enabledWorlds.add(Bukkit.getWorld(world)); }
        meteoritePlayerChance = config.getDouble("meteorite-player-chance", 100);
        spawnHeight = config.getInt("spawn-height", 128);
    }

    public List<World> getEnabledWorlds() {
        return enabledWorlds;
    }

    public double getMeteoritePlayerChance() {
        return meteoritePlayerChance;
    }

    public int getSpawnHeight() {
        return spawnHeight;
    }
}
