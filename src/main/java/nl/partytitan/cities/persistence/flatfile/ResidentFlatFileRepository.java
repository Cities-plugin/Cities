package nl.partytitan.cities.persistence.flatfile;

import com.google.gson.Gson;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import nl.partytitan.cities.internal.models.City;
import nl.partytitan.cities.internal.models.Resident;
import nl.partytitan.cities.internal.services.SchedulerService;
import nl.partytitan.cities.persistence.BaseRepository;
import nl.partytitan.cities.persistence.interfaces.IResidentRepository;
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
public class ResidentFlatFileRepository extends BaseRepository implements IResidentRepository {
    private Gson gson;

    private File residentsFolder;
    private File deletedResidentsFolder;

    private Map<UUID, Resident> inMemoryIdMap;

    @Inject
    public ResidentFlatFileRepository(Gson gson, SchedulerService schedulerService){
        super(schedulerService);

        this.gson = gson;

        this.residentsFolder = new File(Constants.RESIDENTS_PATH);
        this.deletedResidentsFolder = new File(Constants.RESIDENTS_PATH + File.separator + "deleted");

        FileUtils.checkOrCreateFolder(this.residentsFolder);
        FileUtils.checkOrCreateFolder(this.deletedResidentsFolder);

        inMemoryIdMap = getResidentsCache().stream().collect(Collectors.toMap(Resident::getUuid, res -> res));

    }

    private File residentFile(Resident resident) {
        return new File(residentsFolder + File.separator + resident.getUuid() + ".json");
    }

    private List<Resident> getResidentsCache() {
        File[] files = residentsFolder.listFiles();
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

        File deletedLocation = new File(deletedResidentsFolder + File.separator + resident.getUuid());
        File currentFile = residentFile(resident);
        currentFile.renameTo(deletedLocation);
        currentFile.delete();

        return false;
    }
}
