package me.okay.minetwitch.command.chatvisibility.exceptions;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.okay.minetwitch.CommandResult;
import me.okay.minetwitch.CustomSubcommand;
import me.okay.minetwitch.Minetwitch;

public class ExceptionsRemove extends CustomSubcommand {
    private Minetwitch plugin;

    public ExceptionsRemove(Minetwitch plugin) {
        super(
            "remove",
            "Remove players from the visibility exceptions list.",
            "minetwitch.chatvisibility.exceptions.remove",
            "remove <player>"
        );

        this.plugin = plugin;
    }

    @Override
    public CommandResult onRun(CommandSender sender, CustomSubcommand command, String label, String[] args) {
        return CommandResult.SUCCESS;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, CustomSubcommand command, String label, String[] args) {
        if (args.length == 1) {
            ArrayList<String> playerNames = new ArrayList<>();
            for (Player player : Bukkit.getOnlinePlayers()) {
                if (player.getName().toLowerCase().startsWith(args[0].toLowerCase())) {
                    playerNames.add(player.getName());
                }
            }

            return playerNames;
        }
        else {
            return List.of();
        }
    }
}

