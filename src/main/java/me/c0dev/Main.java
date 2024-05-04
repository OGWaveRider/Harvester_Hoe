package me.c0dev;

import me.c0dev.Backpacks.Commands.giveBP;
import me.c0dev.Backpacks.Events.BlockBreakEvent;
import me.c0dev.Backpacks.Events.OpenBPEvent;
import me.c0dev.Backpacks.Items.Backpack;
import me.c0dev.HarvesterHoe.Commands.commands;
import me.c0dev.HarvesterHoe.Commands.getUUID;
import me.c0dev.HarvesterHoe.Events.event;
import me.c0dev.HarvesterHoe.Item.HarvesterHoe;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

public final class Main extends JavaPlugin {


    public FileConfiguration config = this.getConfig();

    public static Plugin instance;
    @Override
    public void onEnable() {
        // Plugin startup logic
        getLogger().info("===============================");
        getLogger().info("===============================");
        getLogger().info("===============================");
        getLogger().info("              C0deV            ");
        getLogger().info("   Premium HarvesterHoe Plugin ");
        getLogger().info("===============================");
        getLogger().info("===============================");
        instance = this;
        HarvesterHoe.init();
        Backpack.init();

        getServer().getPluginManager().registerEvents(new BlockBreakEvent(), this);
        getServer().getPluginManager().registerEvents(new event(), this);
        getServer().getPluginManager().registerEvents(new OpenBPEvent(), this);
        Objects.requireNonNull(getCommand("givePremiumHarvesterHoe")).setExecutor(new commands());
        Objects.requireNonNull(getCommand("getUUID")).setExecutor(new getUUID());
        Objects.requireNonNull(getCommand("giveBP")).setExecutor(new giveBP());

        // PHH Config
        this.config.options().copyDefaults(true);
        saveConfig();

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        saveConfig();
    }
    public static Plugin getInstance()
    {
        return instance;
    }
}

