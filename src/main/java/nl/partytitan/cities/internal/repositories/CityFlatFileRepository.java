package nl.partytitan.cities.internal.repositories;

import com.google.gson.Gson;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import nl.partytitan.cities.Cities;
import nl.partytitan.cities.internal.entities.City;
import nl.partytitan.cities.internal.repositories.interfaces.ICityRepository;
import nl.partytitan.cities.internal.tasks.FlatFileSaveTask;
import nl.partytitan.cities.internal.utils.FileUtils;

import java.io.File;
import java.util.List;
import java.util.UUID;

@Singleton
public class CityFlatFileRepository extends BaseRepository implements ICityRepository {

    private Gson gson;

    private File CitiesFolder;
    private File DeletedCitiesFolder;

    @Inject
    public CityFlatFileRepository(@Named("ConfigFolder") File dataFolder, Cities plugin){
        super(plugin);

        this.CitiesFolder = new File(dataFolder, "cities");
        this.DeletedCitiesFolder = new File(dataFolder, "cities" + File.separator + "deleted");

        FileUtils.checkOrCreateFolder(this.CitiesFolder);
        FileUtils.checkOrCreateFolder(this.DeletedCitiesFolder);

        this.gson = new Gson();
    }

    @Override
    public List<City> GetCities() {
        return null;
    }

    @Override
    public City GetCity(UUID id) {
        return null;
    }

    private File cityFile(City city) {
        return new File(CitiesFolder + File.separator + city.getId());
    }

    @Override
    public boolean CreateCity(City city) {
        String body = gson.toJson(city);
        return this.queryQueue.add(new FlatFileSaveTask(body, cityFile(city)));
    }

    @Override
    public boolean UpdateCity(City city) {
        return false;
    }

    @Override
    public boolean RemoveCity(City city) {
        return false;
    }
}
