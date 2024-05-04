package me.c0dev.Backpacks.PersistentData;

import java.io.Serializable;
import java.util.UUID;

public class BackPackInformation implements Serializable {

    private static final long serialVersionUID = 1L;
    private final UUID uuid;
    private int size;

    public BackPackInformation() {
        this.uuid = UUID.randomUUID();
        this.size = 0;
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

}
