package nl.partytitan.cities.internal.repositories.interfaces;

import nl.partytitan.cities.internal.entities.Resident;

import java.util.List;
import java.util.UUID;

public interface IResidentRepository {
    List<Resident> GetResidents();
    Resident GetResident(UUID id);
    boolean ResidentExists(UUID id);
    boolean ResidentExists(String playerName);
    boolean CreateResident(Resident resident);
    boolean UpdateResident(Resident resident);
    boolean RemoveResident(Resident resident);
}
