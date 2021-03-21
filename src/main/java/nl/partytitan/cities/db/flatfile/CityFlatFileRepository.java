package nl.partytitan.cities.db.flatfile;

import com.google.gson.Gson;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import nl.partytitan.cities.Cities;
import nl.partytitan.cities.internal.entities.City;
import nl.partytitan.cities.internal.entities.Resident;
import nl.partytitan.cities.internal.repositories.BaseRepository;
import nl.partytitan.cities.internal.repositories.interfaces.ICityRepository;
import nl.partytitan.cities.internal.tasks.FlatFileSaveTask;
import nl.partytitan.cities.internal.utils.FileUtils;

import java.io.File;
import java.util.*;
import java.util.stream.Collector;
import java.util.stream.Collectors;

@Singleton
public class CityFlatFileRepository extends BaseRepository implements ICityRepository {

    private Gson gson;

    private File CitiesFolder;
    private File DeletedCitiesFolder;

    private Map<UUID, City> inMemoryIdMap;
    private Map<String, City> inMemoryNameMap;

    @Inject
    public CityFlatFileRepository(@Named("ConfigFolder") File dataFolder, Cities plugin){
        super(plugin);

        this.CitiesFolder = new File(dataFolder, "cities");
        this.DeletedCitiesFolder = new File(dataFolder, "cities" + File.separator + "deleted");

        FileUtils.checkOrCreateFolder(this.CitiesFolder);
        FileUtils.checkOrCreateFolder(this.DeletedCitiesFolder);
        this.gson = new Gson();

        // flatfile makes filtering by property impossible so we use a cache
        List<City> cachedCities = getCitiesCache();
        inMemoryIdMap = cachedCities.stream().collect(Collectors.toMap(City::getId, city -> city));
        inMemoryNameMap = cachedCities.stream().collect(Collectors.toMap(City::getName, city -> city));
    }

    private List<City> getCitiesCache() {
        File[] files = CitiesFolder.listFiles();
        if (files == null || files.length == 0)
            return null;
        List<City> cities = new ArrayList();
        for (File file : files) {
            if(file.isFile()){
                City city = gson.fromJson(FileUtils.convertFileToString(file), City.class);
                cities.add(city);
            }
        }
        return cities;
    }

    @Override
    public List<City> getCities() {
        List<City> cities = new ArrayList<City>(inMemoryIdMap.values());
        for (City city : cities) {
            getPlugin().getInjector().injectMembers(city);
        }
        return cities;
    }

    @Override
    public City getCity(UUID id) {
        City city = inMemoryIdMap.get(id);
        getPlugin().getInjector().injectMembers(city);
        return city;
    }

    @Override
    public City getCity(String cityName) {
        return inMemoryNameMap.get(cityName);
    }

    private File cityFile(City city) {
        return new File(CitiesFolder + File.separator + city.getId() + ".json");
    }

    @Override
    public boolean createCity(City city) {
        String body = gson.toJson(city);
        File file = cityFile(city);

        this.inMemoryIdMap.put(city.getId(), city);
        this.inMemoryNameMap.put(city.getName(), city);

        return this.queryQueue.add(new FlatFileSaveTask(body, file));
    }

    @Override
    public boolean updateCity(City city) {
        // If name is changed remove it from the name map
        if (!this.inMemoryNameMap.containsKey(city.getName()))
        {
            String currentName = this.inMemoryIdMap.get(city.getId()).getName();
            this.inMemoryNameMap.remove(currentName);
        }

        this.inMemoryIdMap.put(city.getId(), city);
        this.inMemoryNameMap.put(city.getName(), city);

        // save file
        String body = gson.toJson(city);
        File file = cityFile(city);
        return this.queryQueue.add(new FlatFileSaveTask(body, file));
    }

    @Override
    public boolean removeCity(City city) {
        // remove from cache
        this.inMemoryIdMap.remove(city.getId());
        this.inMemoryNameMap.remove(city.getName());

        // delete file
        File deletedLocation = new File(DeletedCitiesFolder + File.separator + city.getId());
        File currentFile = cityFile(city);
        currentFile.renameTo(deletedLocation);
        currentFile.delete();

        return true;
    }
}
