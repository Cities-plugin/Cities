package nl.partytitan.cities.db.flatfile;

import com.google.gson.Gson;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import nl.partytitan.cities.Cities;
import nl.partytitan.cities.internal.entities.Planet;
import nl.partytitan.cities.internal.repositories.BaseRepository;
import nl.partytitan.cities.internal.repositories.interfaces.IPlanetRepository;
import nl.partytitan.cities.internal.tasks.FlatFileSaveTask;
import nl.partytitan.cities.internal.utils.server.FileUtils;

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

    @Inject
    public PlanetFlatFileRepository(@Named("ConfigFolder") File dataFolder, Cities plugin, Gson gson){
        super(plugin);

        this.planetsFolder = new File(dataFolder, "planets");
        this.deletedPlanetsFolder = new File(dataFolder, "planets" + File.separator + "deleted");
        this.gson = gson;

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
