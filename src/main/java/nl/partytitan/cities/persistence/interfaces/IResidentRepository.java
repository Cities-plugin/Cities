package nl.partytitan.cities.persistence.interfaces;

import nl.partytitan.cities.internal.models.City;
import nl.partytitan.cities.internal.models.Resident;

import java.util.List;
import java.util.UUID;

public interface IResidentRepository {
    List<Resident> getResidents();
    List<Resident> getResidentsByCity(City city);
    Resident getResident(UUID id);
    boolean residentExists(UUID id);
    boolean createResident(Resident resident);
    boolean updateResident(Resident resident);
    boolean removeResident(Resident resident);
}

