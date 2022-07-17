package nl.partytitan.cities.internal.utils.server;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.bukkit.OfflinePlayer;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.Collection;
import java.util.UUID;

@Singleton
public class ServerUtils {

    @Inject
    private static Server server;

    public static Player getPlayer(UUID uuid) {
        return getOfflinePlayer(uuid).getPlayer();
    }

    public static OfflinePlayer getOfflinePlayer(UUID uuid){
        return server.getOfflinePlayer(uuid);
    }
    public static World getWorld(String worldName){
        return server.getWorld(worldName);
    }

    public static boolean isPrimaryThread(){
        return server.isPrimaryThread();
    }
}
