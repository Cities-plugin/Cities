package nl.partytitan.cities.internal.repositories;

import com.google.gson.Gson;
import com.google.inject.Inject;
import com.google.inject.name.Named;
import nl.partytitan.cities.Cities;
import nl.partytitan.cities.internal.entities.City;
import nl.partytitan.cities.internal.entities.Resident;
import nl.partytitan.cities.internal.repositories.interfaces.IResidentRepository;
import nl.partytitan.cities.internal.tasks.FlatFileSaveTask;
import nl.partytitan.cities.internal.utils.FileUtils;

import java.io.File;
import java.util.*;

public class ResidentFlatFileRepository extends BaseRepository implements IResidentRepository {

    private Gson gson;

    private File ResidentsFolder;
    private File DeletedResidentsFolder;

    @Inject
    public ResidentFlatFileRepository(@Named("ConfigFolder") File dataFolder, Cities plugin){
        super(plugin);

        this.ResidentsFolder = new File(dataFolder, "residents");
        this.DeletedResidentsFolder = new File(dataFolder, "residents" + File.separator + "deleted");

        FileUtils.checkOrCreateFolder(this.ResidentsFolder);
        FileUtils.checkOrCreateFolder(this.DeletedResidentsFolder);

        this.gson = new Gson();
    }

    private File residentFile(UUID uuid) {
        return new File(ResidentsFolder + File.separator + uuid);
    }

    private File residentFile(Resident resident) {
        return new File(ResidentsFolder + File.separator + resident.getUuid());
    }

    @Override
    public List<Resident> GetResidents() {
        File[] files = ResidentsFolder.listFiles();
        List<Resident> residents = null;
        for (File file : files) {
            residents.add(gson.fromJson(FileUtils.convertFileToString(file), Resident.class));
        }
        return residents;
    }

    @Override
    public Resident GetResident(UUID id) {
        File file = residentFile(id);
        if(file.exists() && file.isFile()){
            return gson.fromJson(FileUtils.convertFileToString(file), Resident.class);
        }
        return null;
    }

    @Override
    public boolean ResidentExists(UUID id) {
        return residentFile(id).exists();
    }

    @Override
    public boolean ResidentExists(String playerName) {
        return false;
    }

    @Override
    public boolean CreateResident(Resident resident) {
        String body = gson.toJson(resident);
        return this.queryQueue.add(new FlatFileSaveTask(body, residentFile(resident)));
    }

    @Override
    public boolean UpdateResident(Resident resident) {
        return false;
    }

    @Override
    public boolean RemoveResident(Resident resident) {
        return false;
    }
}
