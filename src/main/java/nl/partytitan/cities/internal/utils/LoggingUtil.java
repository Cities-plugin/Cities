package nl.partytitan.cities.internal.utils;

import nl.partytitan.cities.Cities;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bukkit.ChatColor;

public class LoggingUtil {
    private static final Logger LOGGER = LogManager.getRootLogger();
    private static final Logger LOGGER_DEBUG = LogManager.getLogger("nl.partytitan.cities");

    public static void sendErrorMsg(String msg) {
        LOGGER.warn(ChatColor.RED + "[Cities] Error: " + msg);
    }

    public static void sendErrorMsg(String msg, Object obj) {
        LOGGER.warn(ChatColor.RED + "[Cities] Error: " + msg, obj);
    }

    public static void sendMsg(String msg) {
        LOGGER.info("[Cities] " + msg);
    }
    public static void sendMsg(String msg, Object obj) {
        LOGGER.info("[Cities] " + msg, obj);
    }
}
