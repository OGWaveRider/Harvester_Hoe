package me.c0dev.Backpacks.Items;

import me.c0dev.Backpacks.PersistentData.BackPackInformation;
import me.c0dev.Backpacks.PersistentData.BackPackInformationDataType;
import me.c0dev.Main;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
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
import java.util.List;

public class Backpack {

    private static Plugin plugin = JavaPlugin.getPlugin(Main.class);
    private static FileConfiguration config = plugin.getConfig();
    public static ItemStack BackPack;
    public static BackPackInformation information;

    public static NamespacedKey uuid = new NamespacedKey(plugin, "backpack_uuid");
    public static NamespacedKey size = new NamespacedKey(plugin, "backpack_size");
    public static NamespacedKey items = new NamespacedKey(plugin, "backpack_items");

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

        List<String> backpackLore = backpacksConfig.getStringList("lore");
        String backpackName = backpacksConfig.getString("name");
        String backpackItem = backpacksConfig.getString("item");
        boolean backpackGlow = backpacksConfig.getBoolean("glow");
        int backpackSize = backpacksConfig.getInt("size");

        ItemStack item = null;

        if (backpackItem == null) {
            item = new ItemStack(Material.PLAYER_HEAD);
        } else {
            item = new ItemStack(Material.valueOf(backpackItem.toUpperCase()));
        }

        ItemMeta meta = item.getItemMeta();

        information.setSize(backpackSize);

        if (meta == null) {
            throw new NullPointerException("Item meta is null");
        }

        if (backpackGlow) {
            meta.addEnchant(Enchantment.LURE, 1, true);
            meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        }

        meta.setDisplayName(backpackName);
        meta.setLore(backpackLore); // TODO Display size???
        meta.getPersistentDataContainer().set(uuid, new BackPackInformationDataType(), information);
        meta.getPersistentDataContainer().set(size, new BackPackInformationDataType(), information);
        meta.getPersistentDataContainer().set(items, new BackPackInformationDataType(), information);

        item.setItemMeta(meta);
        item.setAmount(1);

        BackPack = item;

        return item;
    }
}
