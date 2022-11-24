package me.dave.meteoriteshowers;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;

public class LocationData {
    private final LocationType locationType;
    private final HashSet<UUID> playerList;
    private final List<World> worlds;
    private final Location corner1;
    private final Location corner2;

    /**
     * Creates a LocationData Object containing all online players
     */
    public LocationData() {
        this.locationType = LocationType.PLAYERS;
        HashSet<UUID> playerList = new HashSet<>();
        Bukkit.getOnlinePlayers().forEach((player) -> playerList.add(player.getUniqueId()));
        this.playerList = playerList;
        this.worlds = MeteoriteShowers.configManager.getEnabledWorlds();
        this.corner1 = null;
        this.corner2 = null;
    }

    /**
     * Creates a LocationData Object containing a specific list of players
     */
    public LocationData(HashSet<UUID> playerList) {
        this.locationType = LocationType.PLAYERS;
        this.playerList = playerList;
        this.worlds = MeteoriteShowers.configManager.getEnabledWorlds();
        this.corner1 = null;
        this.corner2 = null;
    }

    /**
     * Creates a LocationData Object containing a specific location
     */
    public LocationData(Location location) {
        this.locationType = LocationType.RANGE;
        this.playerList = null;
        List<World> worlds = new ArrayList<>();
        worlds.add(location.getWorld());
        this.worlds = worlds;
        this.corner1 = location;
        this.corner2 = location;
    }

    /**
     * Creates a LocationData Object containing a location range from {@code corner1} to {@code corner2}
     */
    public LocationData(Location corner1, Location corner2) {
        this.locationType = LocationType.RANGE;
        this.playerList = null;
        List<World> worlds = new ArrayList<>();
        worlds.add(corner1.getWorld());
        this.worlds = worlds;
        this.corner1 = corner1;
        this.corner2 = corner2;
    }

    public LocationType getLocationType() {
        return locationType;
    }

    public HashSet<UUID> getPlayerList() {
        return playerList;
    }

    public Location getCorner1() {
        return corner1;
    }

    public Location getCorner2() {
        return corner2;
    }
}
