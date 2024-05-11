package me.c0dev.Backpacks.Utils;

import me.c0dev.Backpacks.Items.Backpack;
import me.c0dev.Backpacks.PersistentData.BackPackInformation;
import me.c0dev.Backpacks.PersistentData.BackPackInformationDataType;
import me.c0dev.ItemSerialization;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

public class Utilities {

    private static final PersistentDataType<byte[], BackPackInformation> backPackData = new BackPackInformationDataType();

    public static ItemStack getBackpackInInventory(Inventory inventory) {
        ItemStack foundBackpack = null;
        for (ItemStack item : inventory.getContents()) {
            System.out.println(item);
            System.out.println(item.isSimilar(Backpack.BackPack));
            if (item != null && item.isSimilar(Backpack.BackPack)) {
                ItemMeta itemMeta = item.getItemMeta();
                if (itemMeta == null) {
                    continue;
                }
                PersistentDataContainer container = itemMeta.getPersistentDataContainer();
                if (container.has(Backpack.uuid, backPackData)) {
                    BackPackInformation backPackInformation = container.get(Backpack.uuid, backPackData);

                    if (backPackInformation.getMaxItems() >= backPackInformation.getItemAmount() && backpackIsFull(item)) {
                        System.out.println("Found backpack but is full");
                        continue;
                    }
                    foundBackpack = item;
                    break;
                }
            }
        }
        return foundBackpack;
    }

    public static boolean itemIsInbackpack(@NonNull ItemStack item, ItemStack backpack) {
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
    public static boolean backpackIsFullItem(@NonNull String item, ItemStack backpack) {
        ItemMeta itemMeta = backpack.getItemMeta();
        PersistentDataContainer container = Objects.requireNonNull(itemMeta).getPersistentDataContainer();
        BackPackInformation backPackInformation = container.get(Backpack.uuid, backPackData);
        ConcurrentHashMap<String, Integer> storedItems = Objects.requireNonNull(backPackInformation).getItems();
        int storedAmount = storedItems.get(item);
        int maxItemStorage = backPackInformation.getSize();
        return storedAmount >= maxItemStorage;
    }

    // Checks if all slots in the backpack are full
    public static boolean backpackIsFull(@NonNull ItemStack backpack) {
        ItemMeta itemMeta = backpack.getItemMeta();
        PersistentDataContainer container = Objects.requireNonNull(itemMeta).getPersistentDataContainer();
        BackPackInformation backPackInformation = container.get(Backpack.uuid, backPackData);
        int backpackItemAmt = Objects.requireNonNull(backPackInformation).getItemAmount();
        int backpackMaxItemAmt = Objects.requireNonNull(backPackInformation).getMaxItems();
        if (backpackItemAmt >= backpackMaxItemAmt) {
            return backPackInformation.getItems().entrySet().stream().filter(entry -> backpackIsFullItem(entry.getKey(), backpack)).isParallel();
        }
        return false;
    }

    public static Item itemStackToItem(@NonNull ItemStack itemStack, Location location) {
        Item item = Objects.requireNonNull(location.getWorld()).dropItem(location, itemStack);
        return item;
    }
}
