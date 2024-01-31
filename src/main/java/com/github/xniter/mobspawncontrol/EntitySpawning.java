package com.github.xniter.mobspawncontrol;

import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.util.Vector;

import java.util.List;

public class EntitySpawning implements Listener {

    @EventHandler
    public void onSpawn(CreatureSpawnEvent event) {
        LivingEntity entity = event.getEntity();

        if (ConfigOptions.isEntityEnabled(entity.getType())) {
            event.setCancelled(true);
        }
    }
}