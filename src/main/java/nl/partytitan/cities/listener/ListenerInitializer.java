package nl.partytitan.cities.listener;

import com.google.inject.Inject;
import com.google.inject.Injector;
import nl.partytitan.cities.dependencyinjection.interfaces.IInitializer;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

public class ListenerInitializer implements IInitializer {
    @Inject
    private JavaPlugin plugin;

    @Override
    public void init(final @NotNull Injector injector) {

    }
}
