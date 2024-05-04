package me.c0dev.Backpacks.Commands;

import me.c0dev.Main;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

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

        }
        return false;
    }
}
