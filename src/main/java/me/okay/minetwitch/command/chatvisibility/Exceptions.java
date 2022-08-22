package me.okay.minetwitch.command.chatvisibility;

import org.bukkit.command.CommandSender;

import me.okay.minetwitch.CommandResult;
import me.okay.minetwitch.CustomSubcommand;
import me.okay.minetwitch.Minetwitch;

public class Exceptions extends CustomSubcommand {
    private Minetwitch plugin;

    public Exceptions(Minetwitch plugin) {
        super(
            "exceptions",
            "Add or remove players from the visibility exceptions list.",
            "minetwitch.chatvisibility.exceptions",
            "exceptions <add|remove|clear> <player>"
        );

        this.plugin = plugin;
    }

    @Override
    public CommandResult onRun(CommandSender sender, CustomSubcommand command, String label, String[] args) {
        return CommandResult.SUCCESS;
    }
}
