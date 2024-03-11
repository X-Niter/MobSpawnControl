package com.github.xniter.nethermobs.config;

import org.bukkit.plugin.Plugin;

public class ConfigManager {

    public void loadResources(Plugin plugin) {

        plugin.reloadConfig();

        if (!plugin.getDataFolder().exists()) {
            plugin.getDataFolder().mkdirs();
        }

        // Load the Entities Config file
        EntitiesConfig.load(plugin);
    }

}
