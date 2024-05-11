package me.c0dev.HarvesterHoe.Item;

import me.c0dev.Main;
import me.c0dev.HarvesterHoe.PersistentData.Information;
import me.c0dev.HarvesterHoe.PersistentData.InformationDataType;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.enchantments.Enchantment;

import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import javax.naming.ConfigurationException;
import java.io.File;
import java.util.List;


public class HarvesterHoe {

    public static ItemStack HarvesterHoe;
    public static void init() {
        try {
            createHarvesterHoe();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public static Information information = new Information();
    private static final Plugin plugin = JavaPlugin.getPlugin(Main.class);
    private static final FileConfiguration config = plugin.getConfig();

    public static NamespacedKey uuid = new NamespacedKey(plugin, "harvesterhoe_uuid");
    public static NamespacedKey upgrades = new NamespacedKey(plugin, "harvesterhoe_upgrades");
    public static ItemStack createHarvesterHoe() throws NullPointerException, ConfigurationException {
        ConfigurationSection harvesterHoeConfig = config.getConfigurationSection("harvesterhoe");

        if (harvesterHoeConfig == null) {
            throw new ConfigurationException("Harvesterhoe Config Not Found");
        }

        ConfigurationSection harvesterHoeItemConfig = harvesterHoeConfig.getConfigurationSection("item");

        if (harvesterHoeItemConfig == null) {
            throw new ConfigurationException("Harvesterhoe Item Config Not Found");
        }

        ConfigurationSection harvesterHoeUpgradeConfig = harvesterHoeConfig.getConfigurationSection("upgrades");

        if (harvesterHoeUpgradeConfig == null) {
            throw new ConfigurationException("Harvesterhoe Upgrade Config Not Found");
        }

        Material itemMaterial = Material.valueOf(harvesterHoeItemConfig.getString("material"));
        List<String> itemLore = harvesterHoeItemConfig.getStringList("lore");
        boolean itemGlow = harvesterHoeItemConfig.getBoolean("glow");
        boolean itemUnbreakable = harvesterHoeItemConfig.getBoolean("unbreakable");

        ItemStack item = new ItemStack(itemMaterial);
        ItemMeta itemMeta = item.getItemMeta();

        if (itemMeta == null) {
            throw new NullPointerException("Item meta is null");
        }

        PersistentDataContainer container = itemMeta.getPersistentDataContainer();

        if (itemGlow) {
            itemMeta.addEnchant(Enchantment.LURE, 1, true);
            itemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        }

        if (itemUnbreakable) {
            itemMeta.setUnbreakable(true);
            itemMeta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
        }

        container.set(uuid, new InformationDataType(), information);
        container.set(upgrades, new InformationDataType(), information);

        itemMeta.setLore(itemLore);

        item.setItemMeta(itemMeta);
        item.setAmount(1);

        HarvesterHoe = item;

        return item;
    }
}


