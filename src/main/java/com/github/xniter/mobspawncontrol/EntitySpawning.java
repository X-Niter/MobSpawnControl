package com.github.xniter.mobspawncontrol;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.util.Vector;

import java.util.List;

public class EntitySpawning implements Listener {

    @EventHandler
    public void onSpawn(CreatureSpawnEvent event) {
        LivingEntity entity = event.getEntity();

        for (EntityType entityType : EntityType.values()) {
            if (ConfigOptions.isEntityEnabled(entityType)) {
                if (entity.getType() == entityType) {
                    // You might not want to cancel the event, just return if not using default spawning.
                    event.setCancelled(true);

                    // Check other conditions and modify spawning logic as needed.
                    int spawnRate = ConfigOptions.getSpawnRate(entityType);
                    if (spawnRate == 0 || MobSpawnControl.getRandom().nextInt(spawnRate / 10) == 0) {
                        List<String> allowedWorlds = ConfigOptions.getAllowedWorlds(entityType);
                        if (isAllowedWorld(entity.getWorld().getName(), allowedWorlds)) {
                            // Use BukkitScheduler for asynchronous tasks.
                            Bukkit.getScheduler().runTask(MobSpawnControl.getPlugin(), () -> spawnCustomEntity(entity, entityType, entity.getLocation()));
                        }
                    }
                }
            }
        }
    }

    private void spawnCustomEntity(Entity entity, EntityType entityType, Location location) {
        // Adjust the spawning logic based on the original entity's location.
        // Example: Adjust the Y-coordinate to be slightly above the original entity.
        Location newLocation = location.clone().add(0, 1, 0);

        // Spawn the new entity
        entity.getWorld().spawnEntity(newLocation, entityType);
    }

    private boolean isAllowedWorld(String currentWorld, List<String> allowedWorlds) {
        for (String allowedWorld : allowedWorlds) {
            if (allowedWorld.equalsIgnoreCase(currentWorld)) {
                return true;
            }
        }
        return false;
    }
}