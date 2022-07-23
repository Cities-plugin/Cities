package nl.partytitan.cities.dependencyinjection;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.inject.AbstractModule;
import com.google.inject.Injector;
import com.google.inject.TypeLiteral;
import com.google.inject.matcher.Matchers;
import com.google.inject.spi.InjectionListener;
import com.google.inject.spi.TypeEncounter;
import com.google.inject.spi.TypeListener;
import nl.partytitan.cities.dependencyinjection.annotations.PostConstruct;
import nl.partytitan.cities.external.economy.EconomyProvider;
import nl.partytitan.cities.external.economy.interfaces.IEconomyRepository;
import nl.partytitan.cities.persistence.flatfile.adapters.LocationAdapter;
import nl.partytitan.cities.persistence.interfaces.ICityBlockRepository;
import nl.partytitan.cities.persistence.interfaces.ICityRepository;
import nl.partytitan.cities.persistence.interfaces.IPlanetRepository;
import nl.partytitan.cities.persistence.interfaces.IResidentRepository;
import nl.partytitan.cities.persistence.providers.CitiesProvider;
import nl.partytitan.cities.persistence.providers.CityBlocksProvider;
import nl.partytitan.cities.persistence.providers.PlanetsProvider;
import nl.partytitan.cities.persistence.providers.ResidentsProvider;
import org.bukkit.Location;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;

public class BinderModule extends AbstractModule implements TypeListener {
    private final JavaPlugin javaPlugin;

    public BinderModule(final @NotNull JavaPlugin javaPlugin) {
        this.javaPlugin = javaPlugin;
    }

    @Override
    protected void configure() {
        super.bindListener(Matchers.any(), this);

        super.bind(JavaPlugin.class).toInstance(javaPlugin);
        super.bind(PluginManager.class).toInstance(javaPlugin.getServer().getPluginManager());
        super.bind(BukkitScheduler.class).toInstance(javaPlugin.getServer().getScheduler());

        super.bind(IEconomyRepository.class).toProvider(EconomyProvider.class);

        // Persistence
        super.bind(Gson.class).toInstance(getCustomGson());

        super.bind(ICityRepository.class).toProvider(CitiesProvider.class);
        super.bind(ICityBlockRepository.class).toProvider(CityBlocksProvider.class);
        super.bind(IPlanetRepository.class).toProvider(PlanetsProvider.class);
        super.bind(IResidentRepository.class).toProvider(ResidentsProvider.class);
    }

    @Override
    public <I> void hear(final @NotNull TypeLiteral<I> typeLiteral, final @NotNull TypeEncounter<I> typeEncounter) {
        typeEncounter.register((InjectionListener<I>) i ->
                Arrays.stream(i.getClass().getMethods()).filter(method -> method.isAnnotationPresent(PostConstruct.class))
                        .forEach(method -> invokeMethod(method, i)));
    }

    private void invokeMethod(final @NotNull Method method, final @NotNull Object object) {
        try {
            method.invoke(object);
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    private Gson getCustomGson(){
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(Location.class, new LocationAdapter());
        return gsonBuilder.create();
    }
}
