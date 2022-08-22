package me.okay.minetwitch.command;

import org.bukkit.command.CommandSender;

import me.okay.minetwitch.CommandResult;
import me.okay.minetwitch.CustomCommand;
import me.okay.minetwitch.CustomSubcommand;
import me.okay.minetwitch.Minetwitch;
import me.okay.minetwitch.command.chatvisibility.ChatVisibility;

public class MinetwitchCommand extends CustomCommand {

    public MinetwitchCommand(Minetwitch plugin) {
        super(plugin, "minetwitch");

        addSubcommand(new Reload(plugin));
        addSubcommand(new Broadcast(plugin));
        addSubcommand(new ChatVisibility(plugin));
    }

    @Override
    public CommandResult onRun(CommandSender sender, CustomSubcommand command, String label, String[] args) {
        return CommandResult.USAGE_FAILURE;
    };
}
