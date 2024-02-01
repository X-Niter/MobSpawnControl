package com.github.xniter.mobspawncontrol.events;

import com.github.xniter.mobspawncontrol.MobSpawnControl;
import com.github.xniter.mobspawncontrol.config.EntitiesConfig;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;

import java.util.List;
import java.util.Objects;

public class CreatureSpawnListener implements Listener {

    @EventHandler
    public void onCreatureSpawn(CreatureSpawnEvent event) {
        EntityType entityType = event.getEntityType();
        World world = event.getLocation().getWorld();
        MobSpawnControl.LOGGER.info("CreatureSpawnEvent fired and spawned [" + entityType.name() + "]");

        MobSpawnControl.LOGGER.info("Entity enabled? [" + EntitiesConfig.isEntityEnabled(entityType) + "]");

        // Check if the entity is enabled in the configuration
        if (EntitiesConfig.isEntityEnabled(entityType)) {

            MobSpawnControl.LOGGER.info("Config for entity " + entityType.name()
                    + " is set to be Enabled and controlled by MobSpawnControl");

            if (EntitiesConfig.getSpawnRate(entityType) == 0) {
                MobSpawnControl.LOGGER.info("Spawning of [" + entityType.name() + " has been set to 0(zero) so we are removing the entity.");

                event.setCancelled(true);
                event.getEntity().remove();
                MobSpawnControl.LOGGER.info("Entity [" + entityType.name() + " has been removed.");
            }


            // Check other conditions and modify spawning logic as needed
            int spawnRate = EntitiesConfig.getSpawnRate(entityType);

            assert world != null;
            if (isAllowedWorld(world.toString(), Objects.requireNonNull(EntitiesConfig.getAllowedWorlds(entityType)))) {
                MobSpawnControl.LOGGER.info("Entity [" + entityType.name() + "] is in allowed world to spawn.");

                if (MobSpawnControl.getRandom().nextInt(spawnRate) == 0) {
                    spawnCustomEntity(event.getLocation(), entityType);
                } else {
                    // Cancel the original spawn event
                    MobSpawnControl.LOGGER.info("Cancelled Spawn event for " + entityType.name());
                    event.setCancelled(true);
                    event.getEntity().remove();
                }
            }
        }
    }

    private boolean isAllowedWorld(String currentWorld, List<String> allowedWorlds) {
        return allowedWorlds.isEmpty() || allowedWorlds.contains(currentWorld);
    }

    private void spawnCustomEntity(Location location, EntityType entityType) {
        int spawnRate = EntitiesConfig.getSpawnRate(entityType);

        // Generate random offsets
        double offsetX = MobSpawnControl.getRandom().nextDouble() * 10 - 5; // Adjust the range as needed
        double offsetY = MobSpawnControl.getRandom().nextDouble() * 10 - 5;
        double offsetZ = MobSpawnControl.getRandom().nextDouble() * 10 - 5;

        // Create a new location with random offsets
        Location newLocation = location.clone().add(offsetX, offsetY, offsetZ);

        // Adjust the spawning logic based on the spawn rate
        int multiplier = 1 + spawnRate / 10; // Adjust the divisor as needed
        for (int i = 0; i < multiplier; i++) {
            // Spawn the new entity
            Objects.requireNonNull(location.getWorld()).spawnEntity(newLocation, entityType);

            // Log the information
            MobSpawnControl.LOGGER.info("Spawned " + entityType.name() + " at " + newLocation);
        }
    }
}