package me.dave.meteorshower;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.Player;

import java.util.*;

public class MeteorShowerObject {
    private final MeteorShower plugin = MeteorShower.getInstance();
    private final HashSet<UUID> entityList = new HashSet<>();

    public MeteorShowerObject(LocationData locationData) {
        new MeteorShowerObject(locationData, 0, 0);
    }

    public MeteorShowerObject(LocationData locationData, int duration) {
        new MeteorShowerObject(locationData, duration, 0);
    }

    public MeteorShowerObject(LocationData locationData, int duration, int count) {
        // Period in ticks between meteorites spawning
        int period = 10;
        // Number of meteors to send per period
        double countPerPeriod = count / ((double) duration / period);
        if (countPerPeriod < 1) {
            countPerPeriod = 1;
            period = duration / count;
        }

        double finalCountPerPeriod = countPerPeriod;
        LocationType locType = locationData.getLocationType();
        List<Location> locationList = new ArrayList<>();
        Bukkit.getScheduler().runTaskTimer(plugin, () -> {
            for (int i = 0; i < Math.ceil(finalCountPerPeriod); i++) {

            }
        }, 0, period);
    }

    // Summons Meteorites above all online players
    private void summonPlayerMeteorites(int duration, int count) {
        List<UUID> playerList = new ArrayList<>();
        List<World> defaultWorlds = MeteorShower.configManager.getEnabledWorlds();
        Bukkit.getOnlinePlayers().forEach((player) -> { if (defaultWorlds.contains(player.getWorld())) playerList.add(player.getUniqueId()); });

        summonPlayerMeteorites(playerList, duration, count);
    }

    // Summons Meteorites above a specific list of players (Bypasses configured default worlds)
    private void summonPlayerMeteorites(List<UUID> playerList, int duration, int count) {
        if (playerList.size() == 0) return;
        if (count == 0) count = playerList.size();

        // Period in ticks between meteorites spawning
        int period = 10;
        // Number of spawn periods
        double periodCount = (double) duration / period;
        // Number of meteors to send per period
        double countPerPeriod = count / periodCount;
        if (countPerPeriod < 1) {
            countPerPeriod = 1;
            period = (int) periodCount;
        }

        Collections.shuffle(playerList);
        double finalCountPerPeriod = countPerPeriod;
        Bukkit.getScheduler().runTaskTimer(plugin, () -> {
            for (int i = 0; i < Math.ceil(finalCountPerPeriod); i++) {
                Player player = Bukkit.getPlayer(playerList.get((i % playerList.size())));
                Location spawnLoc = player.getLocation().clone();
                spawnLoc.setY(MeteorShower.configManager.getSpawnHeight());
                FallingBlock meteorite = player.getWorld().spawnFallingBlock(spawnLoc, Material.MAGMA_BLOCK.createBlockData());
                //  set persistent data
            }
        }, 0, period);
    }

//    private void summonLocationMeteorites(Location location, int duration, int count) {
//
//    }

//    private void summonRangeMeteorites(Location corner1, Location corner2, int duration, int count) {
//
//    }
}
