package nl.partytitan.cities.internal.utils.confirmation;

import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.hover.content.Text;
import nl.partytitan.cities.internal.utils.MessageUtil;
import nl.partytitan.cities.internal.utils.TranslationUtil;
import nl.partytitan.cities.internal.utils.confirmation.obj.Confirmation;
import nl.partytitan.cities.internal.utils.server.SchedulerUtil;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * A class that handles the processing confirmations sent in Towny.
 *
 * @author Lukas Mansour (ArticDive)
 * @author Suneet Tipirneni (Siris)
 */
public class ConfirmationUtil {

    public static String confirmCommand = "/confirm";
    public static String cancelCommand = "/cancel";

    private final static Map<CommandSender, ConfirmationContext> confirmations = new ConcurrentHashMap<>();

    private static final class ConfirmationContext {
        final Confirmation confirmation;
        final int taskID;

        ConfirmationContext(Confirmation confirmation, int taskID) {
            this.confirmation = confirmation;
            this.taskID = taskID;
        }
    }

    /**
     * Revokes the confirmation associated with the given sender.
     *
     * @param sender The sender to get the confirmation from.
     */
    public static void revokeConfirmation(CommandSender sender) {
        ConfirmationContext context = confirmations.get(sender);

        SchedulerUtil.cancelTask(context.taskID);
        Confirmation confirmation = context.confirmation;
        confirmations.remove(sender);

        // Run the cancel handler.
        if (confirmation.getCancelHandler() != null) {
            confirmation.getCancelHandler().run();
        }

        MessageUtil.sendMsg(sender, TranslationUtil.of("msg_successful_cancel"));
    }

    /**
     * Registers and begins the timeout timer for the confirmation.
     *
     * @param sender The sender to receive the confirmation.
     * @param confirmation The confirmation to add.
     */
    public static void sendConfirmation(CommandSender sender, Confirmation confirmation) {

        // Check if confirmation is already active and perform appropriate actions.
        if (confirmations.containsKey(sender)) {
            // Cancel prior Confirmation actions.
            revokeConfirmation(sender);
        }

        // Send the confirmation message.
        String title = confirmation.getTitle();
        sendConfirmMessage(sender, title);

        int duration = confirmation.getDuration();

        Runnable handler = () -> {
            // Show cancel messages only if the confirmation exists.
            if (hasConfirmation(sender)) {
                confirmations.remove(sender);
                MessageUtil.sendErrorMsg(sender, "Confirmation Timed out.");
            }
        };

        int taskID;
        long ticks = 20L * duration;
        taskID = SchedulerUtil.runTaskLater(handler, ticks).getTaskId();

        // Cache the task.
        confirmations.put(sender, new ConfirmationContext(confirmation, taskID));
    }

    /**
     * Internal use only.
     *
     * @param sender The sender using the confirmation.
     */
    public static void acceptConfirmation(CommandSender sender) {
        // Get confirmation
        ConfirmationContext context = confirmations.get(sender);

        // Get handler
        Runnable handler = context.confirmation.getAcceptHandler();

        // Cancel task.
        SchedulerUtil.cancelTask(context.taskID);

        // Remove confirmation as it's been handled.
        confirmations.remove(sender);

        // Execute handler.
        if (context.confirmation.isAsync()) {
            SchedulerUtil.runAsyncTask(handler);
        } else {
            SchedulerUtil.runTask(handler);
        }
    }

    public static boolean hasConfirmation(CommandSender sender) {
        return confirmations.containsKey(sender);
    }

    public static void sendConfirmMessage(CommandSender player, String firstline) {

        String lastline = TranslationUtil.of("msg_confirmation_will_expire");
        // Create confirm button based on given params.
        TextComponent confirmComponent = new TextComponent(ChatColor.GREEN + confirmCommand.replace('/', '[').concat("]"));
        confirmComponent.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(TranslationUtil.of("msg_confirmation_hover_accept"))));
        confirmComponent.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/city:" + confirmCommand.replace("/","")));

        // Create cancel button based on given params.
        TextComponent cancelComponent = new TextComponent(ChatColor.GRAY + cancelCommand.replace('/', '[').concat("]"));
        cancelComponent.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(TranslationUtil.of("msg_confirmation_hover_cancel"))));
        cancelComponent.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/city:" + cancelCommand.replace("/","")));

        // Use spigot to send the message.
        player.spigot().sendMessage(new ComponentBuilder(firstline + "\n")
                .append(confirmComponent).append(ChatColor.WHITE + " - " + TranslationUtil.of("msg_confirmation_click_accept", confirmCommand.replace("/", ""), confirmCommand) + "\n")
                .append(cancelComponent).append(ChatColor.WHITE + " - " + TranslationUtil.of("msg_confirmation_click_cancel", cancelCommand.replace("/", ""), cancelCommand) + "\n")
                .append(lastline)
                .create());
    }
}
