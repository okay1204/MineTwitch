package me.okay.minetwitch.command;

import me.okay.minetwitch.MinetwitchPlugin;
import me.okay.minetwitch.customcommand.CustomCommand;

public class MinetwitchCommand extends CustomCommand {

    public MinetwitchCommand(MinetwitchPlugin plugin) {
        super(plugin, "minetwitch");

        addSubcommand(new Reload(plugin));
        addSubcommand(new Broadcast(plugin));
        addSubcommand(new Link(plugin));
        addSubcommand(new Unlink(plugin));
    }
}
