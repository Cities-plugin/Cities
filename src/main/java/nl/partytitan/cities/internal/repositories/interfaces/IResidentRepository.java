package nl.partytitan.cities.internal.repositories.interfaces;

import nl.partytitan.cities.internal.entities.Resident;

import java.util.List;
import java.util.UUID;

public interface IResidentRepository {
    List<Resident> getResidents();
    Resident getResident(UUID id);
    boolean residentExists(UUID id);
    boolean createResident(Resident resident);
    boolean updateResident(Resident resident);
    boolean removeResident(Resident resident);
}
