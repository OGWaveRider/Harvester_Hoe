package me.c0dev.HarvesterHoe.Events;

import me.c0dev.Backpacks.Items.Backpack;
import me.c0dev.Backpacks.PersistentData.BackPackInformation;
import me.c0dev.Backpacks.PersistentData.BackPackInformationDataType;
import me.c0dev.Backpacks.Utils.Utilities;
import me.c0dev.HarvesterHoe.Item.HarvesterHoe;
import me.c0dev.HarvesterHoe.PersistentData.Information;
import me.c0dev.HarvesterHoe.PersistentData.InformationDataType;
import me.c0dev.ItemSerialization;
import me.c0dev.Main;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class BlockDropItemEvent implements Listener {

    private static final NamespacedKey backpack_uuid = Backpack.uuid;
    private static final NamespacedKey uuid = HarvesterHoe.uuid;
    private static final InformationDataType harvesterHoeData = new InformationDataType();
    private static final BackPackInformationDataType backPackData = new BackPackInformationDataType();
    private static final Plugin plugin = JavaPlugin.getPlugin(Main.class);
    private static final FileConfiguration config = plugin.getConfig();
    private static final ArrayList<Material> actionableBlocks = new ArrayList<>();

    static {
        ConfigurationSection harvesterHoeConfig = config.getConfigurationSection("harvesterhoe");

        if (harvesterHoeConfig == null) {
            throw new NullPointerException("Cannot find harvesterhoe config");
        }

        List<String> blocks = harvesterHoeConfig.getStringList("blocks");

        for (String block : blocks) {
            Material material = Material.valueOf(block.toUpperCase());
            actionableBlocks.add(material);
        }
    }
    @EventHandler
    public void onDropItem(org.bukkit.event.block.BlockDropItemEvent event) {
        Block block = event.getBlock();
        Material blockType = block.getType();
        Player player = event.getPlayer();
        PlayerInventory playerInventory = player.getInventory();
        ItemStack itemInHand = playerInventory.getItemInMainHand();

        if (itemInHand.getType().isAir() || !itemInHand.isSimilar(HarvesterHoe.HarvesterHoe)) {
            return;
        }

        ItemMeta itemMeta = itemInHand.getItemMeta();

        if (itemMeta == null) {
            return;
        }

        if (actionableBlocks.isEmpty()) {
            return;
        }

        PersistentDataContainer container = itemMeta.getPersistentDataContainer();

        if (!container.has(uuid, harvesterHoeData)) {
            return;
        }

        Information harvesterHoeInformation = container.get(uuid, harvesterHoeData);

        if (harvesterHoeInformation == null) {
            return;
        }

        ConcurrentHashMap<Material, Integer> upgrades = harvesterHoeInformation.getUpgrades();

        if (upgrades.isEmpty()) {
            return;
        }

        if (!actionableBlocks.contains(blockType)) {
            return;
        }

        ArrayList<ItemStack> drops = new ArrayList<>();

        int harvesterHoeMulti = upgrades.getOrDefault(blockType, 1);

        event.getItems().forEach(item -> {
            ItemStack drop = item.getItemStack();
            int dropAmount = drop.getAmount();
            int newAmount = dropAmount * harvesterHoeMulti;
            drop.setAmount(newAmount);
            drops.add(drop);
        });

        event.getItems().clear();

        ItemStack backpack = Utilities.getBackpackInInventory(playerInventory);

        if (backpack != null) {
            ItemMeta backpackMeta = backpack.getItemMeta();
            PersistentDataContainer backpackContainer = backpackMeta.getPersistentDataContainer();
            BackPackInformation backPackInformation = backpackContainer.get(backpack_uuid, backPackData);
            for (ItemStack drop: drops) {
                String serializedItem = ItemSerialization.serializeItem(drop);
                int itemAmount = drop.getAmount();
                if (!Utilities.backpackIsFull(backpack) || !Utilities.backpackIsFullItem(serializedItem, backpack)) {
                    backPackInformation.addItem(serializedItem, itemAmount);
                }
            }
        }
    }
}
