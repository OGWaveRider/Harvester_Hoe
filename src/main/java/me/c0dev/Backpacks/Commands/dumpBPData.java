package me.c0dev.Backpacks.Commands;

import me.c0dev.Backpacks.Items.Backpack;
import me.c0dev.Backpacks.PersistentData.BackPackInformation;
import me.c0dev.Backpacks.PersistentData.BackPackInformationDataType;
import me.c0dev.ItemSerialization;
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
            sender.sendMessage(ChatColor.DARK_RED + "[!] " + ChatColor.RED + "You are not allowed to execute this command!");
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
            sender.sendMessage(ChatColor.DARK_RED + "[!] " + ChatColor.RED + "Command must be executed on an item!");
            return true;
        }
        ItemMeta itemMeta = item.getItemMeta();
        if (itemMeta == null) {
            sender.sendMessage(ChatColor.DARK_RED + "[!] " + ChatColor.RED + "Item meta not found!");
            return true;
        }
        PersistentDataContainer container = itemMeta.getPersistentDataContainer();
        BackPackInformation backPackInformation = container.get(Backpack.uuid, backPackData);
        if (backPackInformation == null) {
            sender.sendMessage(ChatColor.DARK_RED + "[!] " + ChatColor.RED + "Backpack Information not found!");
            return true;
        }
        player.sendMessage("Backpack Type: " + backPackInformation.getType());
        if (backPackInformation.getItems().isEmpty()) {
            player.sendMessage("Backpack contains no items");
        } else {
            backPackInformation.getItems().forEach((itemSerialized, itemAmount) -> {
                int i = 0;
                try {
                    ItemStack itemDeserialized = ItemSerialization.deserializeItem(itemSerialized);
                    player.sendMessage("Backpack Item #" + i + ": " +  itemDeserialized.toString());
                    player.sendMessage("Backpack Item Amount #" + i + ": " + itemAmount.toString());
                } catch (Exception e) {
                    return;
                }
            });
        }
        player.sendMessage("Backpack Size: " + backPackInformation.getSize());
        player.sendMessage("Backpack UUID: " + backPackInformation.getUuid());
        return true;
    }
}
