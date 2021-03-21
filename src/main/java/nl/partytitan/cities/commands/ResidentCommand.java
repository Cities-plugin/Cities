package nl.partytitan.cities.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.CommandHelp;
import co.aikar.commands.annotation.*;
import com.google.inject.Inject;
import nl.partytitan.cities.internal.entities.Resident;
import nl.partytitan.cities.internal.repositories.interfaces.IResidentRepository;
import nl.partytitan.cities.messageformats.ResidentFormatter;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;
import java.util.UUID;

@CommandAlias("res|resident")
@Description("The command to see information about a resident")
public class ResidentCommand extends BaseCommand {

    @Inject
    IResidentRepository residentRepository;

    @Default
    @CommandCompletion("@players")
    public void player(@Nonnull Player sender, @Optional OfflinePlayer player) {
        UUID residentId = player == null ? sender.getUniqueId() : player.getUniqueId();

        Resident resident = residentRepository.getResident(residentId);
        sender.sendMessage(ResidentFormatter.getDetails(resident, sender));
    }
}
