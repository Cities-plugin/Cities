package nl.partytitan.cities.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.Subcommand;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;

public class ConfirmationCommand extends BaseCommand {
    @Subcommand("confirm")
    public void confirm(@Nonnull Player sender){

    }

    @Subcommand("cancel")
    public void cancel(@Nonnull Player sender){

    }
}
