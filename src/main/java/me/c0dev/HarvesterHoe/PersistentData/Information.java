package me.c0dev.HarvesterHoe.PersistentData;

import java.io.Serializable;
import java.util.UUID;

public class Information implements Serializable {

    private static final long serialVersionUID = 1L;
    private UUID uuid;

    public Information() {
        this.uuid = UUID.randomUUID();
    }

    public UUID getUuid() {
        return  uuid;
    }
}
