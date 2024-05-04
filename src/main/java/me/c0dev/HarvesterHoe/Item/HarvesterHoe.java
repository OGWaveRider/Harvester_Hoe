package me.c0dev.HarvesterHoe.Item;

import me.c0dev.Main;
import me.c0dev.HarvesterHoe.PersistentData.Information;
import me.c0dev.HarvesterHoe.PersistentData.InformationDataType;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;

import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;


public class HarvesterHoe {

    public static ItemStack HarvesterHoe;
    public static void init() {
        CreateHarvesterHoe();
    }

    private static final Information information = new Information();
    public static ItemStack CreateHarvesterHoe() {
        ItemStack item = new ItemStack(Material.NETHERITE_HOE, 1);
        ItemMeta meta = item.getItemMeta();
        NamespacedKey key = new NamespacedKey(Main.getPlugin(Main.class), "harvester_hoe_uuid");
        if (meta == null) {
            throw new NullPointerException("Hoe meta is null!");
        }
        meta.setDisplayName("§2§lHarvester Hoe");
            // TODO Store more data on hoe & Display as lore :)
            meta.getPersistentDataContainer().set(key, new InformationDataType(), information);

            meta.addEnchant(Enchantment.LURE, 1, true);
            meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
            meta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
            meta.setUnbreakable(true);
            item.setItemMeta(meta);
            HarvesterHoe = item;
            return item;
    }
}


