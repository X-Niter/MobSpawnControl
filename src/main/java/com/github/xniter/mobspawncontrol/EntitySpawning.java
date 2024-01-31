package com.github.xniter.mobspawncontrol;

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
                // Check if the entity type is configured to be enabled
                if (entity.getType() == entityType) {
                    event.setCancelled(true);
                    entity.remove();

                    // Check if the spawn rate allows the entity to spawn
                    int spawnRate = ConfigOptions.getSpawnRate(entityType);
                    if (spawnRate == 0 || MobSpawnControl.getRandom().nextInt(spawnRate / 10) == 0) {
                        // Check if the entity can spawn in the current world
                        List<String> allowedWorlds = ConfigOptions.getAllowedWorlds(entityType);
                        entity.getWorld();
                        if (isAllowedWorld(entity.getWorld().getName(), allowedWorlds)) {
                            // Spawn the new entity
                            int[] nums = new int[3];
                            for (int i = 0; i < nums.length; ++i) {
                                nums[i] = MobSpawnControl.getRandom().nextInt(5);
                                nums[i] = MobSpawnControl.getRandom().nextInt(2) == 0 ? nums[i] : nums[i] * -1;
                            }

                            entity.getWorld().spawnEntity(entity.getLocation().add(new Vector(nums[0], nums[1], nums[2])), entityType);

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
}