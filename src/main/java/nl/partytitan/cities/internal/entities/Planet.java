package nl.partytitan.cities.internal.entities;

import nl.partytitan.cities.internal.utils.TranslationUtil;

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

    public String getUnclaimedZoneName(){
        return customUnclaimedZoneName == null || customUnclaimedZoneName.length() == 0 ? TranslationUtil.of("planet_default_unclaimed_zone_name") : customUnclaimedZoneName;
    }

    public boolean isPVPEnabled() {
        return IsPVPEnabled;
    }

    public void setPVPEnabled(boolean PVPEnabled) {
        IsPVPEnabled = PVPEnabled;
    }
}
