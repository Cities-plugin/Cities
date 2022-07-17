package nl.partytitan.cities;

import co.aikar.commands.CommandManager;
import co.aikar.commands.PaperCommandManager;
import com.google.inject.Injector;
import nl.partytitan.cities.commands.CitiesCommand;
import nl.partytitan.cities.commands.CityCommand;
import nl.partytitan.cities.commands.ConfirmationCommand;
import nl.partytitan.cities.commands.ResidentCommand;
import nl.partytitan.cities.internal.config.SettingsConfig;
import nl.partytitan.cities.internal.entities.Planet;
import nl.partytitan.cities.internal.repositories.interfaces.IPlanetRepository;
import nl.partytitan.cities.internal.utils.LoggingUtil;
import nl.partytitan.cities.internal.utils.TranslationUtil;
import nl.partytitan.cities.listeners.cities.PlayerChangeChunkListener;
import nl.partytitan.cities.listeners.cities.PlayerChangeCityBlockListener;
import nl.partytitan.cities.listeners.cities.PlayerEnterCityListener;
import nl.partytitan.cities.listeners.cities.PlayerLeaveCityListener;
import nl.partytitan.cities.listeners.player.PlayerJoinListener;
import nl.partytitan.cities.listeners.player.PlayerMoveListener;
import nl.partytitan.cities.listeners.world.WorldLoadListener;
import org.bukkit.World;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import javax.annotation.Nonnull;
import java.io.File;
import java.util.List;

/**
 * Cites for Bukkit
 *
 * @author Partytitan
 */
public class Cities extends JavaPlugin {

    private Injector injector;
    private PaperCommandManager commandManager;
    private SettingsConfig settings;

    @Override
    public void onEnable() {

        System.out.println("====================      Cities      ========================");

        // configure dependency injection
        configure();

        // enable command manager
        registerCommands();

        // enable events
        registerEvents();

        LoggingUtil.sendMsg("Version: " + getVersion() + " - Plugin Enabled");

        System.out.println("==============================================================");
    }

    private void configure() {
        File datafolder = getDataFolder();

        //get settings
        settings = new SettingsConfig(datafolder);

        //get language files
        TranslationUtil.loadLanguage(datafolder, settings.getLanguage());

        // configure dependency injection
        CitiesModule module = new CitiesModule(this, getVersion(), datafolder, settings);
        injector = module.createInjector();
        injector.injectMembers(this);


        // check if all worlds are loaded
        loadUnloadedWorlds();
    }

    private void loadUnloadedWorlds(){
        IPlanetRepository planetRepository = injector.getInstance(IPlanetRepository.class);
        List<World> worlds = getServer().getWorlds();

        if(worlds.size() > planetRepository.getPlanets().size()){
            for (World world : worlds) {
                if(!planetRepository.planetExists(world.getName())){
                    String planetName = world.getName();
                    planetRepository.createPlanet(new Planet(planetName));
                    LoggingUtil.sendMsg(TranslationUtil.of("planet_created", planetName));
                }
            }
        }
    }

    private void registerCommands() {
        commandManager = new PaperCommandManager(this);
        commandManager.enableUnstableAPI("help");

        commandManager.registerCommand(injector.getInstance(CitiesCommand.class));
        commandManager.registerCommand(injector.getInstance(CityCommand.class));
        commandManager.registerCommand(injector.getInstance(ResidentCommand.class));
        commandManager.registerCommand(injector.getInstance(ConfirmationCommand.class));
    }

    private void registerEvents() {
        final PluginManager pluginManager = getServer().getPluginManager();

        // player events
        pluginManager.registerEvents(injector.getInstance(PlayerJoinListener.class), this);
        pluginManager.registerEvents(injector.getInstance(PlayerMoveListener.class), this);

        // world events
        pluginManager.registerEvents(injector.getInstance(WorldLoadListener.class), this);

        // cities events
        pluginManager.registerEvents(injector.getInstance(PlayerChangeChunkListener.class), this);
        pluginManager.registerEvents(injector.getInstance(PlayerEnterCityListener.class), this);
        pluginManager.registerEvents(injector.getInstance(PlayerLeaveCityListener.class), this);
        pluginManager.registerEvents(injector.getInstance(PlayerChangeCityBlockListener.class), this);
    }

    @Override
    public void onDisable() {
        getLogger().info("onDisable is called!");
    }

    @Nonnull
    public Injector getInjector() {
        return injector;
    }

    @Nonnull
    public CommandManager getCommandManager() {
        return commandManager;
    }

    public String getVersion() {
        return (Cities.class.getPackage().getImplementationVersion() == null) ? getDescription().getVersion() + "-unknown" : Cities.class.getPackage().getImplementationVersion();
    }
}
