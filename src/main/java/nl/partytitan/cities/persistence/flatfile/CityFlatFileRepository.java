package nl.partytitan.cities.persistence.flatfile;

import com.google.gson.Gson;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import nl.partytitan.cities.internal.models.City;
import nl.partytitan.cities.internal.services.SchedulerService;
import nl.partytitan.cities.persistence.BaseRepository;
import nl.partytitan.cities.persistence.interfaces.ICityRepository;
import nl.partytitan.cities.tasks.FlatFileSaveTask;
import nl.partytitan.cities.internal.Constants;
import nl.partytitan.cities.utils.FileUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Singleton
public class CityFlatFileRepository extends BaseRepository implements ICityRepository {
    private Gson gson;

    private File citiesFolder;
    private File deletedCitiesFolder;

    private Map<UUID, City> inMemoryIdMap;
    private Map<String, City> inMemoryNameMap;

    public CityFlatFileRepository(Gson gson, SchedulerService schedulerService){
        super(schedulerService);

        this.gson = gson;

        this.citiesFolder = new File(Constants.CITIES_PATH);
        this.deletedCitiesFolder = new File(Constants.CITIES_PATH + File.separator + "deleted");

        FileUtils.checkOrCreateFolder(this.citiesFolder);
        FileUtils.checkOrCreateFolder(this.deletedCitiesFolder);

        // flatfile makes filtering by property impossible so we use a cache
        List<City> cachedCities = getCitiesCache();
        inMemoryIdMap = cachedCities.stream().collect(Collectors.toMap(City::getId, city -> city));
        inMemoryNameMap = cachedCities.stream().collect(Collectors.toMap(City::getName, city -> city));
    }

    private List<City> getCitiesCache() {
        File[] files = citiesFolder.listFiles();
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
        return new ArrayList<City>(inMemoryIdMap.values());
    }

    @Override
    public City getCity(UUID id) {
        return inMemoryIdMap.get(id);
    }

    @Override
    public City getCity(String cityName) {
        return inMemoryNameMap.get(cityName);
    }

    private File cityFile(City city) {
        return new File(citiesFolder + File.separator + city.getId() + ".json");
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
        File deletedLocation = new File(deletedCitiesFolder + File.separator + city.getId());
        File currentFile = cityFile(city);
        currentFile.renameTo(deletedLocation);
        currentFile.delete();

        return true;
    }
}
