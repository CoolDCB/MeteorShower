package me.dave.meteoriteshowers;

import org.bukkit.*;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataType;

import java.util.*;

public class ShowerSummoner {
    private final MeteoriteShowers plugin = MeteoriteShowers.getInstance();
    private final NamespacedKey meteoriteKey = new NamespacedKey(plugin, "MeteoriteShowers");

    // Summons Meteorites above all online players
    private void summonPlayerMeteorites(int duration, int count) {
        List<UUID> playerList = new ArrayList<>();
        List<World> defaultWorlds = MeteoriteShowers.configManager.getEnabledWorlds();
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
                spawnLoc.setY(MeteoriteShowers.configManager.getSpawnHeight());
                FallingBlock meteorite = player.getWorld().spawnFallingBlock(spawnLoc, Material.MAGMA_BLOCK.createBlockData());
                meteorite.getPersistentDataContainer().set(meteoriteKey, PersistentDataType.INTEGER, 0);
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
