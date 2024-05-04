package me.c0dev.Backpacks.Items;

import me.c0dev.Backpacks.PersistentData.BackPackInformation;
import me.c0dev.Backpacks.PersistentData.BackPackInformationDataType;
import me.c0dev.HarvesterHoe.PersistentData.InformationDataType;
import me.c0dev.Main;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Skull;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.function.Consumer;

public class Backpack {

    private static ItemStack BackPack;
    private static BackPackInformation information;

    private static Plugin plugin = JavaPlugin.getPlugin(Main.class);
    private static FileConfiguration config = plugin.getConfig();

    public static void init() {
        createAllBackpacks();
    }

    public static ArrayList<ItemStack> createAllBackpacks() {
        ArrayList<ItemStack> backpacks = new ArrayList<>();
        ConfigurationSection backpacksConfig = config.getConfigurationSection("backpacks");
        if (backpacksConfig == null) {
            throw new NullPointerException("Backpacks configuration not found!");
        }
        for (String backpackSize : backpacksConfig.getKeys(false)) {
            ItemStack backpack = createBackpack(backpackSize);
            backpacks.add(backpack);
        }
        return backpacks;
    }

    public static ItemStack createBackpack(String type) {
        ConfigurationSection backpacksConfig = config.getConfigurationSection("backpacks." + type);

        if (backpacksConfig == null) {
            throw new NullPointerException("Backpacks configuration not found!");
        }

        NamespacedKey uuid = new NamespacedKey(plugin, "backpack_uuid");
        NamespacedKey size = new NamespacedKey(plugin, "backpack_size");

        List<String> backpackLore = backpacksConfig.getStringList("lore");
        int backpackSize = backpacksConfig.getInt("size");

        ItemStack head = new ItemStack(Material.PLAYER_HEAD, 1);
        SkullMeta meta = (SkullMeta) head.getItemMeta();

        information.setSize(backpackSize);

        if (meta == null) {
            throw new NullPointerException("[!] Skull meta is null!");
        }

        meta.setLore(backpackLore);
        meta.getPersistentDataContainer().set(uuid, new BackPackInformationDataType(), information);
        meta.getPersistentDataContainer().set(size, new BackPackInformationDataType(), information);

        head.setItemMeta(meta);
        return head;
    }
}
