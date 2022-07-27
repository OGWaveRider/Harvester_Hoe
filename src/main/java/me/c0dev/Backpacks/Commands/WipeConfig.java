package me.c0dev.Backpacks.Commands;

import me.c0dev.Backpacks.Files.BackpackDataManager;
import me.c0dev.Main;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class WipeConfig implements CommandExecutor {
    public static BackpackDataManager config;

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            return false;
        }
        if (sender instanceof Player && sender.isOp()) {

            config = new BackpackDataManager(JavaPlugin.getPlugin(Main.class));
            config.getConfig().options().copyDefaults(true);
            for (String key : config.getConfig().getKeys(false)) {
                config.getConfig().set(key, null);
            }
            config.saveConfig();
            config.reloadConfig();
        }
        return true;
    }
}