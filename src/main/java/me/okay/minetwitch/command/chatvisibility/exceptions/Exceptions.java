package me.okay.minetwitch.command.chatvisibility.exceptions;

import me.okay.minetwitch.CustomSubcommand;
import me.okay.minetwitch.Minetwitch;

public class Exceptions extends CustomSubcommand {
    public Exceptions(Minetwitch plugin) {
        super(
            "exceptions",
            "Manage the visibility exceptions list.",
            "minetwitch.chatvisibility.exceptions"
        );

        addSubcommand(new ExceptionsAdd(plugin));
        addSubcommand(new ExceptionsRemove(plugin));
        addSubcommand(new ExceptionsList(plugin));
        addSubcommand(new ExceptionsClear(plugin));
    }
}
