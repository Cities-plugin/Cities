package nl.partytitan.cities.internal.entities;

import com.google.inject.Inject;
import nl.partytitan.cities.internal.config.SettingsConfig;
import nl.partytitan.cities.internal.config.obj.Level;
import nl.partytitan.cities.internal.integrations.eco.interfaces.IEconomyRepository;
import javax.xml.stream.Location;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
public class City {
    private UUID id;
    private String name;
    private UUID mayorId;
    private String homeBlockId;
    private Location spawn;
    private List<UUID> residentIds = new ArrayList<>();
    private List<String> cityBlocks = new ArrayList<>();
    // virtual

    @Inject
    private transient SettingsConfig settings;

    @Inject
    private transient IEconomyRepository economyRepository;

    public City(UUID id, String name) {
        this.id = id;
        this.name = name;
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public UUID getMayorId() {
        return mayorId;
    }

    public void addResident(UUID redidentId){
        this.residentIds.add(redidentId);
    }

    public void removeResident(UUID redidentId){
        this.residentIds.remove(redidentId);
    }

    public boolean hasResident(UUID redidentId){
        return this.residentIds.contains(redidentId);
    }

    public Level getLevel(){
        List<Level> levels = settings.getCityLevels();
        return levels.stream().sorted(Comparator.comparingInt(Level::getMinPopulation)).filter(cl -> cl.getMinPopulation() > residentIds.size()).findFirst().orElse(null);
    }

    public String getFormattedName(){
        Level level = getLevel();
        String preFix = level.hasTitlePreFix() ? level.getTitlePreFix() + " " : "";
        String postFix = level.hasTitlePostFix() ? " " + level.getTitlePostFix() : "";
        return preFix + getName() + postFix;
    }

    public void setMayorId(UUID mayorId) {
        this.mayorId = mayorId;
    }
    public double getBalance(){
        return economyRepository.getBalance(getId());
    }

    public String getFormattedBalance() {
        return economyRepository.formatBalance(getBalance());
    }

    public List<String> getCityBlocks() {
        return cityBlocks;
    }

    public void addCityBlock(CityBlock cityBlock) {
        this.cityBlocks.add(cityBlock.getIdentifier());
    }

    public void removeCityBlock(CityBlock cityBlock) {
        this.cityBlocks.remove(cityBlock.getIdentifier());
    }
}
