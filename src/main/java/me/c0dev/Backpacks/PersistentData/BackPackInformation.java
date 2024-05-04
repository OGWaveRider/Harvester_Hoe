package me.c0dev.Backpacks.PersistentData;

import org.bukkit.inventory.ItemStack;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.UUID;

public class BackPackInformation implements Serializable {

    private static final long serialVersionUID = 1L;
    private final UUID uuid;
    private int size;
    private int max_items;
    private ArrayList<ItemStack> items;

    public BackPackInformation() {
        this.uuid = UUID.randomUUID();
        this.size = 0;
        this.max_items = 0;
        this.items = new ArrayList<>();
    }

    public boolean addItem(ItemStack item) {
        return this.items.add(item);
    }

    public boolean removeItem(ItemStack item) {
        return this.items.remove(item);
    }

    public int getItemAmount() {
        return this.items.size();
    }

    public ArrayList<ItemStack> getItems() {
        return this.items;
    }

    public ArrayList<ItemStack> setItems(ArrayList<ItemStack> items) {
        return this.items = items;
    }

    public int setSize(int newSize) {
        return size = newSize;
    }

    public UUID getUuid() {
        return uuid;
    }

    public int getSize() {
        return size;
    }

    public int getMaxItems() {
        return this.max_items;
    }

    public int setMaxitems(int newMax) {
        return this.max_items = newMax;
    }

}
