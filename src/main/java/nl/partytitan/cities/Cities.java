package nl.partytitan.cities;

import co.aikar.commands.PaperCommandManager;
import com.google.inject.Injector;
import nl.partytitan.cities.commands.CitiesCommand;
import nl.partytitan.cities.commands.CityCommand;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

import javax.annotation.Nonnull;

/**
 * Cites for Bukkit
 *
 * @author Partytitan
 */
public class Cities extends JavaPlugin {

    private Injector injector;
    private PaperCommandManager commandManager;

    @Override
    public void onEnable() {
        // configure dependency injection
        CitiesModule module = new CitiesModule(this, getVersion(), getDataFolder());
        injector = module.createInjector();
        injector.injectMembers(this);

        // enable command manager
        commandManager = new PaperCommandManager(this);

        registerCommands();

        getServer().getConsoleSender().sendMessage(ChatColor.BLUE + "Cities is running");
        getLogger().info("onEnable is called!");
    }

    private void registerCommands() {
        commandManager.registerCommand(injector.getInstance(CitiesCommand.class));
        commandManager.registerCommand(injector.getInstance(CityCommand.class));
    }

    @Override
    public void onDisable() {
        getLogger().info("onDisable is called!");
    }

    @Nonnull
    public Injector getInjector() {
        return injector;
    }

    public String getVersion() {
        return (Cities.class.getPackage().getImplementationVersion() == null) ? getDescription().getVersion() + "-unknown" : Cities.class.getPackage().getImplementationVersion();
    }
}
