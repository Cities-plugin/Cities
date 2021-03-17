package nl.partytitan.cities.internal.valueobjects;

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

    public int getX() {

        return x;
    }

    public int getZ() {

        return z;
    }

    public String getWorldName() {

        return worldName;
    }
}
