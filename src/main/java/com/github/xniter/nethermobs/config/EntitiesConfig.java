package com.github.xniter.nethermobs.config;

import com.github.xniter.nethermobs.NetherMobs;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.EntityType;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EntitiesConfig {

    public static final Map<String, EntitiesConfig> entityConfigMap = new HashMap<>();
    private static final JavaPlugin plugin = NetherMobs.getPlugin();
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
            NetherMobs.LOGGER.info("No EntitiesConfig.yml found, creating now");

            // Add comment to the top of Entities Config file
            plugin.getConfig().options().header(
                    "MobSpawnControl Configuration\n"
                            + "Set values to true to enable, false to disable\n"
                            + "Spawn rate is an integer value representing the spawn rate (0 to disable spawning)\n"
                            + "Allowed worlds is a list of world names where the entity can spawn"
            );

            for (EntitiesConfig option : entityConfigMap.values()) {
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
            NetherMobs.LOGGER.info("EntitiesConfig.yml found!");

            entityConfigMap.clear();

            for (String entityType : config.getKeys(false)) {
                boolean defaultValue = config.getBoolean(entityType + ".Enabled", false);
                int defaultSpawnRate = config.getInt(entityType + ".SpawnRate", 50);
                List<String> defaultAllowedWorlds = config.getStringList(entityType + ".AllowedWorlds");

                EntitiesConfig option = new EntitiesConfig(entityType.toUpperCase(), defaultValue, defaultSpawnRate, defaultAllowedWorlds);
                entityConfigMap.put(option.getName().toUpperCase(), option);
            }

            plugin.reloadConfig();
        }
    }

    // Check if an entity is defined in the configuration
    public static boolean isEntityDefined(EntityType entityType) {
        return entityConfigMap.containsKey(entityType.toString().toUpperCase());
    }

    private static List<String> initializeDefaultAllowedWorlds() {
        List<String> worldNames = new ArrayList<>();

        for (World world : Bukkit.getServer().getWorlds()) {
            if (world.getEnvironment().equals(World.Environment.NETHER)) {
                worldNames.add(world.getName());
            }

        }

        return worldNames;
    }

    public static EntitiesConfig getConfigOption(EntityType entityType) {
        return entityConfigMap.get(entityType.name());
    }

    // Dynamically add an entity type to the configuration
    public static void addEntityType(EntityType entityType, boolean defaultValue, int defaultSpawnRate, List<String> defaultAllowedWorlds) {
        if (!entityConfigMap.containsKey(entityType.name())) {
            EntitiesConfig option = new EntitiesConfig(entityType.toString().toUpperCase(), defaultValue, defaultSpawnRate, defaultAllowedWorlds);
            entityConfigMap.put(option.getName().toUpperCase(), option);
            saveConfig(); // Save the configuration after adding a new entity type
            NetherMobs.LOGGER.info("Added " + entityType.name() + " to the configuration.");
        }
    }


    // Save the configuration to the file
    public static void saveConfig() {
        FileConfiguration config = plugin.getConfig();
        for (EntitiesConfig option : entityConfigMap.values()) {
            String entitySection = "EntityTypes." + option.getName();
            config.set(entitySection + ".Enabled", option.getDefaultValue());
            config.set(entitySection + ".SpawnRate", option.getSpawnRate());
            config.set(entitySection + ".AllowedWorlds", option.getAllowedWorlds());
        }
        plugin.saveConfig();
    }

    public static int getSpawnRate(EntityType entityType) {
        EntitiesConfig configOption = getConfigOption(entityType);
        return configOption != null ? configOption.getSpawnRate() : 0;
    }

    // Get all entity types from the configuration
    public static List<String> getAllEntityTypes() {
        List<String> entityTypes = new ArrayList<>();

        for (EntitiesConfig option : entityConfigMap.values()) {
            entityTypes.add(option.getName());
        }

        return entityTypes;
    }

    public String getName() {
        return this.name;
    }

    public Object getDefaultValue() {
        return this.defaultValue;
    }

    // New getters and setters
    public int getSpawnRate() {
        return spawnRate != 0 ? spawnRate : defaultSpawnRate;
    }

    public List<String> getAllowedWorlds() {
        return allowedWorlds != null && !allowedWorlds.isEmpty() ? allowedWorlds : defaultAllowedWorlds;
    }

}