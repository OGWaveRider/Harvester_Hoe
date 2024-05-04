package me.c0dev.Backpacks.Events;

import me.c0dev.Backpacks.PersistentData.BackPackInformation;
import me.c0dev.Backpacks.PersistentData.BackPackInformationDataType;
import me.c0dev.Main;
import me.c0dev.HarvesterHoe.Item.HarvesterHoe;
import me.c0dev.Backpacks.Items.Backpack;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class BlockBreakEvent implements Listener {

    private static final Plugin plugin = JavaPlugin.getPlugin(Main.class);
    private static final FileConfiguration config = plugin.getConfig();

    private static final ItemStack harvesterHoe = HarvesterHoe.HarvesterHoe;
    @EventHandler
    public void onBlockBreak(org.bukkit.event.block.BlockBreakEvent event) {
        Block block = event.getBlock();
        Player player = event.getPlayer();
        PlayerInventory playerInventory = player.getInventory();
        ItemStack itemInHand = playerInventory.getItemInMainHand();

        if (!itemInHand.isSimilar(harvesterHoe)) {
            return;
        }

        ItemMeta itemMeta = itemInHand.getItemMeta();

        if (itemMeta == null) {
            return;
        }

        PersistentDataContainer container = itemMeta.getPersistentDataContainer();

        List<NamespacedKey> keys = new ArrayList<>(Arrays.asList(Backpack.uuid, Backpack.size, Backpack.items));

        PersistentDataType<byte[], BackPackInformation> backpackData = new BackPackInformationDataType();

        if (!container.getKeys().containsAll(keys)) {
            return;
        }

        BackPackInformation backpackInformation = container.get(Backpack.uuid, backpackData);

        int backpackSize = backpackInformation.getSize();

        Collection<ItemStack> drops = block.getDrops();

        // TODO
        // Check if item is already in backpack
        // If item is in backpack add to stack
        // If item is not in backpack create new stack & take up new item slot
        // Add item to backpack
        // If item slot is full & there is another available slot take up new slot
        // If no slots do nothing || If slots are full for that item do nothing
    }
}
