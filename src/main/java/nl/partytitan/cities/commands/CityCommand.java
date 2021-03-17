package nl.partytitan.cities.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import com.google.inject.Inject;
import nl.partytitan.cities.internal.entities.City;
import nl.partytitan.cities.internal.repositories.interfaces.ICityRepository;
import org.bukkit.command.CommandSender;

import javax.annotation.Nonnull;
import java.util.UUID;

@CommandAlias("c|city")
@Description("The command to create and manage your city")
public class CityCommand extends BaseCommand {

    @Inject
    ICityRepository cityRepository;

    @Default
    @Subcommand("help")
    public void help(@Nonnull CommandSender sender) {
        sender.sendMessage("Help message");
    }

    @Subcommand("create")
    public void create(@Nonnull CommandSender sender, String name) {
        cityRepository.CreateCity(new City(UUID.randomUUID(), name));
        sender.sendMessage("Created town1: " + name);
    }
}
