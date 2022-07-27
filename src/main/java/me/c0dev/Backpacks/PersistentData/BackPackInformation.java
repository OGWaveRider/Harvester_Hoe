package me.c0dev.Backpacks.PersistentData;

import java.io.Serializable;
import java.util.UUID;

public class BackPackInformation implements Serializable {

    private static final long serialVersionUID = 1L;
    private final UUID uuid;

    public BackPackInformation() {
        this.uuid = UUID.randomUUID();
    }

    public UUID getUuid() {
        return uuid;
    }

}
