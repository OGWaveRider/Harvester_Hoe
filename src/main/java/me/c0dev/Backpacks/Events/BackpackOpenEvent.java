package me.c0dev.Backpacks.Events;

import me.c0dev.Backpacks.Items.Backpack;
import me.c0dev.Backpacks.PersistentData.BackPackInformation;
import me.c0dev.ItemSerialization;
import me.c0dev.Main;
import me.c0dev.Backpacks.PersistentData.BackPackInformationDataType;
import me.c0dev.Menus.Menu;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Predicate;

import static me.c0dev.Backpacks.Utils.Utilities.getBackpackInInventory;

public class BackpackOpenEvent implements Listener {

    private static final Plugin plugin = JavaPlugin.getPlugin(Main.class);
    private static final FileConfiguration config = plugin.getConfig();
    private static final NamespacedKey uuid = Backpack.uuid;

    private static final PersistentDataType<byte[], BackPackInformation> backPackData = new BackPackInformationDataType();

    private static final EnumSet<Action> validActions = EnumSet.of(Action.RIGHT_CLICK_AIR, Action.RIGHT_CLICK_BLOCK);
    @EventHandler
    public void onRightClick(PlayerInteractEvent event) {
        Action action = event.getAction();
        ItemStack item = event.getItem();
        Player player = event.getPlayer();
        if (item == null) {
            return;
        }
        if (validActions.contains(action)) {
            ItemMeta itemMeta = item.getItemMeta();
            if (itemMeta == null) {
                return;
            }

            PersistentDataContainer container = itemMeta.getPersistentDataContainer();
            if (container.has(uuid, backPackData)) {
                event.setCancelled(true);
                BackPackInformation backPackInformation = container.get(Backpack.uuid, backPackData);
                String backpackType = backPackInformation.getType();
                ConfigurationSection backpackConfig = config.getConfigurationSection("backpacks." + backpackType);

                if (backpackConfig == null) {
                    throw new NullPointerException("Unable to find backpacks config");
                }

                ConfigurationSection backpackMenu = backpackConfig.getConfigurationSection("menu");

                if (backpackMenu == null) {
                    throw new NullPointerException("Unable to find menu configuration");
                }

                String backpackMenuType = backpackMenu.getString("type");
                String backpackMenuTitle = backpackMenu.getString("title");
                String backpackItem = backpackMenu.getString("item.material");
                List<String> backpackItemLore = backpackMenu.getStringList("item.lore");
                String backpackSlotSet = backpackMenu.getString("partition");
                ItemStack backpackMenuFillerItem = new ItemStack(Material.valueOf(backpackItem));
                InventoryType inventoryType = InventoryType.valueOf(backpackMenuType);
                ConcurrentHashMap<String, Integer> items = backPackInformation.getItems();
                Menu menu = new Menu(inventoryType, backpackMenuFillerItem, backpackMenuTitle, backpackSlotSet);
                openBackpackMenu(menu, player, backPackInformation, items, backpackItemLore);
            }
        }
    }

    @EventHandler
    public void itemHandler(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        Inventory playerInventory = player.getInventory();
        Inventory inventory = event.getClickedInventory();
        Inventory topInventory = event.getInventory();

        if (inventory == null) {
            return;
        }

        if (inventory.getType().equals(InventoryType.PLAYER) && topInventory.equals(Menu.menuInventory)) {
            event.setCancelled(true);
            return;
        }

        if (!inventory.equals(Menu.menuInventory)) {
            return;
        }


        ItemStack backpack = getBackpackInInventory(playerInventory);

        if (backpack == null) {
            return;
        }

        ItemMeta itemMeta = backpack.getItemMeta();

        if (itemMeta == null) {
            return;
        }

        PersistentDataContainer container = itemMeta.getPersistentDataContainer();

        if (!container.has(uuid, backPackData)) {
            return;
        }

        Map<Integer, ItemStack> slots = Menu.slots;
        int clickedSlot = event.getSlot();

        if (!slots.containsKey(clickedSlot)) {
            System.out.println("Slot not found: " + clickedSlot);
            return;
        }

        BackPackInformation backPackInformation = container.get(Backpack.uuid, backPackData);

        if (backPackInformation == null) {
            return;
        }

        int itemSlotMaxAmount = backPackInformation.getSize();
        ItemStack itemInSlot = slots.get(clickedSlot);

        if (itemInSlot == null) {
            return;
        }

        ItemMeta itemSlotMeta = itemInSlot.getItemMeta();

        if (itemSlotMeta == null) {
            return;
        }

        String itemSerialized = ItemSerialization.serializeItem(itemInSlot);

        ConcurrentHashMap<String, Integer> items = backPackInformation.getItems();

        if (items == null || items.isEmpty()) {
            event.setCancelled(true);
            return;
        }

        if (!items.containsKey(itemSerialized)) {
            event.setCancelled(true);
            return;
        }

        int itemAmount = items.get(itemSerialized);
        ClickType clickType = event.getClick();

        switch(clickType) {
            case LEFT:
                if (playerInventory.firstEmpty() == -1) {
                    player.sendMessage(ChatColor.DARK_RED + "[!] " + ChatColor.RED + "You're Inventory is full!");
                    return;
                }
                for (int i = 0; i < itemAmount; i++) {
                    if (playerInventory.firstEmpty() == -1) {
                        break;
                    }
                    playerInventory.addItem(itemInSlot.clone());
                    items.put(itemSerialized, itemAmount--);
                }
                Menu.refreshMenu(player);
                break;
            case RIGHT:
                if (playerInventory.firstEmpty() == -1) {
                    player.sendMessage(ChatColor.DARK_RED + "[!] " + ChatColor.RED + "You're Inventory is full!");
                    return;
                }
                ItemStack itemClone = itemInSlot.clone();
                int newAmount = 0;

                if (itemAmount < 64) {
                    itemClone.setAmount(itemAmount);
                } else {
                    itemClone.setAmount(64);
                    newAmount = itemAmount - 64;
                }
                playerInventory.addItem(itemClone);
                items.put(itemSerialized, newAmount);
                Menu.refreshMenu(player);
                break;
        }

    }

    public void openBackpackMenu(Menu menu, Player player, BackPackInformation backPackInformation, ConcurrentHashMap<String, Integer> items, List<String> lore) {
        for (Map.Entry<String, Integer> entry: items.entrySet()) {
            String itemSerialized = entry.getKey();
            try {
                ItemStack item = ItemSerialization.deserializeItem(itemSerialized);
                ItemStack itemClone = item.clone();
                ItemMeta itemMeta = itemClone.getItemMeta();

                if (itemMeta != null) {
                    ArrayList<String> newLore = updateLore(backPackInformation, lore, entry);
                    itemMeta.setLore(newLore);
                }

                itemClone.setItemMeta(itemMeta);

                menu.newPartition(itemClone);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        Inventory gui = menu.menuInventory;
        player.openInventory(gui);
    }

    private static ArrayList<String> updateLore(BackPackInformation backPackInformation, List<String> lore, Map.Entry<String, Integer> entry) {
        ArrayList<String> newLore = new ArrayList<String>();
        int itemSlotMaxAmount = backPackInformation.getSize();

        for (String l : lore) {
            if (l == null || l.isEmpty()) {
                newLore.add("");
                continue;
            }
            newLore.add(l.replaceAll("<amount>", String.valueOf(entry.getValue())));
            newLore.add(l.replaceAll("<amountMax>", String.valueOf(itemSlotMaxAmount)));
        }
        return newLore;
    }
}
