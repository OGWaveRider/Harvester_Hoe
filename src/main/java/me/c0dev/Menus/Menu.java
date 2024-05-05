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

    private static int size = 0;
    public static Inventory menuInventory;

    public Menu(InventoryType inventoryType, ItemStack filler, String name, String slotSet) {
        this.menuInventory = Bukkit.createInventory(null, inventoryType, name);
        int i = 0;
        for (char c : slotSet.toCharArray()) {
            switch (c) {
                case '1':
                    menuInventory.setItem(i++, null);
                    slots.put(this.size++, null);
                    break;
                case '0':
                    menuInventory.setItem(i++, filler);
                    slots.put(this.size++, filler);
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
