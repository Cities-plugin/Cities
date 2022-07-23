package nl.partytitan.cities.internal.models;

public class Planet {
    private String Name;
    private boolean CitiesAllowedInWorld = true;
    private boolean IsPVPEnabled = true;

    private String customUnclaimedZoneName;

    public Planet(String name) {
        Name = name;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public boolean isCitiesAllowedInWorld() {
        return CitiesAllowedInWorld;
    }

    public void setCitiesAllowedInWorld(boolean citiesAllowedInWorld) {
        CitiesAllowedInWorld = citiesAllowedInWorld;
    }

    public boolean isPVPEnabled() {
        return IsPVPEnabled;
    }

    public void setPVPEnabled(boolean PVPEnabled) {
        IsPVPEnabled = PVPEnabled;
    }
}
