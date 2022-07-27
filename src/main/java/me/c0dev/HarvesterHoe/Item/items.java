package me.c0dev.HarvesterHoe.Item;

import me.c0dev.HarvesterHoe.Files.DataManager;
import me.c0dev.Main;
import me.c0dev.HarvesterHoe.PersistentData.Information;
import me.c0dev.HarvesterHoe.PersistentData.InformationDataType;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;

import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;


import java.util.ArrayList;
import java.util.List;



public class items {

    public static ItemStack HarvesterHoe;
    public static void init() {
        CreateHarvesterHoe();
    }

    public static Information information;
    public static DataManager data;
    public static ItemStack CreateHarvesterHoe() {
        ItemStack item = new ItemStack(Material.NETHERITE_HOE, 1);
        ItemMeta meta = item.getItemMeta();
        NamespacedKey key = new NamespacedKey(Main.getPlugin(Main.class), "harvester_hoe_uuid");
        information = new Information();

        assert meta != null;
        meta.setDisplayName("§2§lHarvester Hoe");
            List<String> lore = new ArrayList<>();

            lore.add("§9Left click to harvest crops");// 0
            lore.add("§9Right click for the settings"); // 1
            lore.add(""); // 2
            lore.add("§eWheat Upgrade: LVL 1"); // 3
            lore.add("§cBeetroot Upgrade: LVL 1"); // 4
            lore.add("§6Carrot Upgrade: LVL 1"); // 5
            lore.add("§ePotato Upgrade: LVL 1"); // 6
            lore.add("§2Kelp Upgrade: Lvl 1"); // 7
            lore.add("§aBamboo Upgrade: Lvl 1"); // 8
            lore.add("§2SugarCane Upgrade: Lvl 1"); // 9

            meta.getPersistentDataContainer().set(key, new InformationDataType(), information);
            meta.setLore(lore);



            meta.addEnchant(Enchantment.LURE, 1, true);
            meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
            meta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
            meta.setUnbreakable(true);
            item.setItemMeta(meta);

            data = new DataManager(JavaPlugin.getPlugin(Main.class));

            // Config for upgrade levels
            data.getConfig().set(information.getUuid() + ".upgrades" + ".wheat", 1);
            data.getConfig().set(information.getUuid() + ".upgrades" + ".beetroot", 1);
            data.getConfig().set(information.getUuid() + ".upgrades" + ".carrots", 1);
            data.getConfig().set(information.getUuid() + ".upgrades" + ".potatoes", 1);
            data.getConfig().set(information.getUuid() + ".upgrades" + ".kelp", 1);
            data.getConfig().set(information.getUuid() + ".upgrades" + ".bamboo", 1);
            data.getConfig().set(information.getUuid() + ".upgrades" + ".sugar_cane", 1);

            data.getConfig().set(information.getUuid() + ".amount_mined" + ".wheat", 0);
            data.getConfig().set(information.getUuid() + ".amount_mined" + ".beetroot", 0);
            data.getConfig().set(information.getUuid() + ".amount_mined" + ".carrots", 0);
            data.getConfig().set(information.getUuid() + ".amount_mined" + ".potatoes", 0);
            data.getConfig().set(information.getUuid() + ".amount_mined" + ".kelp", 0);
            data.getConfig().set(information.getUuid() + ".amount_mined" + ".bamboo", 0);
            data.getConfig().set(information.getUuid() + ".amount_mined" + ".sugar_cane", 0);

            data.saveConfig();

            System.out.println(information.getUuid());

            HarvesterHoe = item;
            return item;
    }
}


