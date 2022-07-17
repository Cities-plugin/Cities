package nl.partytitan.cities.internal.utils.injection;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import nl.partytitan.cities.internal.repositories.interfaces.ICityBlockRepository;
import nl.partytitan.cities.internal.repositories.interfaces.ICityRepository;
import nl.partytitan.cities.internal.repositories.interfaces.IPlanetRepository;
import nl.partytitan.cities.internal.repositories.interfaces.IResidentRepository;

@Singleton
public class RepositoryUtil {
    @Inject
    private static IResidentRepository residentRepository;

    @Inject
    private static IPlanetRepository planetRepository;

    @Inject
    private static ICityRepository cityRepository;

    @Inject
    private static ICityBlockRepository cityBlockRepository;

    public static IResidentRepository getResidentRepository() {
        return residentRepository;
    }

    public static IPlanetRepository getPlanetRepository() {
        return planetRepository;
    }

    public static ICityRepository getCityRepository() {
        return cityRepository;
    }

    public static ICityBlockRepository getCityBlockRepository() {
        return cityBlockRepository;
    }
}
