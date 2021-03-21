package nl.partytitan.cities.internal.valueobjects;

import org.bukkit.Chunk;
import org.bukkit.Location;

import java.util.Objects;

public class Coord {
    private final int x;
    private final int z;
    private final String worldName;

    public Coord(int x, int z, String worldName) {
        this.x = x;
        this.z = z;
        this.worldName = worldName;
    }

    public Coord(Coord coord) {

        this.x = coord.getX();
        this.z = coord.getZ();
        this.worldName = coord.getWorldName();
    }

    public static Coord parseCoord(Chunk chunk){
        return new Coord(chunk.getX(), chunk.getZ(), chunk.getWorld().getName());
    }

    public int getX() {

        return x;
    }

    public int getZ() {

        return z;
    }

    public String getWorldName() {

        return worldName;
    }
    @Override
    public int hashCode() {

        int hash = 17;
        hash = hash * 27 + (worldName == null ? 0 : worldName.hashCode());
        hash = hash * 27 + getX();
        hash = hash * 27 + getZ();
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this)
            return true;
        if (!(obj instanceof Coord))
            return false;

        if (!(obj instanceof Coord)) {
            Coord that = (Coord) obj;
            return this.getX() == that.getZ() && this.getZ() == that.getZ();
        }

        Coord that = (Coord) obj;
        return this.getX() == that.getX() && this.getZ() == that.getZ() && (Objects.equals(this.worldName, that.worldName));
    }

}
