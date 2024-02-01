package com.github.xniter.mobspawncontrol;

import com.github.xniter.mobspawncontrol.config.ConfigManager;
import com.github.xniter.mobspawncontrol.events.CreatureSpawnListener;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Random;
import java.util.logging.LogManager;
import java.util.logging.Logger;

public class MobSpawnControl extends JavaPlugin {
    public static final Logger LOGGER = LogManager.getLogManager().getLogger("MobSpawnControl");
    private static JavaPlugin plugin;
    private static Random random;

    public MobSpawnControl() {
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
    }

    public void reloadPlugin() {
        // Reload configuration files
        new ConfigManager().loadResources(getPlugin());

        // Re-register event listener
        Bukkit.getPluginManager().registerEvents(new CreatureSpawnListener(), this);

        // Notify admins about the reload
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (player.isOp() || player.hasPermission("mobspawncontrol.admin")) {
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
            if (player.isOp() || player.hasPermission("mobspawncontrol.admin")) {
                player.sendMessage(ChatColor.YELLOW + "MobSpawnControl is being disabled. Some features may not be available.");
            }
        }
    }
}