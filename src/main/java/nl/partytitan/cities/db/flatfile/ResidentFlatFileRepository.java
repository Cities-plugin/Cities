package nl.partytitan.cities.db.flatfile;

import com.google.gson.Gson;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import nl.partytitan.cities.Cities;
import nl.partytitan.cities.internal.entities.City;
import nl.partytitan.cities.internal.entities.Resident;
import nl.partytitan.cities.internal.repositories.BaseRepository;
import nl.partytitan.cities.internal.repositories.interfaces.IResidentRepository;
import nl.partytitan.cities.internal.tasks.FlatFileSaveTask;
import nl.partytitan.cities.internal.utils.server.FileUtils;

import java.io.File;
import java.util.*;
import java.util.stream.Collectors;

@Singleton
public class ResidentFlatFileRepository extends BaseRepository implements IResidentRepository {

    private Gson gson;

    private File ResidentsFolder;
    private File DeletedResidentsFolder;

    private Map<UUID, Resident> inMemoryIdMap;

    @Inject
    public ResidentFlatFileRepository(@Named("ConfigFolder") File dataFolder, Cities plugin, Gson gson){
        super(plugin);

        this.ResidentsFolder = new File(dataFolder, "residents");
        this.DeletedResidentsFolder = new File(dataFolder, "residents" + File.separator + "deleted");
        this.gson = gson;

        FileUtils.checkOrCreateFolder(this.ResidentsFolder);
        FileUtils.checkOrCreateFolder(this.DeletedResidentsFolder);

        inMemoryIdMap = getResidentsCache().stream().collect(Collectors.toMap(Resident::getUuid, res -> res));

    }

    private File residentFile(Resident resident) {
        return new File(ResidentsFolder + File.separator + resident.getUuid() + ".json");
    }

    private List<Resident> getResidentsCache() {
        File[] files = ResidentsFolder.listFiles();
        if (files == null || files.length == 0)
            return null;
        List<Resident> residents = new ArrayList();
        for (File file : files) {
            if(file.isFile()){
                Resident resident = gson.fromJson(FileUtils.convertFileToString(file), Resident.class);
                residents.add(resident);
            }
        }
        return residents;
    }


    @Override
    public List<Resident> getResidents() {
        return new ArrayList<Resident>(inMemoryIdMap.values());
    }

    @Override
    public List<Resident> getResidentsByCity(City city) {
        List<Resident> residents = new ArrayList<Resident>();
        for (UUID uuid : city.getResidentIds()) {
            residents.add(inMemoryIdMap.get(uuid));
        }
        return residents;
    }

    @Override
    public Resident getResident(UUID id) {
        return inMemoryIdMap.get(id);
    }

    @Override
    public boolean residentExists(UUID id) {
        return inMemoryIdMap.containsKey(id);
    }

    @Override
    public boolean createResident(Resident resident) {
        String body = gson.toJson(resident);

        this.inMemoryIdMap.put(resident.getUuid(), resident);

        return this.queryQueue.add(new FlatFileSaveTask(body, residentFile(resident)));
    }

    @Override
    public boolean updateResident(Resident resident) {
        String body = gson.toJson(resident);

        this.inMemoryIdMap.put(resident.getUuid(), resident);

        return this.queryQueue.add(new FlatFileSaveTask(body, residentFile(resident)));
    }

    @Override
    public boolean removeResident(Resident resident) {
        this.inMemoryIdMap.remove(resident.getUuid());

        File deletedLocation = new File(DeletedResidentsFolder + File.separator + resident.getUuid());
        File currentFile = residentFile(resident);
        currentFile.renameTo(deletedLocation);
        currentFile.delete();

        return false;
    }
}
