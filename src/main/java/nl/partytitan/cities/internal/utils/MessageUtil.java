package nl.partytitan.cities.internal.utils;

import com.google.inject.Inject;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.ChatColor;
import org.bukkit.Server;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

public class MessageUtil {


    @Inject
    private static Server server;

    /**
     * Sends an Error message (red) to the Player or console
     *
     * @param sender the Object sending the message
     * @param msg the message to send
     */
    public static void sendErrorMsg(Object sender, String msg) {
        if (sender != null) {
            CommandSender toSend = (CommandSender) sender;
            if (toSend instanceof ConsoleCommandSender) {
                toSend.sendMessage(ChatColor.stripColor(msg));
            } else {
                toSend.sendMessage(TranslationUtil.of("cities_prefix") + ChatColor.RED + msg);
            }
        } else {
            LoggingUtil.sendErrorMsg("Sender cannot be null!");
        }
    }

    /**
     * Sends an message to the Player or console
     *
     * @param sender the Object sending the message
     * @param msg the message to send
     */
    public static void sendMsg(Object sender, String msg) {
        if (sender != null) {
            CommandSender toSend = (CommandSender) sender;
            if (toSend instanceof ConsoleCommandSender) {
                toSend.sendMessage(ChatColor.stripColor(msg));
            } else {
                toSend.sendMessage(TranslationUtil.of("cities_prefix") + msg);
            }
        } else {
            LoggingUtil.sendErrorMsg("Sender cannot be null!");
        }
    }

    /**
     * Send a message to ALL online players and the log.
     *
     * @param lines String array to send as a message
     */
    public static void sendGlobalMessage(String[] lines) {
        for (String line : lines) {
            LoggingUtil.sendMsg(ChatColor.stripColor("[Global Msg] " + line));
        }
        for (Player player : server.getOnlinePlayers()) {
            if (player != null) {
                for (String line : lines) {
                    player.sendMessage(TranslationUtil.of("cities_prefix") + line);
                }
            }
        }
    }

    /**
     * Send a message to ALL online players and the log.
     * Uses default_towny_prefix
     *
     * @param msg message to send
     */
    public static void sendGlobalMessage(String msg) {
        LoggingUtil.sendMsg(ChatColor.stripColor("[Global Msg] " + msg));
        for (Player player : server.getOnlinePlayers()) {
            if (player != null) {
                player.sendMessage(TranslationUtil.of("cities_prefix") + msg);
            }
        }
    }

    public static void sendActionBarMessage(Player player, String msg){
        player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(msg));
    }
}
