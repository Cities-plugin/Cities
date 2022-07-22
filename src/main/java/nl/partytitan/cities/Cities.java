package nl.partytitan.cities;

import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import nl.partytitan.cities.command.CommandInitializer;
import nl.partytitan.cities.config.ConfigInitializer;
import nl.partytitan.cities.dependencyinjection.BinderModule;
import nl.partytitan.cities.listener.ListenerInitializer;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Logger;

public class Cities extends JavaPlugin {

    @Inject
    private ConfigInitializer configInitializer;
    @Inject
    private ListenerInitializer listenerInitializer;
    @Inject
    private CommandInitializer commandInitializer;

    @Override
    public void onEnable() {
        final Injector injector = Guice.createInjector(new BinderModule(this));
        injector.injectMembers(this);

        configInitializer.init(injector);
        commandInitializer.init(injector);
        listenerInitializer.init(injector);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}