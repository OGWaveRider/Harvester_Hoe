package me.c0dev.Backpacks.Events;

import me.c0dev.Backpacks.Files.BackpackDataManager;
import me.c0dev.Backpacks.PersistentData.BackPackInformation;
import me.c0dev.HarvesterHoe.Files.DataManager;
import me.c0dev.HarvesterHoe.PersistentData.Information;
import me.c0dev.HarvesterHoe.PersistentData.InformationDataType;
import me.c0dev.Main;
import me.c0dev.Backpacks.PersistentData.BackPackInformationDataType;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class OpenBPEvent implements Listener {

    private Inventory gui;
    public BackpackDataManager config;
    public DataManager data;
    @EventHandler
    public void OnRightClickBlock(PlayerInteractEvent event) {
        if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            if (event.getItem() != null) {
                PersistentDataContainer container = Objects.requireNonNull(event.getItem().getItemMeta()).getPersistentDataContainer();
                NamespacedKey key = new NamespacedKey(Main.getPlugin(Main.class), "small_backpack");
                if (container.has(key, new BackPackInformationDataType())) {
                    Player player = event.getPlayer();
                    player.sendMessage("Opening menu");
                    openNewGUI(player);
                }
            }
        }
    }
    @EventHandler
    public void OnRightClickAir(PlayerInteractEvent event) {
        if (event.getAction() == Action.RIGHT_CLICK_AIR) {
            if (event.getItem() != null) {
                PersistentDataContainer container = event.getItem().getItemMeta().getPersistentDataContainer();
                NamespacedKey key = new NamespacedKey(Main.getPlugin(Main.class), "small_backpack");
                if (container.has(key, new BackPackInformationDataType())) {
                    Player player = event.getPlayer();
                    player.sendMessage("Opening menu"); // Good for debug, bad for production
                    openNewGUI(player);
                }
            }
        }
    }
    @EventHandler
    public void onPlace(BlockPlaceEvent e) {
        Block block = e.getBlock();
        Player player = e.getPlayer();
        PersistentDataContainer container = player.getInventory().getItemInMainHand().getItemMeta().getPersistentDataContainer();
        NamespacedKey key = new NamespacedKey(Main.getPlugin(Main.class), "small_backpack");

        if (container.has(key, new BackPackInformationDataType()))
            if (block.getType() == Material.PLAYER_HEAD) {
                e.setCancelled(true);
            }
    }

    @EventHandler
    public void getItems(InventoryClickEvent e) {
        if (!e.getInventory().equals(gui)) return;
        e.setCancelled(true);
        // get item amount // done
        // add item to inventory until inventory is full or item runs out.
        // deduct item from config

        Player player = (Player) e.getWhoClicked();

        data = new DataManager(JavaPlugin.getPlugin(Main.class));
        Information information = getHarvesterHoe(player);

        Player p = (Player) e.getWhoClicked();
        PersistentDataContainer container = p.getInventory().getItemInMainHand().getItemMeta().getPersistentDataContainer();
        NamespacedKey key = new NamespacedKey(Main.getPlugin(Main.class), "small_backpack");
        BackPackInformation info = container.get(key, new BackPackInformationDataType());
        switch(e.getSlot()) {
            case 1: {
                int amount = config.getConfig().getInt(info.getUuid() + ".wheat" + ".amount");
                int otherAmount = data.getConfig().getInt(information.getUuid() + ".amount_mined" + ".wheat");
                int amountTaken = 0;
                if (amount > 0 && otherAmount > 0) {
                    p.getInventory().addItem(new ItemStack(Material.WHEAT, amount));
                    for (ItemStack stack : p.getInventory().getContents()) {
                        if (stack != null && stack.getType() == Material.WHEAT) {
                            amountTaken += stack.getAmount();
                            config.getConfig().set(info.getUuid() + ".wheat" + ".amount", amount - amountTaken);
                            data.getConfig().set(information.getUuid() + ".amount_mined" + ".wheat", otherAmount - amountTaken);
                            if (amountTaken > amount && amountTaken > otherAmount) return;
                        }
                        data.saveConfig();
                        config.saveConfig();
                    }
                    p.openInventory(gui);
                    updateInventory(p);
                }
                break;
            }
            case 2: {
                // Copy/paste change to next item in config :)
                int amount = config.getConfig().getInt(info.getUuid() + ".wheat" + ".amount");
            }
        }
    }

    public void openNewGUI(Player p) {
        gui = Bukkit.createInventory(null, 9, "Small Backpack");
        List<String> lore = new ArrayList<>();
        List<String> lore2 = new ArrayList<>();
        List<String> lore3 = new ArrayList<>();
        List<String> lore4 = new ArrayList<>();
        List<String> lore5 = new ArrayList<>();
        List<String> lore6 = new ArrayList<>();
        List<String> lore7 = new ArrayList<>();


        config = new BackpackDataManager(JavaPlugin.getPlugin(Main.class));
        PersistentDataContainer container = p.getInventory().getItemInMainHand().getItemMeta().getPersistentDataContainer();
        NamespacedKey key = new NamespacedKey(Main.getPlugin(Main.class), "small_backpack");
        BackPackInformation info = container.get(key, new BackPackInformationDataType());

        ItemStack GrayGlasspane = new ItemStack(Material.GRAY_STAINED_GLASS_PANE, 1);
        ItemMeta meta = GrayGlasspane.getItemMeta();
        assert meta != null;
        meta.setDisplayName(ChatColor.GRAY + "");
        GrayGlasspane.setItemMeta(meta);

        ItemStack Wheat = new ItemStack(Material.WHEAT, 1);
        ItemStack Beetroot = new ItemStack(Material.BEETROOT, 1);
        ItemStack Carrots = new ItemStack(Material.CARROT, 1);
        ItemStack Potatoes = new ItemStack(Material.POTATO, 1);
        ItemStack Kelp = new ItemStack(Material.KELP, 1);
        ItemStack Bamboo = new ItemStack(Material.BAMBOO);
        ItemStack Sugarcane = new ItemStack(Material.SUGAR_CANE, 1);

        ItemMeta WheatMeta = Wheat.getItemMeta();
        ItemMeta BeetrootMeta = Beetroot.getItemMeta();
        ItemMeta CarrotMeta = Carrots.getItemMeta();
        ItemMeta PotatoMeta = Potatoes.getItemMeta();
        ItemMeta KelpMeta = Kelp.getItemMeta();
        ItemMeta BambooMeta = Bamboo.getItemMeta();
        ItemMeta SugarcaneMeta = Sugarcane.getItemMeta();

        WheatMeta.setDisplayName(ChatColor.YELLOW + "Wheat");
        assert info != null;
        lore.add("§7" + config.getConfig().getInt(info.getUuid() + ".wheat" + ".amount") + "§7/1024");
        lore.add("");
        lore.add("§8Left Click to Claim Items");
        WheatMeta.setLore(lore);
        Wheat.setItemMeta(WheatMeta);

        BeetrootMeta.setDisplayName(ChatColor.RED + "Beetroot");
        lore2.add("§7" + config.getConfig().getInt(info.getUuid() + ".beetroot" + ".amount") + "§7/1024");
        lore2.add("");
        lore2.add("§8Left Click to Claim Items");
        BeetrootMeta.setLore(lore2);
        Beetroot.setItemMeta(BeetrootMeta);

        CarrotMeta.setDisplayName(ChatColor.GOLD + "Carrots");
        lore3.add("§7" + config.getConfig().getInt(info.getUuid() + ".carrots" + ".amount") + "§7/1024");
        lore3.add("");
        lore3.add("§8Left Click to Claim Items");
        CarrotMeta.setLore(lore3);
        Carrots.setItemMeta(CarrotMeta);

        PotatoMeta.setDisplayName(ChatColor.YELLOW + "Potatoes");
        lore4.add("§7" + config.getConfig().getInt(info.getUuid() + ".potatoes" + ".amount") + "§7/1024");
        lore4.add("");
        lore4.add("§8Left Click to Claim Items");
        PotatoMeta.setLore(lore4);
        Potatoes.setItemMeta(PotatoMeta);

        KelpMeta.setDisplayName(ChatColor.DARK_GREEN + "Kelp");
        lore5.add("§7" + config.getConfig().getInt(info.getUuid() + ".kelp" + ".amount") + "§7/1024");
        lore5.add("");
        lore5.add("§8Left Click to Claim Items");
        KelpMeta.setLore(lore5);
        Kelp.setItemMeta(KelpMeta);

        BambooMeta.setDisplayName(ChatColor.GREEN + "Bamboo");
        lore6.add("§7" + config.getConfig().getInt(info.getUuid() + ".bamboo" + ".amount") + "§7/1024");
        lore6.add("");
        lore6.add("§8Left Click to Claim Items");
        BambooMeta.setLore(lore6);
        Bamboo.setItemMeta(BambooMeta);

        SugarcaneMeta.setDisplayName(ChatColor.DARK_GREEN + "Sugarcane");
        lore7.add("§7" + config.getConfig().getInt(info.getUuid() + ".sugar_cane" + ".amount") + "§7/1024");
        lore7.add("");
        lore7.add("§8Left Click to Claim Items");
        SugarcaneMeta.setLore(lore7);
        Sugarcane.setItemMeta(SugarcaneMeta);

        gui.setItem(0, GrayGlasspane);
        gui.setItem(1, Wheat);
        gui.setItem(2, Beetroot);
        gui.setItem(3, Carrots);
        gui.setItem(4, Potatoes);
        gui.setItem(5, Kelp);
        gui.setItem(6, Bamboo);
        gui.setItem(7, Sugarcane);
        gui.setItem(8, GrayGlasspane);

        p.openInventory(gui);
    }

    private void updateInventory(final Player player) {
        new BukkitRunnable() {
            @Override
            public void run() {
                List<String> lore = new ArrayList<>();
                List<String> lore2 = new ArrayList<>();
                List<String> lore3 = new ArrayList<>();
                List<String> lore4 = new ArrayList<>();
                List<String> lore5 = new ArrayList<>();
                List<String> lore6 = new ArrayList<>();
                List<String> lore7 = new ArrayList<>();

                if (player.getInventory().getItemInMainHand().getItemMeta() == null) return;
                config = new BackpackDataManager(JavaPlugin.getPlugin(Main.class));
                PersistentDataContainer container = player.getInventory().getItemInMainHand().getItemMeta().getPersistentDataContainer();
                NamespacedKey key = new NamespacedKey(Main.getPlugin(Main.class), "small_backpack");
                BackPackInformation info = container.get(key, new BackPackInformationDataType());
                if (info == null) return;

                ItemStack GrayGlasspane = new ItemStack(Material.GRAY_STAINED_GLASS_PANE, 1);
                ItemMeta meta = GrayGlasspane.getItemMeta();
                assert meta != null;
                meta.setDisplayName(ChatColor.GRAY + "");
                GrayGlasspane.setItemMeta(meta);

                ItemStack Wheat = new ItemStack(Material.WHEAT, 1);
                ItemStack Beetroot = new ItemStack(Material.BEETROOT, 1);
                ItemStack Carrots = new ItemStack(Material.CARROT, 1);
                ItemStack Potatoes = new ItemStack(Material.POTATO, 1);
                ItemStack Kelp = new ItemStack(Material.KELP, 1);
                ItemStack Bamboo = new ItemStack(Material.BAMBOO);
                ItemStack Sugarcane = new ItemStack(Material.SUGAR_CANE, 1);

                ItemMeta WheatMeta = Wheat.getItemMeta();
                ItemMeta BeetrootMeta = Beetroot.getItemMeta();
                ItemMeta CarrotMeta = Carrots.getItemMeta();
                ItemMeta PotatoMeta = Potatoes.getItemMeta();
                ItemMeta KelpMeta = Kelp.getItemMeta();
                ItemMeta BambooMeta = Bamboo.getItemMeta();
                ItemMeta SugarcaneMeta = Sugarcane.getItemMeta();

                WheatMeta.setDisplayName(ChatColor.YELLOW + "Wheat");
                assert info != null;
                lore.add("§7" + config.getConfig().getInt(info.getUuid() + ".wheat" + ".amount") + "§7/1024");
                lore.add("");
                lore.add("§8Left Click to Claim Items");
                WheatMeta.setLore(lore);
                Wheat.setItemMeta(WheatMeta);

                BeetrootMeta.setDisplayName(ChatColor.RED + "Beetroot");
                lore2.add("§7" + config.getConfig().getInt(info.getUuid() + ".beetroot" + ".amount") + "§7/1024");
                lore2.add("");
                lore2.add("§8Left Click to Claim Items");
                BeetrootMeta.setLore(lore2);
                Beetroot.setItemMeta(BeetrootMeta);

                CarrotMeta.setDisplayName(ChatColor.GOLD + "Carrots");
                lore3.add("§7" + config.getConfig().getInt(info.getUuid() + ".carrots" + ".amount") + "§7/1024");
                lore3.add("");
                lore3.add("§8Left Click to Claim Items");
                CarrotMeta.setLore(lore3);
                Carrots.setItemMeta(CarrotMeta);

                PotatoMeta.setDisplayName(ChatColor.YELLOW + "Potatoes");
                lore4.add("§7" + config.getConfig().getInt(info.getUuid() + ".potatoes" + ".amount") + "§7/1024");
                lore4.add("");
                lore4.add("§8Left Click to Claim Items");
                PotatoMeta.setLore(lore4);
                Potatoes.setItemMeta(PotatoMeta);

                KelpMeta.setDisplayName(ChatColor.DARK_GREEN + "Kelp");
                lore5.add("§7" + config.getConfig().getInt(info.getUuid() + ".kelp" + ".amount") + "§7/1024");
                lore5.add("");
                lore5.add("§8Left Click to Claim Items");
                KelpMeta.setLore(lore5);
                Kelp.setItemMeta(KelpMeta);

                BambooMeta.setDisplayName(ChatColor.GREEN + "Bamboo");
                lore6.add("§7" + config.getConfig().getInt(info.getUuid() + ".bamboo" + ".amount") + "§7/1024");
                lore6.add("");
                lore6.add("§8Left Click to Claim Items");
                BambooMeta.setLore(lore6);
                Bamboo.setItemMeta(BambooMeta);

                SugarcaneMeta.setDisplayName(ChatColor.DARK_GREEN + "Sugarcane");
                lore7.add("§7" + config.getConfig().getInt(info.getUuid() + ".sugar_cane" + ".amount") + "§7/1024");
                lore7.add("");
                lore7.add("§8Left Click to Claim Items");
                SugarcaneMeta.setLore(lore7);
                Sugarcane.setItemMeta(SugarcaneMeta);

                gui.setItem(0, GrayGlasspane);
                gui.setItem(1, Wheat);
                gui.setItem(2, Beetroot);
                gui.setItem(3, Carrots);
                gui.setItem(4, Potatoes);
                gui.setItem(5, Kelp);
                gui.setItem(6, Bamboo);
                gui.setItem(7, Sugarcane);
                gui.setItem(8, GrayGlasspane);

            }
        }.runTaskTimer(JavaPlugin.getPlugin(Main.class), 0L, 20L);
    }

    public Information getHarvesterHoe(Player player) {
        for (ItemStack item : player.getInventory().getContents()) {
            if (item != null) {
                PersistentDataContainer container = item.getItemMeta().getPersistentDataContainer();
                NamespacedKey key = new NamespacedKey(Main.getPlugin(Main.class), "harvester_hoe_uuid");
                Information information = container.get(key, new InformationDataType());
                return information;
            }
        }
        return null;
    }
}
