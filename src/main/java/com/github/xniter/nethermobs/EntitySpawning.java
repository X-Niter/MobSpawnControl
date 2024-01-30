package com.github.xniter.nethermobs;

import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.util.Vector;

public class EntitySpawning implements Listener {
    EntityType[] netherMobs;

    public EntitySpawning() {
        this.netherMobs = new EntityType[]{
                EntityType.GHAST,
                EntityType.BLAZE,
                EntityType.ZOMBIFIED_PIGLIN,
                EntityType.MAGMA_CUBE,
                EntityType.SKELETON,
                EntityType.WITHER_SKELETON
        };
    }

    @EventHandler
    public void onSpawn(CreatureSpawnEvent event) {
        LivingEntity entity = event.getEntity();
        switch (entity.getType()) {
            case CAVE_SPIDER:
            case ENDERMAN:
            case SILVERFISH:
            case SKELETON:
            case SLIME:
            case SPIDER:
            case WITCH:
            case ZOMBIE:
            case CREEPER:
                EntityType type = this.netherMobs[NetherMobs.getRandom().nextInt(this.netherMobs.length)];
                if (NetherMobs.getRandom().nextInt(ConfigOptions.getSpawnRate(type) / 10) == 0) {
                    if (!ConfigOptions.isEntityEnabled(type)) {
                        return;
                    } else {
                        int[] nums = new int[3];

                        for(int i = 0; i < nums.length; ++i) {
                            nums[i] = NetherMobs.getRandom().nextInt(5);
                            nums[i] = NetherMobs.getRandom().nextInt(2) == 0 ? nums[i] : nums[i] * -1;
                        }

                        Entity e = entity.getWorld().spawnEntity(entity.getLocation().add(new Vector(nums[0], nums[1], nums[2])), type);
                        if (type == EntityType.SKELETON) {
                            Skeleton skeleton = (Skeleton) e;

                            skeleton.remove();

                            skeleton.getWorld().spawnEntity(entity.getLocation().add(new Vector(nums[0], nums[1], nums[2])), EntityType.WITHER_SKELETON);

                            if (!(Boolean) ConfigOptions.REPLACE_ORIG_MOB.getValue()) {
                                event.setCancelled(true);
                            }
                        }

                        if (!(Boolean)ConfigOptions.REPLACE_ORIG_MOB.getValue()) {
                            event.setCancelled(true);
                        }
                    }
                }
            default:
        }
    }
}