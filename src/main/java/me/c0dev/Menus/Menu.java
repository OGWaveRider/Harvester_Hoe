package me.c0dev.Menus;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class Menu {

    public static Map<Integer, ItemStack> slots = new HashMap<>();
    public static Inventory menuInventory;

    public Menu(InventoryType inventoryType, ItemStack filler, String name, String slotSet) {
        this.menuInventory = Bukkit.createInventory(null, inventoryType, name);
        for (int slotIdx = 0; slotIdx < menuInventory.getSize(); slotIdx++) {
            char[] chars = slotSet.toCharArray();
            char character = chars[slotIdx];
            switch(character) {
                case '0':
                    menuInventory.setItem(slotIdx, filler);
                    slots.put(slotIdx, filler);
                    break;
                case '1':
                    menuInventory.setItem(slotIdx, null);
                    slots.put(slotIdx, null);
                    break;
            }
        }
    }

    public void newPartition(ItemStack item) {
        for (int inventoryIdx = 0; inventoryIdx < menuInventory.getSize(); inventoryIdx++) {
            ItemStack itemInSlot = menuInventory.getItem(inventoryIdx);
            if (itemInSlot != null) {
                continue;
            }
            this.setItemStack(inventoryIdx, item);
        }
    }

    public ItemStack getItemStack(int slot) {
        return this.slots.get(slot);
    }

    public ItemStack setItemStack(int slot, ItemStack item) {
        return this.slots.put(slot, item);
    }

    public static void refreshMenu(Player player) {
        player.updateInventory();
    }
}
