package nl.partytitan.cities.command;

import com.google.inject.Inject;
import com.google.inject.Injector;
import nl.partytitan.cities.command.commands.CitiesCommand;
import nl.partytitan.cities.dependencyinjection.interfaces.Initializer;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import revxrsal.commands.bukkit.BukkitCommandHandler;

public class CommandInitializer implements Initializer {
    @Inject
    private JavaPlugin plugin;

    @Override
    public void init(final @NotNull Injector injector) {
        plugin.getLogger().info("Register commands");

        BukkitCommandHandler commandHandler = BukkitCommandHandler.create(plugin);

        commandHandler.register(injector.getInstance(CitiesCommand.class));

        commandHandler.registerBrigadier();
    }
}
