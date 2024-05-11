package me.c0dev.HarvesterHoe.Events;

import me.c0dev.Menus.Menu;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.PlayerInventory;

public class HarvesterHoeUpgradeEvent implements Listener {
    @EventHandler
    public void onClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        PlayerInventory playerInventory = player.getInventory();
        Inventory inventory = event.getClickedInventory();
        Inventory topInventory = event.getInventory();

        if (inventory == null) {
            return;
        }

        InventoryType inventoryType = inventory.getType();

        if (inventory.getType().equals(InventoryType.PLAYER) && topInventory.equals(Menu.menuInventory)) {
            event.setCancelled(true);
            return;
        }

        if (!inventory.equals(Menu.menuInventory)) {
            return;
        }

        // TODO Upgrade hoe
    }
}
