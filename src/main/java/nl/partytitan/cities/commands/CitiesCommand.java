package nl.partytitan.cities.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import com.google.inject.Inject;
import nl.partytitan.cities.internal.config.interfaces.ISettings;
import org.bukkit.command.CommandSender;

import javax.annotation.Nonnull;
import javax.inject.Named;

@CommandAlias("cities")
@Description("The base cities command")
public class CitiesCommand extends BaseCommand {

    @Inject
    private ISettings settings;

    @Inject
    @Named("PluginVersion")
    private String pluginVersion;

    @Default
    @Subcommand("version")
    @CommandPermission("%user")
    public void version(@Nonnull CommandSender sender) {
        sender.sendMessage("You are using Cities version " + settings.getStorageType());
    }
}
