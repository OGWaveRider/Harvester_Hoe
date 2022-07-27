package me.c0dev.Backpacks.Items;

import me.c0dev.Backpacks.Files.BackpackDataManager;
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
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;

public class SmallBackpack {

    public static ItemStack SmallBP;
    public static BackpackDataManager config;
    public static BackPackInformation information;

    public static void init() {
        CreateSmallBackpack();
    }

    public static ItemStack CreateSmallBackpack() {
        ItemStack head = new ItemStack(Material.PLAYER_HEAD, 1);
        SkullMeta skullSBP = (SkullMeta) head.getItemMeta();
        skullSBP.setOwner("MHF_Chest");
        head.setItemMeta(skullSBP);
        ItemMeta meta = head.getItemMeta();
        NamespacedKey key = new NamespacedKey(Main.getPlugin(Main.class), "small_backpack");
        information = new BackPackInformation();

        meta.setDisplayName("§9Small Backpack");
        List<String> lore = new ArrayList<>();
        lore.add("§7This backpack can hold 1024 items per crop.");
        meta.getPersistentDataContainer().set(key, new BackPackInformationDataType(), information);
        meta.setLore(lore);

        meta.addEnchant(Enchantment.LURE, 1, true);
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        meta.setUnbreakable(true);
        head.setItemMeta(meta);

        config = new BackpackDataManager(JavaPlugin.getPlugin(Main.class));

        config.getConfig().set(information.getUuid() + ".wheat" + ".amount", 0);
        config.getConfig().set(information.getUuid() + ".potatoes" + ".amount", 0);
        config.getConfig().set(information.getUuid() + ".carrots" + ".amount", 0);
        config.getConfig().set(information.getUuid() + ".beetroot" + ".amount", 0);
        config.getConfig().set(information.getUuid() + ".sugar_cane" + ".amount", 0);
        config.getConfig().set(information.getUuid() + ".kelp" + ".amount", 0);
        config.getConfig().set(information.getUuid() + ".bamboo" + ".amount", 0);

        config.saveConfig();

        SmallBP = head;
        return head;
    }
}
