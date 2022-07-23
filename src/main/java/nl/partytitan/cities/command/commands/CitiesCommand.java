package nl.partytitan.cities.command.commands;

import com.google.inject.Inject;
import nl.partytitan.cities.config.configs.MainConfig;
import org.bukkit.command.CommandSender;
import revxrsal.commands.annotation.Command;
import revxrsal.commands.annotation.Default;
import revxrsal.commands.annotation.Description;
import revxrsal.commands.annotation.Subcommand;

@Command("cities")
@Description("The base cities command")
public class CitiesCommand {
    @Inject
    private MainConfig mainConfig;

    @Command({"cities", "cities version"})
    public void version(CommandSender sender) {
        sender.sendMessage("You are using cities version: " + mainConfig.GetConfigVersion());
    }
}
