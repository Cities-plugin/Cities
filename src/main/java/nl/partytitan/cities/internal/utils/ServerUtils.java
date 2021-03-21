package nl.partytitan.cities.internal.utils;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.bukkit.OfflinePlayer;
import org.bukkit.Server;
import org.bukkit.entity.Player;

import java.util.Collection;
import java.util.UUID;

@Singleton
public class ServerUtils {

    @Inject
    private static Server server;

    public static Player getPlayer(UUID uuid) {
        OfflinePlayer test = server.getOfflinePlayer(uuid);
        Player test2 = test.getPlayer();
        return server.getOfflinePlayer(uuid).getPlayer();
    }

    public static boolean isPrimaryThread(){
        return server.isPrimaryThread();
    }
}
