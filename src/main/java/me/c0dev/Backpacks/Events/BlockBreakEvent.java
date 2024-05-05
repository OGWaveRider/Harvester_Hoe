package me.c0dev.Backpacks.Events;

import me.c0dev.Backpacks.PersistentData.BackPackInformation;
import me.c0dev.Backpacks.PersistentData.BackPackInformationDataType;
import me.c0dev.ItemSerialization;
import me.c0dev.Main;
import me.c0dev.HarvesterHoe.Item.HarvesterHoe;
import me.c0dev.Backpacks.Items.Backpack;
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

import java.util.*;

import static me.c0dev.Backpacks.Utils.Utilities.*;

public class BlockBreakEvent implements Listener {

    private static final Plugin plugin = JavaPlugin.getPlugin(Main.class);
    private static final FileConfiguration config = plugin.getConfig();

    private static final ItemStack harvesterHoe = HarvesterHoe.HarvesterHoe;
    private static final PersistentDataType<byte[], BackPackInformation> backPackData = new BackPackInformationDataType();
    @EventHandler
    public void onBlockBreak(org.bukkit.event.block.BlockBreakEvent event) {
        Block block = event.getBlock();
        Player player = event.getPlayer();
        PlayerInventory playerInventory = player.getInventory();
        ItemStack itemInHand = playerInventory.getItemInMainHand();

        if (!itemInHand.isSimilar(harvesterHoe)) {
            return;
        }

        ItemStack backpackItem = getBackpackInInventory(playerInventory);

        if (backpackItem == null) {
            return;
        }

        ItemMeta itemMeta = backpackItem.getItemMeta();

        if (itemMeta == null) {
            return;
        }

        PersistentDataContainer container = itemMeta.getPersistentDataContainer();

        if (!container.has(Backpack.uuid, backPackData)) {
            return;
        }

        BackPackInformation backpackInformation = container.get(Backpack.uuid, backPackData);

        if (backpackInformation == null) {
            return;
        }

        Collection<ItemStack> drops = block.getDrops();

        if (backpackIsFull(backpackItem)) {
            return;
        }

        for (ItemStack drop : drops) {
            String dropSerialized = ItemSerialization.serializeItem(drop);
            int dropAmount = drop.getAmount();
            if (itemIsInbackpack(drop, backpackItem) && !backpackIsFullItem(dropSerialized, backpackItem)) {
                backpackInformation.addItem(dropSerialized, dropAmount);
            }
            if (!itemIsInbackpack(drop, backpackItem)) {
                backpackInformation.addItem(dropSerialized, dropAmount);
            }
        }
        drops.clear();
    }
}
