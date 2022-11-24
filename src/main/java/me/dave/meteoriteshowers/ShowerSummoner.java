package me.dave.meteoriteshowers;

import org.bukkit.*;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

public class ShowerSummoner {
    private final MeteoriteShowers plugin = MeteoriteShowers.getInstance();
    private final NamespacedKey meteoriteKey = new NamespacedKey(plugin, "MeteoriteShowers");

    // Summons Meteorites above all online players
    public void summonPlayerMeteorites(int duration, int count) {
        List<UUID> playerList = new ArrayList<>();
        List<World> defaultWorlds = MeteoriteShowers.configManager.getEnabledWorlds();
        Bukkit.getOnlinePlayers().forEach((player) -> { if (defaultWorlds.contains(player.getWorld())) playerList.add(player.getUniqueId()); });

        summonPlayerMeteorites(playerList, duration, count);
    }

    // Summons Meteorites above a specific list of players (Bypasses configured default worlds)
    public void summonPlayerMeteorites(List<UUID> playerList, int duration, int count) {
        if (playerList.size() == 0) return;
        if (count == 0) count = (int) Math.ceil(playerList.size() * (MeteoriteShowers.configManager.getMeteoritePlayerChance() / 100));
        Collections.shuffle(playerList);

        List<Location> locationList = new ArrayList<>();
        for (UUID uuid : playerList) {
            Player player = Bukkit.getPlayer(uuid);
            if (player == null) continue;
            locationList.add(player.getLocation());
        }

        summonShower(locationList, duration, count);
    }

    private void summonLocationMeteorites(Location location, int duration, int count) {
        List<Location> locationList = new ArrayList<>();
        locationList.add(location);

        summonShower(locationList, duration, count);
    }

    private void summonRangeMeteorites(Location corner1, Location corner2, int duration, int count) {
        List<Location> locationList = new ArrayList<>();

        for (int i = 0; i < count; i++) {
            int randX = (int) ((Math.random() * (corner2.getX() - corner1.getX())) + corner1.getX());
            int randY = (int) ((Math.random() * (corner2.getY() - corner1.getY())) + corner1.getY());
            int randZ = (int) ((Math.random() * (corner2.getZ() - corner1.getZ())) + corner1.getZ());
            locationList.add(new Location(corner1.getWorld(), randX, randY, randZ));
        }

        summonShower(locationList, duration, count);
    }

    private void summonShower(List<Location> locationList, int duration, int count) {
        // Period in ticks between meteorites spawning
        int period = 10;
        // Number of spawn periods
        double periodCount = (double) duration / period;
        if (periodCount < 1) periodCount = 1;

        // Number of meteors to send per period
        double countPerPeriod = count / periodCount;
        if (countPerPeriod < 1) {
            countPerPeriod = 1;
            period = (int) periodCount;
        }

        final int[] durationRemaining = {duration};
        int finalPeriod = period;
        double finalCountPerPeriod = countPerPeriod;

        new BukkitRunnable() {
            @Override
            public void run() {
                if (durationRemaining[0] < 0) {
                    cancel();
                    return;
                }
                durationRemaining[0] -= finalPeriod;
                for (int i = 0; i < Math.ceil(finalCountPerPeriod); i++) {
                    Location spawnLoc = locationList.get((i % locationList.size()));
                    spawnLoc.setY(MeteoriteShowers.configManager.getSpawnHeight());
                    World world = spawnLoc.getWorld();
                    if (world == null) continue;
                    FallingBlock meteorite = world.spawnFallingBlock(spawnLoc, Material.MAGMA_BLOCK.createBlockData());
                    meteorite.setDropItem(false);
                    meteorite.getPersistentDataContainer().set(meteoriteKey, PersistentDataType.INTEGER, 0);
                }
            }
        }.runTaskTimer(plugin, 0, period);
    }
}
