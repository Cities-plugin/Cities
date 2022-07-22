package nl.partytitan.cities.config;

import com.google.inject.Inject;
import com.google.inject.Injector;
import nl.partytitan.cities.dependencyinjection.interfaces.Initializer;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.logging.Logger;

public class ConfigInitializer implements Initializer {
    @Inject
    private JavaPlugin plugin;

    @Override
    public void init(@NotNull Injector injector) {
        plugin.getLogger().info("Getting configuration");
    }
}
