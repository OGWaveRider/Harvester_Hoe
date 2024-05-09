package me.c0dev.HarvesterHoe.Events;

import me.c0dev.HarvesterHoe.Item.HarvesterHoe;
import me.c0dev.HarvesterHoe.PersistentData.Information;
import me.c0dev.HarvesterHoe.PersistentData.InformationDataType;
import me.c0dev.Locales.Locales;
import me.c0dev.Main;
import me.c0dev.Menus.Menu;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class HarvesterHoeOpenMenuEvent implements Listener {

    private static final NamespacedKey uuid = HarvesterHoe.uuid;
    private static final InformationDataType harvesterHoeData = new InformationDataType();
    private static final EnumSet<Action> validActions = EnumSet.of(Action.RIGHT_CLICK_BLOCK, Action.RIGHT_CLICK_AIR);
    private static final Plugin plugin = JavaPlugin.getPlugin(Main.class);
    private static final FileConfiguration config = plugin.getConfig();
    @EventHandler
    public void onRightClick(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        Action action = event.getAction();

        if (!validActions.contains(action)) {
            return;
        }

        PlayerInventory playerInventory = player.getInventory();
        ItemStack itemInHand = playerInventory.getItemInMainHand();

        if (itemInHand.getType().isAir() || !itemInHand.isSimilar(HarvesterHoe.HarvesterHoe)) {
            return;
        }

        ItemMeta itemMeta = itemInHand.getItemMeta();

        if (itemMeta == null) {
            return;
        }

        PersistentDataContainer container = itemMeta.getPersistentDataContainer();

        if (!container.has(uuid, harvesterHoeData)) {
            return;
        }

        Information information = container.get(uuid, harvesterHoeData);
        ConfigurationSection harvesterHoeConfig = config.getConfigurationSection("harvesterhoe");

        if (harvesterHoeConfig == null) {
            throw new NullPointerException("Unable to find harvesterhoe configuration");
        }

        ConfigurationSection harvesterHoeUpgradeConfig = harvesterHoeConfig.getConfigurationSection("upgrades");

        if (harvesterHoeUpgradeConfig == null) {
            throw new NullPointerException("Unable to find harvesterhoe upgrades configuration");
        }

        ConfigurationSection harvesterHoeMenuConfig = harvesterHoeConfig.getConfigurationSection("menu");

        if (harvesterHoeMenuConfig == null) {
            throw new NullPointerException("Unable to find harvesterhoe menu configuration");
        }

        Menu menu;

        ConfigurationSection menuItems = harvesterHoeMenuConfig.getConfigurationSection("items");

        if (menuItems == null) {
            throw new NullPointerException("Unable to find harvesterhoe menu items configuration");
        }

        String menuItemPane = menuItems.getString("pane");
        Material fillerMaterial = Material.valueOf(menuItemPane);
        ItemStack fillerItem = new ItemStack(fillerMaterial, 1);
        List<String> menuItemUpgradeLore = menuItems.getStringList("upgrade.lore");
        String menuType = harvesterHoeMenuConfig.getString("type");
        String menuTitle = harvesterHoeMenuConfig.getString("title");
        String menuPartition = harvesterHoeMenuConfig.getString("partition");
        if (menuType == null) {
            menu = new Menu(fillerItem, menuTitle, menuPartition);
        } else {
            menu = new Menu(InventoryType.valueOf(menuType.toUpperCase()), fillerItem, menuTitle, menuPartition);
        }
        event.setCancelled(true);
        openHoeMenu(menu, player, harvesterHoeUpgradeConfig, information, menuItemUpgradeLore);
    }

    public void openHoeMenu(Menu menu, Player player, ConfigurationSection upgradeConfig, Information information, List<String> upgradeItemLore) {
        Set<String> upgradesInConfig = upgradeConfig.getKeys(false);
        ConcurrentHashMap<Material, Integer> upgrades = information.getUpgrades();
        for (String upgrade : upgradesInConfig) {
            Material upgradeMaterial = Material.valueOf(upgrade.toUpperCase());
            int upgradeLevel = upgrades.getOrDefault(upgradeMaterial, 0);
            String upgradeMaterialString = Locales.formatCaps(upgradeMaterial.toString());
            String upgradeCost = information.upgradeCost(upgradeMaterial);
            ArrayList<String> lore = modifyLore(upgradeItemLore, upgradeMaterialString, upgradeCost, upgradeLevel);
            ItemStack item = new ItemStack(upgradeMaterial, 1);
            ItemMeta itemMeta = item.getItemMeta();

            if (itemMeta == null) {
                return;
            }

            itemMeta.setLore(lore);

            item.setItemMeta(itemMeta);
            menu.newPartition(menu, item);
        }
        Inventory gui = menu.menuInventory;
        player.openInventory(gui)
    }

    public ArrayList<String> modifyLore(List<String> lore, String type, String cost, int level) {
        ArrayList<String> newLore = new ArrayList<>();
        for (String l : lore) {
            if (l.isEmpty()) {
                continue;
            }
            l.replaceAll("<type>", type);
            l.replaceAll("<cost>", cost);
            l.replaceAll("<level>", String.valueOf(level));
            newLore.add(l);
        }
        return newLore;
    }
}
