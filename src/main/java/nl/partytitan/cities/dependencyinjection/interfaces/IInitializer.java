package nl.partytitan.cities.dependencyinjection.interfaces;

import com.google.inject.Injector;
import org.jetbrains.annotations.NotNull;

public interface IInitializer {
    void init(final @NotNull Injector injector);
}
