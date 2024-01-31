package com.github.xniter.mobspawncontrol;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.EntityType;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MobSpawnControl extends JavaPlugin {
    private static JavaPlugin plugin;
    private static Random random;
    private boolean replaceOrig;

    public MobSpawnControl() {
    }

    public void onEnable() {
        plugin = this;
        random = new Random();
        this.createConfig();
        // Dynamically add EntityType values to ConfigOptions
//        for (EntityType entityType : EntityType.values()) {
//            if (entityType.isSpawnable() && entityType.isAlive()) {
//                List<String> worldNames = new ArrayList<>();
//                for (World world : Bukkit.getServer().getWorlds()) {
//                    worldNames.add(world.getName());
//                }
//                ConfigOptions.addEntityType(entityType, false, 50, worldNames.toArray(new String[0]));  // Adjust default values as needed
//            }
//        }
        this.getServer().getPluginManager().registerEvents(new EntitySpawning(), this);
    }

    public void onDisable() {
    }

    private void createConfig() {
        try {
            File file = new File(this.getDataFolder(), "config.yml");
            this.getConfig();
            if (!this.getDataFolder().exists()) {
                this.getDataFolder().mkdirs();
            }

            if (!file.exists()) {
                this.getLogger().info("No config.yml found, creating now");
                // Add comments
                this.getConfig().options().header("MobSpawnControl Configuration\n"
                        + "Set values to true to enable, false to disable\n"
                        + "Spawn rate is an integer value representing the spawn rate (0 to disable spawning)\n"
                        + "Allowed worlds is a list of world names where the entity can spawn");

                for (EntityType entityType : EntityType.values()) {
                    if (entityType.isSpawnable() && entityType.isAlive()) {
                        List<String> worldNames = new ArrayList<>();
                        for (World world : Bukkit.getServer().getWorlds()) {
                            worldNames.add(world.getName());
                        }
                        ConfigOptions.addEntityType(entityType, false, 50, worldNames);  // Adjust default values as needed
                    }
                }

                for (ConfigOptions option : ConfigOptions.entityConfigMap.values()) {
                    // Create a section for each entity type
                    String entitySection = "EntityTypes." + option.getName();
                    this.getConfig().createSection(entitySection);

                    // Set values for each entity type
                    this.getConfig().set(entitySection + ".Enabled", option.getDefaultValue());
                    this.getConfig().set(entitySection + ".SpawnRate", option.getSpawnRate());
                    this.getConfig().set(entitySection + ".AllowedWorlds", option.getAllowedWorlds());
                }
            } else {
                this.getLogger().info("config.yml found!");
                this.saveConfig();
            }

            this.saveConfig();
        } catch (Exception var6) {
            var6.printStackTrace();
        }
    }


    public static JavaPlugin getPlugin() {
        return plugin;
    }

    public static Random getRandom() {
        return random;
    }

    public boolean isReplaceOrig() {
        return this.replaceOrig;
    }
}