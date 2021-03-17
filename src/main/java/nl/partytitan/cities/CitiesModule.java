package nl.partytitan.cities;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.name.Names;
import nl.partytitan.cities.internal.config.SettingsConfig;
import nl.partytitan.cities.internal.config.interfaces.ISettings;
import nl.partytitan.cities.internal.repositories.DataModule;
import nl.partytitan.cities.internal.utils.SchedulerUtil;

import javax.annotation.Nonnull;
import java.io.File;

public class CitiesModule extends AbstractModule
{
    private final Cities plugin;
    private final String pluginVersion;
    private File dataFolder;
    private SettingsConfig settingsConfig;

    public CitiesModule(Cities plugin, String pluginVersion, File dataFolder) {
        this.plugin = plugin;
        this.pluginVersion = pluginVersion;
        this.dataFolder = dataFolder;
        this.settingsConfig = new SettingsConfig(dataFolder);
    }

    @Nonnull
    public Injector createInjector() {
        return Guice.createInjector(this);
    }

    @Override
    protected void configure() {
        install(new DataModule(settingsConfig));
        // Here we tell Guice to use our plugin instance everytime we need it
        this.bind(Cities.class).toInstance(this.plugin);
        this.bind(ISettings.class).toInstance(settingsConfig);


        this.bind(String.class).annotatedWith(Names.named("PluginVersion")).toInstance(pluginVersion);
        this.bind(File.class).annotatedWith(Names.named("ConfigFolder")).toInstance(dataFolder);

        requestStaticInjection(SchedulerUtil.class);
    }
}
