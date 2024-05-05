package me.c0dev.HarvesterHoe.Events;

import me.c0dev.Main;
import me.c0dev.HarvesterHoe.PersistentData.Information;
import me.c0dev.HarvesterHoe.PersistentData.InformationDataType;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.data.Ageable;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;
import java.util.EnumSet;
import java.util.concurrent.ConcurrentHashMap;

public class event implements Listener {
    private static Inventory gui;
    private static final EnumSet<Material> ValidMaterials = EnumSet.noneOf(Material.class);
    private static final HashMap<Block, Material> BlockToSeedMap = new HashMap<>();
    private static final ConcurrentHashMap<Material, Integer> MaterialAmount = new ConcurrentHashMap<>(); // TODO Store on HarvesterHoe
    private static final Plugin plugin = Main.getPlugin(Main.class);
    private static final FileConfiguration config = plugin.getConfig();

    static {
        // TODO LOAD DATA
    }

    @EventHandler
    public void onRightClickBlock(PlayerInteractEvent event) {
        if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            if (event.getItem() != null) {
                PersistentDataContainer container = Objects.requireNonNull(event.getItem().getItemMeta()).getPersistentDataContainer();
                NamespacedKey key = new NamespacedKey(Main.getPlugin(Main.class), "harvester_hoe_uuid");
                if (container.has(key, new InformationDataType())) {
                    Player player = event.getPlayer();
                    openNewGui(player);
                }
            }
        }
    }

    @EventHandler
    public void onRightClickAir(PlayerInteractEvent event) {
        if (event.getAction() == Action.RIGHT_CLICK_AIR) {
            if (event.getItem() != null) {
                PersistentDataContainer container = Objects.requireNonNull(event.getItem().getItemMeta()).getPersistentDataContainer();
                NamespacedKey key = new NamespacedKey(Main.getPlugin(Main.class), "harvester_hoe_uuid");
                if (container.has(key, new InformationDataType())) {
                    openNewGui(event.getPlayer());
                }
            }
        }
    }

    @EventHandler
    public void blockBreak(BlockBreakEvent e) {
        Block block = e.getBlock();
        Ageable blockAge = (Ageable) block.getBlockData();
        Player player = e.getPlayer();
        if (player.getInventory().getItemInMainHand().getItemMeta() == null) return;
        PersistentDataContainer container = player.getInventory().getItemInMainHand().getItemMeta().getPersistentDataContainer();
        NamespacedKey key = new NamespacedKey(Main.getPlugin(Main.class), "harvester_hoe_uuid");
        Information info = container.get(key, new InformationDataType());
        // TODO Update Data on HarvesterHoe
        // TODO Call BP BlockBreakEvent

        if (blockAge.getAge() != blockAge.getMaximumAge()) {
            return;
        }

        Material cropBlockType = block.getType();
        Material seedMaterial = getSeedMaterial(block, cropBlockType);
        if (seedMaterial == null) {
            return;
        }
        if (isSeedInInv(player, seedMaterial)) {
            replantCrop(player, block.getLocation(), cropBlockType);
        }
        me.c0dev.Backpacks.Events.BlockBreakEvent event = new me.c0dev.Backpacks.Events.BlockBreakEvent();
        event.onBlockBreak(e);
    }

    @EventHandler
    public void hoeUpgradeEvent(InventoryClickEvent e) {

        if (!e.getInventory().equals(gui)) return;
        e.setCancelled(true);

        Player p = (Player) e.getWhoClicked();
        ItemStack item = p.getInventory().getItemInMainHand();
        ItemMeta meta = item.getItemMeta();

        PersistentDataContainer container = Objects.requireNonNull(p.getInventory().getItemInMainHand().getItemMeta()).getPersistentDataContainer();
        NamespacedKey key = new NamespacedKey(Main.getPlugin(Main.class), "harvester_hoe_uuid");
        Information info = container.get(key, new InformationDataType());

    }

    public void replantCrop(Player player, Location location, Material seedType) {
        PlayerInventory playerInventory = player.getInventory();
        for (int itemIdx = 0; itemIdx < playerInventory.getSize(); itemIdx++) {
            ItemStack item = playerInventory.getItem(itemIdx);
            if (item == null || item.getType().isAir()) {
                continue;
            }
            if (!item.getType().equals(seedType)) {
                continue;
            }
            item.setAmount(item.getAmount() - 1);
        }
        Bukkit.getScheduler().runTaskLater(Main.getInstance(), () -> {
            location.getBlock().setType(seedType);
        }, 5L);
    }

    public boolean isSeedInInv(Player player, Material seedMaterial) {
        PlayerInventory playerInventory = player.getInventory();
        for (int itemIdx = 0; itemIdx < playerInventory.getSize(); itemIdx++)  {
            ItemStack item = playerInventory.getItem(itemIdx);
            if (item == null || item.getType().isAir()) {
                continue;
            }
            if (!item.getType().equals(seedMaterial)) {
                continue;
            }
            return seedMaterial == item.getType();
        }
        return false;
    }

    // Getting the seeds material from the crop block
    public Material getSeedMaterial(Block block, Material cropBlockType) {
        if (!ValidMaterials.contains(cropBlockType)) {
            return null;
        }
        if (!BlockToSeedMap.containsKey(block)) {
            Collection<ItemStack> blockDrops = block.getDrops();
            if (blockDrops.size() > 1) {
                for (ItemStack drop : blockDrops) {
                    if (!drop.getType().equals(cropBlockType)) {
                        return BlockToSeedMap.putIfAbsent(block, drop.getType());
                    }
                }
            }
            return BlockToSeedMap.putIfAbsent(block, cropBlockType);
        }
        return BlockToSeedMap.get(block);
    }


    public static void openNewGui(Player p) {
        // TODO Move completely to config
//        gui = Bukkit.createInventory(null, 54, "Menu");
//        PersistentDataContainer container = p.getInventory().getItemInMainHand().getItemMeta().getPersistentDataContainer();
//        NamespacedKey key = new NamespacedKey(Main.getPlugin(Main.class), "harvester_hoe_uuid");
//        Information info = container.get(key, new InformationDataType());
//
//
//        ItemStack GrayGlasspane = new ItemStack(Material.GRAY_STAINED_GLASS_PANE, 1);
//        ItemMeta meta = GrayGlasspane.getItemMeta();
//        assert meta != null;
//        meta.setDisplayName(ChatColor.GREEN + "");
//        GrayGlasspane.setItemMeta(meta);
//
//        ItemStack WheatUpgrade = new ItemStack(Material.WHEAT, 1);
//        ItemMeta meta1 = WheatUpgrade.getItemMeta();
//        assert meta1 != null;
//        meta1.setDisplayName(ChatColor.YELLOW + "Wheat Upgrade: LVL " + data.getConfig().getInt(info.getUuid() + ".upgrades" + ".wheat"));
//        WheatUpgrade.setItemMeta(meta1);
//
//        ItemStack BeetRootUpgrade = new ItemStack(Material.BEETROOT, 1);
//        ItemMeta beetRootMeta = BeetRootUpgrade.getItemMeta();
//        assert beetRootMeta != null;
//        beetRootMeta.setDisplayName(ChatColor.RED + "BeetRoot Upgrade: LVL " + data.getConfig().getInt(info.getUuid() + ".upgrades" + ".beetroot"));
//        BeetRootUpgrade.setItemMeta(beetRootMeta);
//
//        ItemStack CarrotUpgrade = new ItemStack(Material.CARROT, 1);
//        ItemMeta carrotMeta = CarrotUpgrade.getItemMeta();
//        assert carrotMeta != null;
//        carrotMeta.setDisplayName(ChatColor.GOLD + "Carrot Upgrade: LVL " + data.getConfig().getInt(info.getUuid() + ".upgrades" + ".carrots"));
//        CarrotUpgrade.setItemMeta(carrotMeta);
//
//        ItemStack PotatoUpgrade = new ItemStack(Material.POTATO, 1);
//        ItemMeta potatoMeta = PotatoUpgrade.getItemMeta();
//        assert potatoMeta != null;
//        potatoMeta.setDisplayName(ChatColor.YELLOW + "Potato Upgrade: LVL " + data.getConfig().getInt(info.getUuid() + ".upgrades" + ".potatoes"));
//        PotatoUpgrade.setItemMeta(potatoMeta);
//
//        ItemStack KelpUpgrade = new ItemStack(Material.KELP, 1);
//        ItemMeta kelpMeta = KelpUpgrade.getItemMeta();
//        assert kelpMeta != null;
//        kelpMeta.setDisplayName(ChatColor.DARK_GREEN + "Kelp Upgrade: LVL " + data.getConfig().getInt(info.getUuid() + ".upgrades" + ".kelp"));
//        KelpUpgrade.setItemMeta(kelpMeta);
//
//        ItemStack BambooUpgrade = new ItemStack(Material.BAMBOO, 1);
//        ItemMeta bambooMeta = BambooUpgrade.getItemMeta();
//        assert bambooMeta != null;
//        bambooMeta.setDisplayName(ChatColor.GREEN + "Bamboo Upgrade: LVL " + data.getConfig().getInt(info.getUuid() + ".upgrades" + ".bamboo"));
//        BambooUpgrade.setItemMeta(bambooMeta);
//
//        ItemStack SugarCaneUpgrade = new ItemStack(Material.SUGAR_CANE, 1);
//        ItemMeta sugarCaneMeta = SugarCaneUpgrade.getItemMeta();
//        assert sugarCaneMeta != null;
//        sugarCaneMeta.setDisplayName(ChatColor.DARK_GREEN + "Sugar Cane Upgrade: LVL " + data.getConfig().getInt(info.getUuid() + ".upgrades" + ".sugar_cane"));
//        SugarCaneUpgrade.setItemMeta(sugarCaneMeta);
//
//        for (int i = 0; i < gui.getContents().length; i++) {
//            ItemStack is = gui.getItem(i);
//            if (is == null || is.getType() == Material.AIR) {
//                gui.setItem(i, GrayGlasspane);
//            }
//        }
//
//        ItemStack Collecter = new ItemStack(Material.BARREL, 1);
//        ItemMeta meta2 = Collecter.getItemMeta();
//
//        meta2.setDisplayName(ChatColor.GOLD + "Crops Collected:");
//        List<String> lore1 = new ArrayList<>();
//
//        lore1.add("§9Wheat : " + data.getConfig().getInt(info.getUuid() + ".amount_mined" + ".wheat"));// 0
//        lore1.add("§9Carrots : " + data.getConfig().getInt(info.getUuid() + ".amount_mined" + ".carrots")); // 1
//        lore1.add("§9Potatoes : " + data.getConfig().getInt(info.getUuid() + ".amount_mined" + ".potatoes")); // 2
//        lore1.add("§9Beetroot : " + data.getConfig().getInt(info.getUuid() + ".amount_mined" + ".beetroot")); // 3
//        lore1.add("§9Kelp : " + data.getConfig().getInt(info.getUuid() + ".amount_mined" + ".kelp")); // 4
//        lore1.add("§9Bamboo : " + data.getConfig().getInt(info.getUuid() + ".amount_mined" + ".bamboo")); // 5
//        lore1.add("§9Sugar Cane : " + data.getConfig().getInt(info.getUuid() + ".amount_mined" + ".sugar_cane")); // 7
//
//        meta2.setLore(lore1);
//
//        Collecter.setItemMeta(meta2);
//
//        gui.setItem(10, WheatUpgrade);
//        gui.setItem(12, BeetRootUpgrade);
//        gui.setItem(14, CarrotUpgrade);
//        gui.setItem(16, PotatoUpgrade);
//        gui.setItem(20, KelpUpgrade);
//        gui.setItem(22, BambooUpgrade);
//        gui.setItem(24, SugarCaneUpgrade);
//        gui.setItem(40, Collecter);
//
//        p.openInventory(gui);
    }

    private void updateInventory(final Player player) {
        new BukkitRunnable() {
            @Override
            public void run() {

                if (player.getInventory().getItemInMainHand().getItemMeta() == null) return;
                PersistentDataContainer container = player.getInventory().getItemInMainHand().getItemMeta().getPersistentDataContainer();
                NamespacedKey key = new NamespacedKey(Main.getPlugin(Main.class), "harvester_hoe_uuid");
                Information info = container.get(key, new InformationDataType());


//                if (!player.getInventory().getItemInMainHand().getItemMeta().getPersistentDataContainer().getKeys().contains(key))
//                    return;
//                ItemStack WheatUpgrade = new ItemStack(Material.WHEAT, 1);
//                ItemMeta WheatMeta = WheatUpgrade.getItemMeta();
//                assert WheatMeta != null;
//                WheatMeta.setDisplayName(ChatColor.YELLOW + "Wheat Upgrade: LVL " + data.getConfig().getInt(info.getUuid() + ".upgrades" + ".wheat"));
//                WheatUpgrade.setItemMeta(WheatMeta);
//
//
//                ItemStack BeetRootUpgrade = new ItemStack(Material.BEETROOT, 1);
//                ItemMeta beetRootMeta = BeetRootUpgrade.getItemMeta();
//                assert beetRootMeta != null;
//                beetRootMeta.setDisplayName(ChatColor.RED + "BeetRoot Upgrade: LVL " + data.getConfig().getInt(info.getUuid() + ".upgrades" + ".beetroot"));
//                BeetRootUpgrade.setItemMeta(beetRootMeta);
//
//                ItemStack CarrotUpgrade = new ItemStack(Material.CARROT, 1);
//                ItemMeta carrotMeta = CarrotUpgrade.getItemMeta();
//                assert carrotMeta != null;
//                carrotMeta.setDisplayName(ChatColor.GOLD + "Carrot Upgrade: LVL " + data.getConfig().getInt(info.getUuid() + ".upgrades" + ".carrots"));
//                CarrotUpgrade.setItemMeta(carrotMeta);
//
//                ItemStack PotatoUpgrade = new ItemStack(Material.POTATO, 1);
//                ItemMeta potatoMeta = PotatoUpgrade.getItemMeta();
//                assert potatoMeta != null;
//                potatoMeta.setDisplayName(ChatColor.YELLOW + "Potato Upgrade: LVL " + data.getConfig().getInt(info.getUuid() + ".upgrades" + ".potatoes"));
//                PotatoUpgrade.setItemMeta(potatoMeta);
//
//                ItemStack KelpUpgrade = new ItemStack(Material.KELP, 1);
//                ItemMeta kelpMeta = KelpUpgrade.getItemMeta();
//                assert kelpMeta != null;
//                kelpMeta.setDisplayName(ChatColor.DARK_GREEN + "Kelp Upgrade: LVL " + data.getConfig().getInt(info.getUuid() + ".upgrades" + ".kelp"));
//                KelpUpgrade.setItemMeta(kelpMeta);
//
//                ItemStack BambooUpgrade = new ItemStack(Material.BAMBOO, 1);
//                ItemMeta bambooMeta = BambooUpgrade.getItemMeta();
//                assert bambooMeta != null;
//                bambooMeta.setDisplayName(ChatColor.GREEN + "Bamboo Upgrade: LVL " + data.getConfig().getInt(info.getUuid() + ".upgrades" + ".bamboo"));
//                BambooUpgrade.setItemMeta(bambooMeta);
//
//                ItemStack SugarCaneUpgrade = new ItemStack(Material.SUGAR_CANE, 1);
//                ItemMeta sugarCaneMeta = SugarCaneUpgrade.getItemMeta();
//                assert sugarCaneMeta != null;
//                sugarCaneMeta.setDisplayName(ChatColor.DARK_GREEN + "Sugar Cane Upgrade: LVL " + data.getConfig().getInt(info.getUuid() + ".upgrades" + ".sugar_cane"));
//                SugarCaneUpgrade.setItemMeta(sugarCaneMeta);
//
//                gui.setItem(10, WheatUpgrade);
//                gui.setItem(12, BeetRootUpgrade);
//                gui.setItem(14, CarrotUpgrade);
//                gui.setItem(16, PotatoUpgrade);
//                gui.setItem(20, KelpUpgrade);
//                gui.setItem(22, BambooUpgrade);
//                gui.setItem(24, SugarCaneUpgrade);
            }
        }.runTaskTimer(JavaPlugin.getPlugin(Main.class), 0L, 20L);
    }
}

