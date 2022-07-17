package nl.partytitan.cities.db.flatfile;

import com.google.gson.Gson;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import nl.partytitan.cities.Cities;
import nl.partytitan.cities.internal.entities.CityBlock;
import nl.partytitan.cities.internal.repositories.BaseRepository;
import nl.partytitan.cities.internal.repositories.interfaces.ICityBlockRepository;
import nl.partytitan.cities.internal.tasks.FlatFileSaveTask;
import nl.partytitan.cities.internal.utils.server.FileUtils;
import nl.partytitan.cities.internal.valueobjects.Coord;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Singleton
public class CityBlockFlatFileRepository extends BaseRepository implements ICityBlockRepository {

    private Gson gson;

    private File planetsFolder;
    private File deletedPlanetsFolder;

    private Map<Coord, CityBlock> inMemoryCoordMap;

    @Inject
    public CityBlockFlatFileRepository(@Named("ConfigFolder") File dataFolder, Cities plugin, Gson gson){
        super(plugin);

        this.planetsFolder = new File(dataFolder, "planets");
        this.deletedPlanetsFolder = new File(dataFolder, "planets" + File.separator + "deleted");
        this.gson = gson;

        FileUtils.checkOrCreateFolder(this.planetsFolder);
        FileUtils.checkOrCreateFolder(this.deletedPlanetsFolder);

        List<CityBlock> cachedCities = getCityBlocksCache();
        inMemoryCoordMap = cachedCities.stream().collect(Collectors.toMap(CityBlock::getCoord, cityBlock -> cityBlock));
    }

    private List<CityBlock> getCityBlocksCache() {
        File[] planetFiles = planetsFolder.listFiles();
        if (planetFiles == null || planetFiles.length == 0)
            return null;

        List<CityBlock> cityBlocks = new ArrayList();
        for (File planetFile : planetFiles) {
            if(planetFile.isFile())
                continue;

            File[] cityBlockFiles = planetFile.listFiles();
            if (cityBlockFiles == null || cityBlockFiles.length == 0)
                continue;

            for (File cityBlockFile : cityBlockFiles) {
                if(cityBlockFile.isFile()){
                    CityBlock cityBlock = gson.fromJson(FileUtils.convertFileToString(cityBlockFile), CityBlock.class);
                    cityBlocks.add(cityBlock);
                }
            }
        }
        return cityBlocks;
    }

    @Override
    public List<CityBlock> getCityBlocks() {
        return new ArrayList<CityBlock>(inMemoryCoordMap.values());
    }

    @Override
    public CityBlock getCityBlock(Coord coord) {
        return inMemoryCoordMap.get(coord);
    }

    @Override
    public boolean cityBlockExists(Coord coord) {
        return inMemoryCoordMap.containsKey(coord);
    }

    private File cityBlockFile(CityBlock cityBlock) {
        return new File(planetsFolder + File.separator + cityBlock.getPlanetName() + File.separator + cityBlock.getIdentifier() + ".json");
    }

    private File cityBlockPlanetDirectory(CityBlock cityBlock) {
        return new File(planetsFolder + File.separator + cityBlock.getPlanetName());
    }

    @Override
    public boolean createCityBlock(CityBlock cityBlock) {
        String body = gson.toJson(cityBlock);
        File file = cityBlockFile(cityBlock);

        FileUtils.checkOrCreateFolder(cityBlockPlanetDirectory(cityBlock));

        this.inMemoryCoordMap.put(cityBlock.getCoord(), cityBlock);

        return this.queryQueue.add(new FlatFileSaveTask(body, file));
    }

    @Override
    public boolean updateCityBlock(CityBlock cityBlock) {
        this.inMemoryCoordMap.put(cityBlock.getCoord(), cityBlock);

        // save file
        String body = gson.toJson(cityBlock);
        File file = cityBlockFile(cityBlock);
        return this.queryQueue.add(new FlatFileSaveTask(body, file));
    }

    @Override
    public boolean removeCityBlock(CityBlock cityBlock) {
        // remove from cache
        this.inMemoryCoordMap.remove(cityBlock.getCoord());

        // delete file
        File deletedLocation = new File(deletedPlanetsFolder + File.separator + cityBlock.getPlanetName() + File.separator + cityBlock.getX() + "_" + cityBlock.getZ());
        File currentFile = cityBlockFile(cityBlock);
        currentFile.renameTo(deletedLocation);
        currentFile.delete();

        return true;
    }
}
