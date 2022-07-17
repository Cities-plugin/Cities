package nl.partytitan.cities;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.name.Names;
import nl.partytitan.cities.internal.config.SettingsConfig;
import nl.partytitan.cities.db.DataModule;
import nl.partytitan.cities.internal.integrations.IntegrationsModule;
import nl.partytitan.cities.internal.utils.*;
import nl.partytitan.cities.internal.utils.injection.ConfigUtil;
import nl.partytitan.cities.internal.utils.injection.IntegrationsUtil;
import nl.partytitan.cities.internal.utils.injection.RepositoryUtil;
import nl.partytitan.cities.internal.utils.server.SchedulerUtil;
import nl.partytitan.cities.internal.utils.server.ServerUtils;
import nl.partytitan.cities.services.CityService;
import nl.partytitan.cities.services.interfaces.ICityService;
import org.bukkit.Server;
import org.bukkit.plugin.PluginManager;
import org.bukkit.scheduler.BukkitScheduler;

import javax.annotation.Nonnull;
import java.io.File;

public class CitiesModule extends AbstractModule
{
    private final Cities plugin;
    private final String pluginVersion;
    private File dataFolder;
    private SettingsConfig settingsConfig;

    public CitiesModule(Cities plugin, String pluginVersion, File dataFolder, SettingsConfig settingsConfig) {
        this.plugin = plugin;
        this.pluginVersion = pluginVersion;
        this.dataFolder = dataFolder;
        this.settingsConfig = settingsConfig;
    }

    @Nonnull
    public Injector createInjector() {
        return Guice.createInjector(this);
    }

    @Override
    protected void configure() {
        // Here we tell Guice to use our plugin instance everytime we need it
        this.bind(Cities.class).toInstance(this.plugin);
        this.bind(Server.class).toInstance(this.plugin.getServer());
        this.bind(PluginManager.class).toInstance(this.plugin.getServer().getPluginManager());
        this.bind(BukkitScheduler.class).toInstance(this.plugin.getServer().getScheduler());

        this.bind(SettingsConfig.class).toInstance(this.settingsConfig);

        this.bind(String.class).annotatedWith(Names.named("PluginVersion")).toInstance(pluginVersion);
        this.bind(File.class).annotatedWith(Names.named("ConfigFolder")).toInstance(dataFolder);

        // configurable modules
        install(new DataModule(this.plugin, this.settingsConfig));
        install(new IntegrationsModule(this.plugin, this.settingsConfig));

        // services
        this.bind(ICityService.class).to(CityService.class);

        requestStaticInjection(SchedulerUtil.class);
        requestStaticInjection(ServerUtils.class);
        requestStaticInjection(MessageUtil.class);
        requestStaticInjection(RepositoryUtil.class);
        requestStaticInjection(IntegrationsUtil.class);
        requestStaticInjection(ConfigUtil.class);
    }
}
