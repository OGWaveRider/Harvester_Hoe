package me.c0dev.Backpacks.PersistentData;

import org.bukkit.inventory.ItemStack;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class BackPackInformation implements Serializable {

    private static final long serialVersionUID = 1L;
    private final UUID uuid;
    private int size;
    private int max_items;
    private ConcurrentHashMap<String, Integer> items;

    public BackPackInformation() {
        this.uuid = UUID.randomUUID();
        this.size = 0;
        this.max_items = 0;
        this.items = new ConcurrentHashMap<>();
    }

    public Integer addItem(String item, int amount) {
        return this.items.put(item, amount);
    }

    public Integer removeItemAmount(String item, int amount) {
        int currentAmount = this.items.get(item);
        return this.items.put(item, currentAmount - amount);
    }

    public Integer removeItem(String item) {
        return this.items.remove(item);
    }

    public int getItemAmount() {
        return this.items.size();
    }

    public ConcurrentHashMap<String, Integer> getItems() {
        return this.items;
    }

    public ConcurrentHashMap<String, Integer> setItems(ConcurrentHashMap<String, Integer> items) {
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
