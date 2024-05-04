package me.c0dev.Backpacks.Events;

import me.c0dev.Backpacks.PersistentData.BackPackInformation;
import me.c0dev.Backpacks.PersistentData.BackPackInformationDataType;
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

    // Checks for a specific item in the backpack to see if that slot is full
    public boolean backpackIsFullItem(@NonNull ItemStack item, ItemStack backpack) {
        // TODO
        // If item slot is full & there is another available slot take up new slot
        // If no slots do nothing || If slots are full for that item do nothing
        return false;
    }

    // Checks if all slots in the backpack are full
    public boolean backpackIsFull(@NonNull ItemStack backpack) {
        ItemMeta itemMeta = backpack.getItemMeta();
        PersistentDataContainer container = Objects.requireNonNull(itemMeta).getPersistentDataContainer();
        BackPackInformation backPackInformation = container.get(Backpack.uuid, backPackData);
        for (int itemIdx = 0; itemIdx < Objects.requireNonNull(backPackInformation).getMaxItems(); itemIdx++) {
            int backpackSize = backPackInformation.getSize();
            int itemAmount = backPackInformation.getItems().get(itemIdx).getAmount();
            if (backpackSize < itemAmount) {
                continue;
            }
            return true;
        }
        return false;
    }
}
