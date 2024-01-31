package com.github.xniter.mobspawncontrol;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.EntityType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ConfigOptions {
    private final String name;
    private final Object defaultValue;
    private Object value;

    // New parameters
    private final int defaultSpawnRate;
    private final String[] defaultAllowedWorlds;
    private int spawnRate;
    private String[] allowedWorlds;

    static final Map<String, ConfigOptions> entityConfigMap = new HashMap<>();

    // Constructor without spawn rate and allowed worlds
    public ConfigOptions(String name, Object defaultValue) {
        this.name = name;
        this.defaultValue = defaultValue;
        this.defaultSpawnRate = 0;
        this.defaultAllowedWorlds = null;
    }

    // Constructor with all parameters
    public ConfigOptions(String name, Object defaultValue, int defaultSpawnRate, String[] defaultAllowedWorlds) {
        this.name = name;
        this.defaultValue = defaultValue;
        this.defaultSpawnRate = defaultSpawnRate;
        this.defaultAllowedWorlds = defaultAllowedWorlds != null ? defaultAllowedWorlds : initializeDefaultAllowedWorlds();
    }

    private static String[] initializeDefaultAllowedWorlds() {
        List<String> worldNames = new ArrayList<>();

        for (World world : Bukkit.getServer().getWorlds()) {
            worldNames.add(world.getName());
        }

        return worldNames.toArray(new String[0]);
    }

    public static ConfigOptions getConfigOption(EntityType entityType) {
        return entityConfigMap.get(entityType.name());
    }

    public static void addEntityType(EntityType entityType, boolean defaultValue, int defaultSpawnRate, String[] defaultAllowedWorlds) {
        ConfigOptions option = new ConfigOptions(entityType.name().toLowerCase(), defaultValue, defaultSpawnRate, defaultAllowedWorlds);
        entityConfigMap.put(option.getName(), option);
    }

    public static boolean isEntityEnabled(EntityType entityType) {
        ConfigOptions configOption = getConfigOption(entityType);
        return configOption != null && (Boolean) configOption.getValue();
    }

    public static int getSpawnRate(EntityType entityType) {
        ConfigOptions configOption = getConfigOption(entityType);
        return configOption != null ? configOption.getSpawnRate() : 0;
    }

    public static String[] getAllowedWorlds(EntityType entityType) {
        ConfigOptions configOption = getConfigOption(entityType);
        return configOption != null ? configOption.getAllowedWorlds() : null;
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

    // New getters and setters
    public int getSpawnRate() {
        return spawnRate != 0 ? spawnRate : defaultSpawnRate;
    }

    public void setSpawnRate(int spawnRate) {
        this.spawnRate = spawnRate;
    }

    public String[] getAllowedWorlds() {
        return allowedWorlds != null && allowedWorlds.length > 0 ? allowedWorlds : defaultAllowedWorlds;
    }

    public void setAllowedWorlds(String[] allowedWorlds) {
        this.allowedWorlds = allowedWorlds;
    }
}