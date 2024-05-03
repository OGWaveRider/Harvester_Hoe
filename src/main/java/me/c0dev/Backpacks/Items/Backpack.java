package me.c0dev.Backpacks.Items;

import me.c0dev.Backpacks.PersistentData.BackPackInformation;
import me.c0dev.Backpacks.PersistentData.BackPackInformationDataType;
import me.c0dev.Main;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.ArrayList;
import java.util.List;

public class Backpack {

    public static ItemStack BackPack;
    public static BackPackInformation information;

    public static void init() {
        createBackpack();
    }

    public static ItemStack createBackpack() {
        // TODO
        // Create Backpacks from config
        // Store information on backpacks creation
        ItemStack head = new ItemStack(Material.PLAYER_HEAD, 1);
        return head;
    }
}
