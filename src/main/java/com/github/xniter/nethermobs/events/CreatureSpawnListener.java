package com.github.xniter.nethermobs.events;

import com.github.xniter.nethermobs.NetherMobs;
import com.github.xniter.nethermobs.config.EntitiesConfig;
import org.apache.logging.log4j.Level;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;

import java.util.List;
import java.util.Random;


public class CreatureSpawnListener implements Listener {

    @EventHandler
    public void onCreatureSpawn(CreatureSpawnEvent event) {
        EntityType entityType = event.getEntityType();
        Location location = event.getLocation();
        World world = location.getWorld();

        // Check if the CreatureSpawnEvent is in the Nether world
        if (world != null && world.getEnvironment().equals(World.Environment.NETHER)) {
            event.setCancelled(true); // Cancel the original spawn

            // Get a random entity type from the configuration
            EntityType randomEntityType = getRandomEntityType();

            if (randomEntityType != null) {
                // Spawn the new entity at the original location
                NetherMobs.LOGGER.info("Spawning random entity " + randomEntityType.name() + " at " + location);
                try {
                    location.getWorld().spawnEntity(location, randomEntityType, CreatureSpawnEvent.SpawnReason.NATURAL);
                    NetherMobs.LOGGER.info("Spawned " + randomEntityType.name() + " at " + location);
                } catch (IllegalArgumentException e) {
                    NetherMobs.LOGGER.warning("Invalid argument when spawning entity: " + e.getMessage());
                } catch (Exception e) {

                    // I need a FATAL log using my plugins log manager
                    NetherMobs.LOGGER.log(Level.FATAL, "Failed to spawn entity: " + e.getMessage());
                }
            } else {
                NetherMobs.LOGGER.warning("Random entity type is null. Cannot spawn entity.");
            }
        }
    }

    private EntityType getRandomEntityType() {
        List<String> entityTypes = EntitiesConfig.getAllEntityTypes();

        if (!entityTypes.isEmpty()) {
            Random random = NetherMobs.getRandom();
            int randomIndex = random.nextInt(entityTypes.size());

            // Convert the entity type name to uppercase before using EntityType.valueOf
            String entityTypeString = entityTypes.get(randomIndex).toUpperCase();

            try {
                NetherMobs.LOGGER.info("Random entity type returned is: " + entityTypeString);
                return EntityType.valueOf(entityTypeString);
            } catch (IllegalArgumentException e) {
                // Handle the exception, log an error, or return null based on your requirements
                NetherMobs.LOGGER.warning("Invalid entity type: " + entityTypeString);
            }
        }

        return null;
    }
}