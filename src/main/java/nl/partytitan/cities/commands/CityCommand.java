package nl.partytitan.cities.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import com.google.inject.Inject;
import nl.partytitan.cities.Cities;
import nl.partytitan.cities.internal.config.SettingsConfig;
import nl.partytitan.cities.internal.entities.City;
import nl.partytitan.cities.internal.entities.Resident;
import nl.partytitan.cities.internal.integrations.eco.interfaces.IEconomyRepository;
import nl.partytitan.cities.internal.repositories.interfaces.ICityBlockRepository;
import nl.partytitan.cities.internal.repositories.interfaces.ICityRepository;
import nl.partytitan.cities.internal.repositories.interfaces.IPlanetRepository;
import nl.partytitan.cities.internal.repositories.interfaces.IResidentRepository;
import nl.partytitan.cities.internal.utils.MessageUtil;
import nl.partytitan.cities.internal.utils.TranslationUtil;
import nl.partytitan.cities.messageformats.CityFormatter;
import nl.partytitan.cities.services.interfaces.ICityService;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@CommandAlias("c|city")
@Description("The command to create and manage your city")
public class CityCommand extends BaseCommand {

    private ICityRepository cityRepository;
    private IResidentRepository residentRepository;
    private IPlanetRepository planetRepository;
    private ICityBlockRepository cityBlockRepository;
    private IEconomyRepository economyRepository;
    private SettingsConfig settings;

    private ICityService cityService;

    @Inject
    public CityCommand(Cities plugin, ICityRepository cityRepository, IResidentRepository residentRepository, IPlanetRepository planetRepository, ICityBlockRepository cityBlockRepository, IEconomyRepository economyRepository, SettingsConfig settings, ICityService cityService){
        this.cityRepository = cityRepository;
        this.residentRepository = residentRepository;
        this.planetRepository = planetRepository;
        this.cityBlockRepository = cityBlockRepository;
        this.economyRepository = economyRepository;
        this.settings = settings;
        this.cityService = cityService;

        plugin.getCommandManager().getCommandCompletions().registerAsyncCompletion("cities", c -> {
            List<City> cities = cityRepository.getCities();
            if(cities == null || cities.isEmpty())
                return Collections.emptyList();

            return cities.stream()
                    .map(city -> city.getName())
                    .collect(Collectors.toList());
        });
    }

    @Default
    @CommandCompletion("@cities")

    public void city(@Nonnull Player sender, @Optional String cityName) {
        City city = null;
        if(cityName == null || cityName.isEmpty()){
            // Getting own city
            Resident resident = residentRepository.getResident(sender.getUniqueId());
            if(!resident.hasCity()){
                MessageUtil.sendErrorMsg(sender, TranslationUtil.of("resident_err_no_city"));
                return;
            }
            city = resident.getCity();
        } else {
            // Getting specified city
            city = cityRepository.getCity(cityName);
        }

        // City does not exist
        if(city == null){
            MessageUtil.sendErrorMsg(sender, TranslationUtil.of("city_err_not_found"));
            return;
        }

        // Generate status screen
        sender.sendMessage(CityFormatter.getDetails(city));
    }

    @Subcommand("deposit")
    public void deposit(@Nonnull Player sender, double amount){
        Resident resident = residentRepository.getResident(sender.getUniqueId());

        // has no city
        if(!resident.hasCity())
        {
            MessageUtil.sendErrorMsg(sender, TranslationUtil.of("city_err_not_found"));
            return;
        }

        if(!economyRepository.hasBalance(sender, amount)){
            MessageUtil.sendErrorMsg(sender, TranslationUtil.of("resident_err_insuf_funds"));
            return;
        }

        City city = resident.getCity();

        economyRepository.subtract(sender, amount);
        economyRepository.add(city.getId(), amount);

        MessageUtil.sendCityMessage(city, TranslationUtil.of("city_deposit_success", resident.getUsername(), economyRepository.formatBalance(amount)));
    }

    @Subcommand("create")
    public void create(@Nonnull Player sender, String name) {
        cityService.createCity(name, sender);
    }

    @Subcommand("claim")
    public void claim(@Nonnull Player sender) {
        if(!planetRepository.getPlanet(sender.getWorld().getName()).isCitiesAllowedInWorld()){
            MessageUtil.sendErrorMsg(sender, TranslationUtil.of("planet_err_cities_not_enabled"));
            return;
        }

        Resident resident = residentRepository.getResident(sender.getUniqueId());


        // has no city
        if(!resident.hasCity())
        {
            MessageUtil.sendErrorMsg(sender, TranslationUtil.of("city_err_not_found"));
            return;
        }

        City city = resident.getCity();

        cityService.claimChunk(sender, city, resident);
    }
}
