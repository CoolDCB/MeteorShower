package me.dave.meteoriteshowers.events;

import me.dave.meteoriteshowers.MeteoriteShowers;
import org.bukkit.*;
import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.persistence.PersistentDataType;

public class FallingBlockEvents implements Listener {
    private final MeteoriteShowers plugin = MeteoriteShowers.getInstance();
    private final NamespacedKey meteoriteKey = new NamespacedKey(plugin, "MeteoriteShowers");

    @EventHandler
    public void onEntityChangeBlock(EntityChangeBlockEvent event) {
        Entity entity = event.getEntity();
        if (!entity.getPersistentDataContainer().has(meteoriteKey, PersistentDataType.INTEGER)) return;
        event.setCancelled(true);
        entity.remove();
        World world = entity.getWorld();
        Location location = entity.getLocation();

        Bukkit.getScheduler().runTaskLater(plugin, () -> {
            world.dropItemNaturally(location, MeteoriteShowers.configManager.getRandomDrops());
//            world.dropItemNaturally(location, new ItemStack(Material.IRON_INGOT, 4));
        }, 1);

        world.playSound(location.clone().add(0.5, 0.5, 0.5), Sound.ENTITY_DRAGON_FIREBALL_EXPLODE, 1f, 1f);
        world.spawnParticle(Particle.EXPLOSION_HUGE, location.clone().add(0.5, 0.5, 0.5), 50);
    }
}
