package me.c0dev.Backpacks.Commands;

import me.c0dev.Backpacks.Items.Backpack;
import me.c0dev.Backpacks.PersistentData.BackPackInformation;
import me.c0dev.Backpacks.PersistentData.BackPackInformationDataType;
import me.c0dev.ItemSerialization;
import me.c0dev.Locales.Locales;
import me.c0dev.Main;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

public class dumpBPData implements CommandExecutor {
    public Main main;

    private static final PersistentDataType<byte[], BackPackInformation> backPackData = new BackPackInformationDataType();
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.isOp()) {
            Locales.localeNotify((Player) sender, "You are not allowed to execute this command!", true);
            return true;
        }
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.DARK_RED + "[!] " + ChatColor.RED + "You are not allowed to execute this command!");
            return true;
        }
        Player player = (Player) sender;
        PlayerInventory playerInventory = player.getInventory();
        ItemStack item = playerInventory.getItemInMainHand();
        if (item.getType().isAir()) {
            Locales.localeNotify(player, "Command must be executed on an item!", true);
            return true;
        }
        ItemMeta itemMeta = item.getItemMeta();
        if (itemMeta == null) {
            Locales.localeNotify(player, "ItemMeta not found!", true);
            return true;
        }
        PersistentDataContainer container = itemMeta.getPersistentDataContainer();
        BackPackInformation backPackInformation = container.get(Backpack.uuid, backPackData);
        if (backPackInformation == null) {
            Locales.localeNotify(player, "Backpack Information not found!", true);
            return true;
        }
        Locales.localeNotify(player, "Backpack Type: " + backPackInformation.getType(), false);
        if (backPackInformation.getItems().isEmpty()) {
            Locales.localeNotify(player, "Backpack contains no items!", true);
        } else {
            backPackInformation.getItems().forEach((itemSerialized, itemAmount) -> {
                int i = 0;
                try {
                    ItemStack itemDeserialized = ItemSerialization.deserializeItem(itemSerialized);
                    Locales.localeNotify(player, "Backpack Item # " + i + ": " + itemDeserialized.toString(), false);
                    Locales.localeNotify(player, "Backpack Item Amount # " + i + ": " + itemAmount.toString(), false);
                } catch (Exception e) {
                    return;
                }
            });
        }
        Locales.localeNotify(player, "Backpack Size: " + backPackInformation.getSize(), false);
        Locales.localeNotify(player, "Backpack UUID: " + backPackInformation.getUuid(), false);
        return true;
    }
}
