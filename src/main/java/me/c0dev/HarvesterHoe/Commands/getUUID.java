package me.c0dev.HarvesterHoe.Commands;



import me.c0dev.Main;
import me.c0dev.HarvesterHoe.PersistentData.Information;
import me.c0dev.HarvesterHoe.PersistentData.InformationDataType;
import org.bukkit.ChatColor;
import org.bukkit.NamespacedKey;
import org.bukkit.command.Command;

import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataContainer;


public class getUUID implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player)
        {
            Player player = (Player) sender;

            if (player.isOp() || player.hasPermission("givePremiumHarvesterHoe"))
            {
                PersistentDataContainer container = player.getInventory().getItemInMainHand().getItemMeta().getPersistentDataContainer();
                NamespacedKey key = new NamespacedKey(Main.getPlugin(Main.class), "harvester_hoe_uuid");
                Information info = container.get(key, new InformationDataType());
                player.sendMessage(ChatColor.GOLD + "[!] " + ChatColor.YELLOW + "UUID: " + info.getUuid().toString());
            }
            else{
                player.sendMessage(ChatColor.RED + "You do not have the correct permissions!");
            }
        }
        return true;
    }
}
