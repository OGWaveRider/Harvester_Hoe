package me.c0dev.Backpacks.Commands;

import me.c0dev.Locales.Locales;
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
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (!player.isOp()) {
                return false;
            }
            if (args.length == 0) {
                Locales.localeNotify(player, "Missing backpack type!", true);
                return false;
            }
            String type = args[0];
            if (player.getInventory().firstEmpty() == -1) {
                Locales.localeNotify(player, "You're Inventory is full!", true);
                return false;
            }
            ItemStack backpack = Backpack.createBackpack(type);
            player.getInventory().addItem(backpack);
            return true;
        }
        return false;
    }
}
