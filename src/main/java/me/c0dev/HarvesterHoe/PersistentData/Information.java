package me.c0dev.HarvesterHoe.PersistentData;

import me.c0dev.Main;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.Serializable;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class Information implements Serializable {

    private static final long serialVersionUID = 1L;
    private UUID uuid;
    private ConcurrentHashMap<Material, Integer> upgrades;
    private static final Plugin plugin = JavaPlugin.getPlugin(Main.class);
    private static final FileConfiguration config = plugin.getConfig();

    public Information() {
        this.uuid = UUID.randomUUID();
        this.upgrades = new ConcurrentHashMap<Material, Integer>();
    }

    // TODO Deduct currency (Use vault)
    public void upgrade(Material type) {
        return;
    }
    public String upgradeCost(Material upgrade) {
        ConfigurationSection harvesterHoeConfig = config.getConfigurationSection("harvesterhoe");

        if (harvesterHoeConfig == null) {
            throw new NullPointerException("Harvesterhoe config is null");
        }

        ConfigurationSection harvesterHoeUpgradeConfig = harvesterHoeConfig.getConfigurationSection("upgrades");

        if (harvesterHoeUpgradeConfig == null) {
            throw new NullPointerException("Harvesterhoe upgrade config is null");
        }

        String upgradeLowerCase = upgrade.toString().toLowerCase();
        ConfigurationSection upgradeTypeConfig = harvesterHoeUpgradeConfig.getConfigurationSection(upgradeLowerCase);

        if (upgradeTypeConfig == null) {
            throw new NullPointerException("Unable to find type " + upgradeLowerCase + " in config");
        }

        ConfigurationSection levelsSection = upgradeTypeConfig.getConfigurationSection("levels");

        if (levelsSection == null) {
            throw new NullPointerException("Unable to find levels config for " + upgradeLowerCase);
        }

        Map<String, Object> upgradeLevels = levelsSection.getValues(false);

        if (upgradeLevels.isEmpty()) {
            throw new NullPointerException("Unable to find levels for " + upgradeLowerCase);
        }

        if (this.upgrades.isEmpty()) {
            return (String) upgradeLevels.get("1");
        }

        int currentLevel = this.upgrades.get(upgrade);
        int nextLevel = currentLevel + 1;
        String nextLvlString = String.valueOf(nextLevel);

        if (!upgradeLevels.containsKey(nextLvlString)) {
            return "N/A";
        }

        return (String) upgradeLevels.get(nextLvlString);
    }
    public ConcurrentHashMap<Material, Integer> getUpgrades() {
        return this.upgrades;
    }
    public UUID getUuid() {
        return uuid;
    }
}
