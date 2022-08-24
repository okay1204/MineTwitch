package me.okay.minetwitch.command.chatvisibility.exceptions;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.okay.minetwitch.CommandResult;
import me.okay.minetwitch.CustomSubcommand;
import me.okay.minetwitch.Minetwitch;

public class ExceptionsList extends CustomSubcommand {
    private Minetwitch plugin;

    public ExceptionsList(Minetwitch plugin) {
        super(
            "list",
            "Lists players on the visibility exceptions list.",
            "minetwitch.chatvisibility.exceptions.list",
            "list [<page>]"
        );

        this.plugin = plugin;
    }

    @Override
    public CommandResult onRun(CommandSender sender, CustomSubcommand command, String label, String[] args) {
        return CommandResult.SUCCESS;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, CustomSubcommand command, String label, String[] args) {
        return List.of();
    }
}
