package me.c0dev.Backpacks.Commands;

import me.c0dev.Main;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import me.c0dev.Backpacks.Items.Backpack;

public class giveBP implements CommandExecutor {
    public Main main;
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        // Todo redo
        if (!(sender instanceof Player)) {
            main.getLogger().info("You cannot run this command!");
        }
        Player player = (Player) sender;
        String type = args[0];
        if (player.isOp()) {
            if (player.getInventory().firstEmpty() == -1) {
                player.sendMessage(ChatColor.DARK_RED + "[!] " + ChatColor.RED + "You're Inventory is full!");
                return false;
            }
            ItemStack backpack = Backpack.createBackpack(type);
            player.getInventory().addItem(backpack);
            return true;
        }
        return false;
    }
}
