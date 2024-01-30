package com.github.xniter.nethermobs;

import org.bukkit.entity.EntityType;

public enum ConfigOptions {
    REPLACE_ORIG_MOB("replace-orig-mobs", false, 50),
    GHAST("spawn-ghast", true, 50),
    GHAST_SPAWN_RATE("ghast-spawn-rate", true, 50),
    BLAZE("spawn-blaze", true, 50),
    BLAZE_SPAWN_RATE("blaze-spawn-rate", true, 50),
    PIG_ZOMBIE("spawn-pig-zombie", true, 50),
    PIG_ZOMBIE_SPAWN_RATE("pig-zombie-spawn-rate", true, 50),
    MAGMA_CUBE("spawn-magma-cube", true, 50),
    MAGMA_CUBE_SPAWN_RATE("magma-cube-spawn-rate", true, 50),
    SKELETON("spawn-wither-skeleton", true, 50),
    SKELETON_SPAWN_RATE("wither-skeleton-spawn-rate", true, 50);

    private final String name;
    private boolean isEnabled;
    private int spawnRate;

    ConfigOptions(String name, boolean isEnabled, int spawnRate) {
        this.name = name;
        this.isEnabled = isEnabled;
        this.spawnRate = spawnRate;
    }

    public String getName() {
        return name;
    }

    public boolean isEnabled() {
        return isEnabled;
    }

    public void setEnabled(boolean enabled) {
        isEnabled = enabled;
    }

    public int getSpawnRate() {
        return spawnRate;
    }

    public void setSpawnRate(int spawnRate) {
        this.spawnRate = spawnRate;
    }
}
