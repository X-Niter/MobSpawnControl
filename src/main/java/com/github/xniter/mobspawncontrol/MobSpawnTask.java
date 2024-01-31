package com.github.xniter.mobspawncontrol;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;
import java.util.Objects;

public class MobSpawnTask extends BukkitRunnable {

    @Override
    public void run() {
        // Your spawning logic goes here
        spawnEntities();
    }

    private void spawnEntities() {
        for (World world : Bukkit.getWorlds()) {
            // Replace this logic with your custom entity spawning logic
            Location spawnLocation = world.getSpawnLocation();

            for (EntityType entityType : EntityType.values()) {
                if (ConfigOptions.isEntityEnabled(entityType)) {
                    // Check other conditions and modify spawning logic as needed.
                    int spawnRate = ConfigOptions.getSpawnRate(entityType);
                    if (spawnRate == 0 || MobSpawnControl.getRandom().nextInt(spawnRate / 10) == 0) {
                        List<String> allowedWorlds = ConfigOptions.getAllowedWorlds(entityType);
                        if (isAllowedWorld(world.getName(), allowedWorlds)) {
                            spawnCustomEntity(spawnLocation, entityType);
                            MobSpawnControl.LOGGER.info("Spawned " + entityType.name() + " at " + spawnLocation);
                        }
                    }
                }
            }
        }
    }

    private boolean isAllowedWorld(String currentWorld, List<String> allowedWorlds) {
        for (String allowedWorld : allowedWorlds) {
            if (allowedWorld.equalsIgnoreCase(currentWorld)) {
                return true;
            }
        }
        return false;
    }

    private void spawnCustomEntity(Location location, EntityType entityType) {
        // Generate random offsets
        double offsetX = MobSpawnControl.getRandom().nextDouble() * 10 - 5; // Adjust the range as needed
        double offsetY = MobSpawnControl.getRandom().nextDouble() * 10 - 5;
        double offsetZ = MobSpawnControl.getRandom().nextDouble() * 10 - 5;

        // Create a new location with random offsets
        Location newLocation = location.clone().add(offsetX, offsetY, offsetZ);

        // Spawn the new entity
        Entity entity = Objects.requireNonNull(location.getWorld()).spawnEntity(newLocation, entityType);

        entity.getWorld().spawnEntity(newLocation, entityType);

        // Log the information
        MobSpawnControl.LOGGER.info("Spawned " + entityType.name() + " at " + newLocation);
    }
}