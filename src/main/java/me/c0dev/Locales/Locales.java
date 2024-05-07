package me.c0dev.Locales;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class Locales {
    public static void localeNotify(Player player, String message, Boolean failure) {
        ChatColor colorMain = failure ? ChatColor.DARK_RED : ChatColor.DARK_GREEN;;
        ChatColor colorAlt = failure ? ChatColor.RED : ChatColor.GREEN;;
        player.sendMessage(colorMain + "[!] " + colorAlt + message);
    }
    public static void localeNotify(Player player, String message, Boolean failure, ChatColor[] alternativeColors) {
        ChatColor colorMain = failure ? ChatColor.DARK_RED : ChatColor.DARK_GREEN;;
        ChatColor colorAlt = failure ? ChatColor.RED : ChatColor.GREEN;;
        if (alternativeColors.length == 2) {
            colorMain = alternativeColors[0];
            colorAlt = alternativeColors[1];
        }
        player.sendMessage(colorMain + "[!] " + colorAlt + message);
    }
}
