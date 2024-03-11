package com.github.xniter.nethermobs;

import com.github.xniter.nethermobs.config.ConfigManager;
import com.github.xniter.nethermobs.config.EntitiesConfig;
import com.github.xniter.nethermobs.events.CreatureSpawnListener;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Collections;
import java.util.Random;
import java.util.logging.LogManager;
import java.util.logging.Logger;

public class NetherMobs extends JavaPlugin {
    public static final Logger LOGGER = LogManager.getLogManager().getLogger("NetherMobs");
    private static JavaPlugin plugin;
    private static Random random;

    public NetherMobs() {
    }

    public static JavaPlugin getPlugin() {
        return plugin;
    }

    public static Random getRandom() {
        return random;
    }

    public void onEnable() {
        plugin = this;
        random = new Random();

        // Load plugin resources
        new ConfigManager().loadResources(getPlugin());

        // Register event listener
        getServer().getPluginManager().registerEvents(new CreatureSpawnListener(), this);

        // Get the Nether world
        World netherWorld = getServer().getWorlds().stream()
                .filter(world -> world.getEnvironment() == World.Environment.NETHER)
                .findFirst()
                .orElse(null);

        // Check if the Nether world is found
        if (netherWorld != null) {
            EntityType[] netherMobs = new EntityType[]{
                    EntityType.GHAST,
                    EntityType.BLAZE,
                    EntityType.PIGLIN,
                    EntityType.PIGLIN_BRUTE,
                    EntityType.ZOMBIFIED_PIGLIN,
                    EntityType.MAGMA_CUBE,
                    EntityType.WITHER_SKELETON
            };

            // Get the name of the Nether world
            String netherWorldName = netherWorld.getName();

            // I need to add the entity types to the configuration with the default values being true, 50, and the Nether world
            for (EntityType entityType : netherMobs) {
                EntitiesConfig.addEntityType(entityType, true, 50, Collections.singletonList(netherWorldName));
            }
        } else {
            LOGGER.warning("Nether world not found. Unable to set default allowed worlds for Nether mobs.");
        }
    }

    public void reloadPlugin() {
        // Reload configuration files
        new ConfigManager().loadResources(getPlugin());

        // Re-register event listener
        Bukkit.getPluginManager().registerEvents(new CreatureSpawnListener(), this);

        // Notify admins about the reload
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (player.isOp() || player.hasPermission("nethermobs.admin")) {
                player.sendMessage(ChatColor.YELLOW + "MobSpawnControl configuration reloaded.");
            }
        }

        LOGGER.info("MobSpawnControl configuration reloaded.");
    }

    public void onDisable() {
        // Log a shutdown message
        LOGGER.info(getPlugin().getName() + " shutting down successfully!");

        // Cancel scheduled tasks
        getServer().getScheduler().cancelTasks(this);

        // Save configuration
        saveConfig();

        // Notify admins
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (player.isOp() || player.hasPermission("nethermobs.admin")) {
                player.sendMessage(ChatColor.YELLOW + "MobSpawnControl is being disabled. Some features may not be available.");
            }
        }
    }
}