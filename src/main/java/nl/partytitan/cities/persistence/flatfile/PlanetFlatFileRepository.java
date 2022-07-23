package nl.partytitan.cities.persistence.flatfile;

import com.google.gson.Gson;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import nl.partytitan.cities.internal.models.Planet;
import nl.partytitan.cities.internal.services.SchedulerService;
import nl.partytitan.cities.persistence.BaseRepository;
import nl.partytitan.cities.persistence.interfaces.IPlanetRepository;
import nl.partytitan.cities.tasks.FlatFileSaveTask;
import nl.partytitan.cities.internal.Constants;
import nl.partytitan.cities.utils.FileUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Singleton
public class PlanetFlatFileRepository extends BaseRepository implements IPlanetRepository {
    private Gson gson;

    private File planetsFolder;
    private File deletedPlanetsFolder;

    private Map<String, Planet> inMemoryNameMap;

    public PlanetFlatFileRepository(Gson gson, SchedulerService schedulerService){
        super(schedulerService);

        this.gson = gson;

        this.planetsFolder = new File(Constants.PLANETS_PATH);
        this.deletedPlanetsFolder = new File(Constants.PLANETS_PATH + File.separator + "deleted");

        FileUtils.checkOrCreateFolder(this.planetsFolder);
        FileUtils.checkOrCreateFolder(this.deletedPlanetsFolder);

        // flatfile makes filtering by property impossible so we use a cache
        List<Planet> cachedPlanets = getPlanetsCache();
        inMemoryNameMap = cachedPlanets.stream().collect(Collectors.toMap(Planet::getName, planet -> planet));
    }

    private List<Planet> getPlanetsCache() {
        File[] planetFiles = planetsFolder.listFiles();
        if (planetFiles == null || planetFiles.length == 0)
            return null;
        List<Planet> planets = new ArrayList();
        for (File file : planetFiles) {
            if(file.isFile()){
                Planet planet = gson.fromJson(FileUtils.convertFileToString(file), Planet.class);
                planets.add(planet);
            }
        }
        return planets;
    }

    @Override
    public List<Planet> getPlanets() {
        return new ArrayList<Planet>(inMemoryNameMap.values());
    }

    @Override
    public Planet getPlanet(String planetname) {
        return inMemoryNameMap.get(planetname);
    }

    @Override
    public boolean planetExists(String planetname) {
        return inMemoryNameMap.containsKey(planetname);
    }

    private File planetFile(Planet planet) {
        return new File(planetsFolder + File.separator + planet.getName() + ".json");
    }

    @Override
    public boolean createPlanet(Planet planet) {
        String body = gson.toJson(planet);
        File file = planetFile(planet);

        this.inMemoryNameMap.put(planet.getName(), planet);

        return this.queryQueue.add(new FlatFileSaveTask(body, file));
    }

    @Override
    public boolean updatePlanet(Planet planet) {
        String body = gson.toJson(planet);
        File file = planetFile(planet);

        this.inMemoryNameMap.put(planet.getName(), planet);

        return this.queryQueue.add(new FlatFileSaveTask(body, file));
    }
}

