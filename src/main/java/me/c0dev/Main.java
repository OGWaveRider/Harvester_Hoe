package me.c0dev;

import me.c0dev.Backpacks.Commands.WipeConfig;
import me.c0dev.Backpacks.Commands.giveSmallBP;
import me.c0dev.Backpacks.Events.OpenBPEvent;
import me.c0dev.Backpacks.Files.BackpackDataManager;
import me.c0dev.Backpacks.Items.SmallBackpack;
import me.c0dev.HarvesterHoe.Commands.commands;
import me.c0dev.HarvesterHoe.Commands.getUUID;
import me.c0dev.HarvesterHoe.Commands.wipeConfig;
import me.c0dev.HarvesterHoe.Events.event;
import me.c0dev.HarvesterHoe.Files.DataManager;
import me.c0dev.HarvesterHoe.Item.items;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;


public final class Main extends JavaPlugin {

    public static Plugin instance;
    public BackpackDataManager config;
    public DataManager data;
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
        items.init();
        SmallBackpack.init();

        Bukkit.getServer().getOnlinePlayers().forEach(player -> {
            if (player.isOp()) {
                player.sendMessage(ChatColor.GOLD + "[!] Harvester Hoe is online!");
            }
        });
        getServer().getPluginManager().registerEvents(new event(), this);
        getServer().getPluginManager().registerEvents(new OpenBPEvent(), this);
        getCommand("givePremiumHarvesterHoe").setExecutor(new commands());
        getCommand("getUUID").setExecutor(new getUUID());
        getCommand("wipeConfig").setExecutor(new wipeConfig());
        getCommand("wipeBPConfig").setExecutor(new WipeConfig());
        getCommand("giveSmallBP").setExecutor(new giveSmallBP());

        // BP Config
        this.config = new BackpackDataManager(this);

        config.getConfig().addDefault("small_bp_total_item_held", 1024);
        config.getConfig().addDefault("medium_bp_total_item_held", 10048);
        config.getConfig().addDefault("large_bp_total_item_held", 100032);
        config.getConfig().options().copyDefaults(true);
        config.saveDefaultConfig();

        // Harvester Hoe
        this.data = new DataManager(this);
        data.getConfig().addDefault("max_upgrade_lvl", 5);
        data.getConfig().options().copyDefaults(true);
        data.saveDefaultConfig();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic

        // Harvester Hoe
        // Test
        this.data = new DataManager(this);
        data.saveDefaultConfig();
        data.saveConfig();

        // BP Config
        this.config = new BackpackDataManager(this);
        config.saveDefaultConfig();
        config.saveConfig();
    }
    public static Plugin getInstance()
    {
        return instance;
    }
}

