package me.c0dev.HarvesterHoe.Commands;


import me.c0dev.HarvesterHoe.Events.event;
import me.c0dev.HarvesterHoe.Files.DataManager;
import me.c0dev.Main;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class wipeConfig implements CommandExecutor {
    public static DataManager data;
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            return false;
        }
        if (sender instanceof Player && sender.isOp()) {

            data = new DataManager(JavaPlugin.getPlugin(Main.class));
            data.getConfig().options().copyDefaults(true);
            for (String key : data.getConfig().getKeys(false)) {
                data.getConfig().set(key, null);
            }
            data.saveConfig();
            data.reloadConfig();
        }
        return true;
    }
}
