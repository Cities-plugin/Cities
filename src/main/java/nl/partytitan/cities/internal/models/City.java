package nl.partytitan.cities.internal.models;

import com.google.inject.Inject;
import nl.partytitan.cities.config.configs.MainConfig;
import nl.partytitan.cities.internal.models.base.Management;
import nl.partytitan.cities.internal.objects.Level;
import org.bukkit.OfflinePlayer;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

public class City extends Management {
    @Inject
    private MainConfig mainConfig;

    private UUID mayorId;
    private long foundedDate;

    private String homeBlockId;

    private List<UUID> residentIds = new ArrayList<>();
    private List<String> cityBlocks = new ArrayList<>();

    public City(UUID id, String name, OfflinePlayer owner) {
        super(id, name, owner);
        mayorId = owner.getUniqueId();
        residentIds.add(owner.getUniqueId());

        foundedDate = System.currentTimeMillis();
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

    public int residentcount() { return this.residentIds.size(); }

    public List<UUID> getResidentIds() { return this.residentIds; }

    public Level getLevel(){
        List<Level> levels = mainConfig.getCityLevels();
        return levels.stream().sorted(Comparator.comparingInt(Level::getMinPopulation)).filter(cl -> cl.getMinPopulation() >= residentcount()).findFirst().orElse(null);
    }

    public int claimCount(){
        return cityBlocks.size();
    }

    public int maxClaims(){
        return getLevel().getCityBlockLimit();
    }

    public int availableClaims(){
        return maxClaims() - claimCount();
    }

    public String getFormattedName(){
        Level level = getLevel();
        String preFix = level.hasTitlePreFix() ? level.getTitlePreFix() + " " : "";
        String postFix = level.hasTitlePostFix() ? " " + level.getTitlePostFix() : "";
        return preFix + getName() + postFix;
    }

    public UUID getMayorId() {
        return mayorId;
    }

    public void setMayorId(UUID mayorId) {
        this.mayorId = mayorId;
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

    public long getFoundedDate() {
        return foundedDate;
    }

    public String getHomeBlockId() {
        return homeBlockId;
    }

    public void setHomeBlockId(String homeBlockId) {
        this.homeBlockId = homeBlockId;
    }
}
