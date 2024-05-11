package me.c0dev.Menus;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

public class Menu {

    public static Map<Integer, ItemStack> slots = new HashMap<>();
    private int size = 0;
    public static Inventory menuInventory;
    public Menu(InventoryType inventoryType, ItemStack filler, String name, String slotSet) {
        this.menuInventory = Bukkit.createInventory(null, inventoryType, name);

        int i = 0;
        for (char c : slotSet.toCharArray()) {
            if (c == ' ') {
                continue;
            }

            if (i < this.menuInventory.getSize()) {
                switch (c) {
                    case '1':
                        this.menuInventory.setItem(i, null);
                        slots.put(this.size, null);
                        break;
                    case '0':
                        this.menuInventory.setItem(i, filler);
                        slots.put(this.size, filler);
                        break;
                }
                ++this.size;
            }

            ++i;
        }
    }

    public Menu(ItemStack filler, String name, String slotSet) {
        int i = 0;

        for (char c : slotSet.toCharArray()) {
            if (c == ' ') {
                continue;
            }

            switch (c) {
                case '1':
                    this.slots.put(i, null);
                    break;
                case '0':
                    this.slots.put(i, filler);
                    break;
            }
            ++i;
        }


        this.menuInventory = Bukkit.createInventory(null, i, name);
        this.size = i;

        for (int j = 0; j < i; j++) {
            this.menuInventory.setItem(j, this.slots.get(j));
            slots.put(j, this.slots.get(j));
        }
    }

    public void newPartition(Menu menu, ItemStack item) {
        for (int slot : menu.slots.keySet()) {
            if (menu.slots.get(slot) == null) {
                this.setItemStack(slot, item);
                break;
            }
        }
    }

    public ItemStack getItemStack(int slot) {
        return this.slots.get(slot);
    }

    public ItemStack setItemStack(int slot, ItemStack item) {
        this.slots.put(slot, item);
        if (this.menuInventory != null) {
            this.menuInventory.setItem(slot, item);
        }
        return item;
    }

    public static void refreshMenu(Player player) {
        if (player.getOpenInventory() != null && player.getOpenInventory().getTopInventory().equals(menuInventory)) {
            player.updateInventory();
        }
    }

}
