package me.okay.minetwitch.command;

import me.okay.minetwitch.CustomCommand;
import me.okay.minetwitch.Minetwitch;

public class MinetwitchCommand extends CustomCommand {

    public MinetwitchCommand(Minetwitch plugin) {
        super(plugin, "minetwitch");

        addSubcommand(new Reload(plugin));
    }
}
