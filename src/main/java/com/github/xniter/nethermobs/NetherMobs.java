package com.github.xniter.nethermobs;

import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.Random;

public class NetherMobs extends JavaPlugin {
    private static JavaPlugin plugin;
    private static Random random;
    private boolean replaceOrig;

    public NetherMobs() {
    }

    public void onEnable() {
        plugin = this;
        random = new Random();
        this.createConfig();
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
            } else {
                this.getLogger().info("config.yml found!");
            }

            this.saveConfig();
            ConfigOptions[] var2 = ConfigOptions.values();
            int var3 = var2.length;

            for(int var4 = 0; var4 < var3; ++var4) {
                ConfigOptions option = var2[var4];
                if (!this.getConfig().contains(option.getName())) {
                    this.getConfig().set(option.getName(), option.getDefaultValue());
                    this.saveConfig();
                } else {
                    option.setValue(this.getConfig().get(option.getName()));
                }
            }
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