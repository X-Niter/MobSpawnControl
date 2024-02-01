package com.github.xniter.mobspawncontrol.config;

import com.github.xniter.mobspawncontrol.MobSpawnControl;
import com.github.xniter.mobspawncontrol.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.EntityType;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.*;

public class EntitiesConfig {

    public static final Map<String, EntitiesConfig> entityConfigMap = new HashMap<>();
    private static final JavaPlugin plugin = MobSpawnControl.getPlugin();
    private static final File ENTITIES_CONFIG_FILE = new File(plugin.getDataFolder(), "EntitiesConfig.yml");
    private final String name;
    private final Object defaultValue;
    // New parameters
    private final int defaultSpawnRate;
    private final List<String> defaultAllowedWorlds;
    private Object value;
    private int spawnRate;
    private List<String> allowedWorlds;

    // Constructor without spawn rate and allowed worlds
    public EntitiesConfig(String name, Object defaultValue) {
        this.name = name;
        this.defaultValue = defaultValue;
        this.defaultSpawnRate = 0;
        this.defaultAllowedWorlds = null;
    }

    // Constructor with all parameters
    public EntitiesConfig(String name, Object defaultValue, int defaultSpawnRate, List<String> defaultAllowedWorlds) {
        this.name = name;
        this.defaultValue = defaultValue;
        this.defaultSpawnRate = defaultSpawnRate;
        this.defaultAllowedWorlds = defaultAllowedWorlds != null ? defaultAllowedWorlds : initializeDefaultAllowedWorlds();
    }

    public static void load(Plugin plugin) {
        FileConfiguration config = plugin.getConfig();

        if (!ENTITIES_CONFIG_FILE.exists()) {
            MobSpawnControl.LOGGER.info("No config.yml found, creating now");

            // Add comment to the top of Entities Config file
            plugin.getConfig().options().setHeader(Arrays.asList(
                    "MobSpawnControl Configuration",
                    "Set values to true to enable, false to disable",
                    "Spawn rate is an integer value representing the spawn rate (0 to disable spawning)",
                    "Allowed worlds is a list of world names where the entity can spawn"
            ));

            for (EntitiesConfig option : entityConfigMap.values()) {

                if (entityConfigMap.isEmpty()) {
                    new Utils().populateEntityMapping();
                }

                // Create a section for each entity type
                String entitySection = "EntityTypes." + option.getName();
                plugin.getConfig().createSection(entitySection);

                // Set values for each entity type
                plugin.getConfig().set(entitySection + ".Enabled", option.getDefaultValue());
                plugin.getConfig().set(entitySection + ".SpawnRate", option.getSpawnRate());
                plugin.getConfig().set(entitySection + ".AllowedWorlds", option.getAllowedWorlds());
            }
            plugin.saveConfig();
        } else {
            MobSpawnControl.LOGGER.info("config.yml found!");

            if (!entityConfigMap.isEmpty()) {
                entityConfigMap.clear();
            }

            for (String entityType : config.getKeys(false)) {
                boolean defaultValue = config.getBoolean(entityType + ".Enabled", false);
                int defaultSpawnRate = config.getInt(entityType + ".SpawnRate", 50);
                List<String> defaultAllowedWorlds = config.getStringList(entityType + ".AllowedWorlds");

                EntitiesConfig option = new EntitiesConfig(entityType.toLowerCase(), defaultValue, defaultSpawnRate, defaultAllowedWorlds);
                entityConfigMap.put(option.getName(), option);
            }
            
            plugin.reloadConfig();
        }
    }

    private static List<String> initializeDefaultAllowedWorlds() {
        List<String> worldNames = new ArrayList<>();

        for (World world : Bukkit.getServer().getWorlds()) {
            worldNames.add(world.getName());
        }

        return worldNames;
    }

    public static EntitiesConfig getConfigOption(EntityType entityType) {
        return entityConfigMap.get(entityType.name());
    }

    public static void addEntityType(EntityType entityType, boolean defaultValue, int defaultSpawnRate, List<String> defaultAllowedWorlds) {
        EntitiesConfig option = new EntitiesConfig(entityType.name().toLowerCase(), defaultValue, defaultSpawnRate, defaultAllowedWorlds);
        entityConfigMap.put(option.getName(), option);
    }

    public static boolean isEntityEnabled(EntityType entityType) {
        EntitiesConfig configOption = getConfigOption(entityType);
        return configOption != null && (Boolean) configOption.getValue();
    }

    public static int getSpawnRate(EntityType entityType) {
        EntitiesConfig configOption = getConfigOption(entityType);
        return configOption != null ? configOption.getSpawnRate() : 0;
    }

    public static List<String> getAllowedWorlds(EntityType entityType) {
        EntitiesConfig configOption = getConfigOption(entityType);
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

    public List<String> getAllowedWorlds() {
        return allowedWorlds != null && !allowedWorlds.isEmpty() ? allowedWorlds : defaultAllowedWorlds;
    }

    public void setAllowedWorlds(List<String> allowedWorlds) {
        this.allowedWorlds = allowedWorlds;
    }


}