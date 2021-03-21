package nl.partytitan.cities.internal.tasks.player;

import com.google.inject.Inject;
import nl.partytitan.cities.Cities;
import nl.partytitan.cities.internal.entities.Resident;
import nl.partytitan.cities.internal.repositories.interfaces.IResidentRepository;
import org.bukkit.entity.Player;

import java.util.UUID;

public class OnPlayerLoginTask implements Runnable {

    private Cities plugin;
    private Player player;
    private IResidentRepository residentRepository;

    private UUID playerId;

    public OnPlayerLoginTask(Cities plugin, Player player, IResidentRepository residentRepository){

        this.plugin = plugin;
        this.player = player;
        this.playerId = player.getUniqueId();
        this.residentRepository = residentRepository;
    }

    @Override
    public void run() {
        if(residentRepository.residentExists(playerId)){
            Resident resident = residentRepository.getResident(playerId);

            resident.setLastOnline(System.currentTimeMillis());

            residentRepository.updateResident(resident);
        } else {
            // New resident
            registerNewPlayer();
        }
    }

    private void registerNewPlayer() {
        Resident resident = new Resident(playerId, player.getName());

        resident.setRegistered(System.currentTimeMillis());
        resident.setLastOnline(System.currentTimeMillis());

        residentRepository.createResident(resident);
    }
}
