package me.dave.meteoriteshowers;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class ConfigManager {
    private final MeteoriteShowers plugin = MeteoriteShowers.getInstance();
    private final List<ItemStack> dropList = new ArrayList<>();
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
        dropList.clear();
        enabledWorlds.clear();

        for (String item : config.getStringList("drop-items")) { dropList.add(new ItemStack(Material.valueOf(item.toUpperCase()))); }
        for (String world : config.getStringList("enabled-worlds")) { enabledWorlds.add(Bukkit.getWorld(world)); }
        meteoritePlayerChance = config.getDouble("meteorite-player-chance", 100);
        spawnHeight = config.getInt("spawn-height", 128);
    }

    // Add an int
    public ItemStack getRandomDrops() {
        return dropList.get(0);
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
