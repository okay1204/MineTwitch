package me.okay.minetwitch.command.chatvisibility.exceptions;

import org.bukkit.command.CommandSender;

import me.okay.minetwitch.CommandResult;
import me.okay.minetwitch.CustomSubcommand;
import me.okay.minetwitch.Minetwitch;

public class ExceptionsClear extends CustomSubcommand {
    private Minetwitch plugin;

    public ExceptionsClear(Minetwitch plugin) {
        super(
            "clear",
            "Clear visibility exceptions list.",
            "minetwitch.chatvisibility.exceptions.clear"
        );

        this.plugin = plugin;
    }

    @Override
    public CommandResult onRun(CommandSender sender, CustomSubcommand command, String label, String[] args) {
        return CommandResult.SUCCESS;
    }
}
