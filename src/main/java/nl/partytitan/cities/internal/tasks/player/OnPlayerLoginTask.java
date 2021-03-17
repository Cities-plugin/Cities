package nl.partytitan.cities.internal.tasks.player;

import nl.partytitan.cities.Cities;
import org.bukkit.entity.Player;

public class OnPlayerLoginTask implements Runnable {

    private Cities plugin;
    private Player player;

    public OnPlayerLoginTask(Cities plugin, Player player){

        this.plugin = plugin;
        this.player = player;
    }

    @Override
    public void run() {

    }
}
