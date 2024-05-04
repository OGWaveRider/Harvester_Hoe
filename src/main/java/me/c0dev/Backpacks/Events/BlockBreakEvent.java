package me.c0dev.Backpacks.Events;

import me.c0dev.Backpacks.PersistentData.BackPackInformation;
import me.c0dev.Backpacks.PersistentData.BackPackInformationDataType;
import me.c0dev.ItemSerialization;
import me.c0dev.Main;
import me.c0dev.HarvesterHoe.Item.HarvesterHoe;
import me.c0dev.Backpacks.Items.Backpack;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class BlockBreakEvent implements Listener {

    private static final Plugin plugin = JavaPlugin.getPlugin(Main.class);
    private static final FileConfiguration config = plugin.getConfig();

    private static final ItemStack harvesterHoe = HarvesterHoe.HarvesterHoe;
    private static final PersistentDataType<byte[], BackPackInformation> backPackData = new BackPackInformationDataType();
    @EventHandler
    public void onBlockBreak(org.bukkit.event.block.BlockBreakEvent event) {
        Block block = event.getBlock();
        Player player = event.getPlayer();
        PlayerInventory playerInventory = player.getInventory();
        ItemStack itemInHand = playerInventory.getItemInMainHand();

        if (!itemInHand.isSimilar(harvesterHoe)) {
            return;
        }

        // Find backpack in inventory that isn't full
        ItemStack backpackItem = getBackpackInInventory(playerInventory);

        if (backpackItem == null) {
            return;
        }

        ItemMeta itemMeta = backpackItem.getItemMeta();

        if (itemMeta == null) {
            return;
        }

        PersistentDataContainer container = itemMeta.getPersistentDataContainer();

        if (!container.has(Backpack.uuid, backPackData)) {
            return;
        }

        BackPackInformation backpackInformation = container.get(Backpack.uuid, backPackData);

        int backpackSize = backpackInformation.getSize();

        Collection<ItemStack> drops = block.getDrops();

        // TODO
        // Check if item is already in backpack
        // If item is in backpack add to stack
        // If item is not in backpack create new stack & take up new item slot
        // Add item to backpack

        for (ItemStack drop : drops) {
            if (backpackIsFull(backpackItem)) {
                return;
            }
            if (itemIsInbackpack(drop, backpackItem)) {
                if (!backpackIsFullItem(drop, backpackItem)) {
                    // TODO
                } else {
                    // TODO
                }
            } else {
                if (!backpackIsFullItem(drop, backpackItem)) {
                    // TODO
                }
            }
        }
    }

    public ItemStack getBackpackInInventory(PlayerInventory inventory) {
        ItemStack foundBackpack = null;
        for (int itemIdx = 0; itemIdx < inventory.getSize(); itemIdx++) {
            ItemStack item = inventory.getItem(itemIdx);
            if (item != null && item.isSimilar(Backpack.BackPack)) {
                ItemMeta itemMeta = item.getItemMeta();
                if (itemMeta == null) {
                    continue;
                }
                PersistentDataContainer container = itemMeta.getPersistentDataContainer();
                if (container.has(Backpack.uuid, backPackData)) {
                    BackPackInformation backPackInformation = container.get(Backpack.uuid, backPackData);

                    if (backPackInformation == null) {
                        continue;
                    }

                    if (backPackInformation.getMaxItems() >= backPackInformation.getItemAmount() && backpackIsFull(item)) {
                        continue;
                    }
                    foundBackpack = item;
                    break;
                }
            }
        }
        return foundBackpack;
    }

    public boolean itemIsInbackpack(@NonNull ItemStack item, ItemStack backpack) {
        ItemStack itemClone = item.clone();
        itemClone.setAmount(1);
        String itemSerialized = ItemSerialization.serializeItem(itemClone);
        ItemMeta itemMeta = backpack.getItemMeta();
        PersistentDataContainer container = Objects.requireNonNull(itemMeta).getPersistentDataContainer();
        BackPackInformation backPackInformation = container.get(Backpack.uuid, backPackData);
        ConcurrentHashMap<String, Integer> storedItems = Objects.requireNonNull(backPackInformation).getItems();
        return storedItems.containsKey(itemSerialized);
    }

    // Checks for a specific item in the backpack to see if that slot is full
    public boolean backpackIsFullItem(@NonNull ItemStack item, ItemStack backpack) {
        ItemStack itemClone = item.clone();
        itemClone.setAmount(1);
        String itemSerialized = ItemSerialization.serializeItem(itemClone);
        ItemMeta itemMeta = backpack.getItemMeta();
        PersistentDataContainer container = Objects.requireNonNull(itemMeta).getPersistentDataContainer();
        BackPackInformation backPackInformation = container.get(Backpack.uuid, backPackData);
        ConcurrentHashMap<String, Integer> storedItems = Objects.requireNonNull(backPackInformation).getItems();
        int storedAmount = storedItems.get(itemSerialized);
        int maxItemStorage = backPackInformation.getSize();
        return storedAmount >= maxItemStorage;
    }

    // Checks if all slots in the backpack are full
    public boolean backpackIsFull(@NonNull ItemStack backpack) {
        ItemMeta itemMeta = backpack.getItemMeta();
        PersistentDataContainer container = Objects.requireNonNull(itemMeta).getPersistentDataContainer();
        BackPackInformation backPackInformation = container.get(Backpack.uuid, backPackData);
        int itemAmountPerSlotMax = Objects.requireNonNull(backPackInformation).getSize();
        for (Map.Entry<String, Integer> entry : backPackInformation.getItems().entrySet()) {
            int itemAmount = entry.getValue();
            if (itemAmount < itemAmountPerSlotMax) {
                continue;
            }
            return true;
        }
        return false;
    }
}
