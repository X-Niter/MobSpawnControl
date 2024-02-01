package com.github.xniter.mobspawncontrol.utils;

import com.github.xniter.mobspawncontrol.config.EntitiesConfig;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;

import java.util.ArrayList;
import java.util.List;

public class Utils {

    public void populateEntityMapping() {
        for (EntityType entityType : EntityType.values()) {
            // Check if the entity type corresponds to a living entity
            if (isLivingEntity(entityType)) {
                List<String> worldNames = new ArrayList<>();
                for (World world : Bukkit.getServer().getWorlds()) {
                    worldNames.add(world.getName());
                }
                EntitiesConfig.addEntityType(entityType, false, 50, worldNames);  // Adjust default values as needed
            }
        }
    }

    private boolean isLivingEntity(EntityType entityType) {
        try {
            // Attempt to create a living entity instance
            LivingEntity livingEntity = (LivingEntity) Bukkit.getWorlds().get(0).spawnEntity(Bukkit.getWorlds().get(0).getSpawnLocation(), entityType);
            // Dispose the created entity
            livingEntity.remove();
            // If successful, it's a living entity
            return true;
        } catch (ClassCastException e) {
            // If ClassCastException occurs, it's not a living entity
            return false;
        }
    }
}
