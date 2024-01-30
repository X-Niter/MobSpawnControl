package com.github.xniter.nethermobs;

import org.bukkit.entity.EntityType;

public enum ConfigOptions {
    REPLACE_ORIG_MOB("replace-orig-mobs", false),
    GHAST("spawn-ghast", true),
    GHAST_SPAWN_RATE("ghast-spawn-rate", 50),
    BLAZE("spawn-blaze", true),
    BLAZE_SPAWN_RATE("blaze-spawn-rate", 50),
    PIG_ZOMBIE("spawn-pig-zombie", true),
    PIG_ZOMBIE_SPAWN_RATE("pig-zombie-spawn-rate", 50),
    MAGMA_CUBE("spawn-magma-cube", true),
    MAGMA_CUBE_SPAWN_RATE("magma-cube-spawn-rate", 50),
    SKELETON("spawn-wither-skeleton", true),
    SKELETON_SPAWN_RATE("wither-skeleton-spawn-rate", 50);

    private String name;
    private Object defaultValue;
    private Object value;

    private ConfigOptions(String name, Object defaultValue) {
        this.name = name;
        this.defaultValue = defaultValue;
    }

    public static boolean isEntityEnabled(EntityType type) {
        return (Boolean)valueOf(type.name()).getValue();
    }

    public static int getSpawnRate(EntityType type) {
        return (Integer)valueOf(String.format("%s_SPAWN_RATE", type.name())).getValue();
    }

    public String getName() {
        return this.name;
    }

    public Object getDefaultValue() {
        return this.defaultValue;
    }

    public Object getValue() {
        return this.value;
    }

    public void setValue(Object value) {
        this.value = value;
    }
}
