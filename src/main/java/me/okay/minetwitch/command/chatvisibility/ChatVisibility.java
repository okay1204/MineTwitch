package me.okay.minetwitch.command.chatvisibility;

import me.okay.minetwitch.Minetwitch;
import me.okay.minetwitch.command.chatvisibility.exceptions.Exceptions;
import me.okay.minetwitch.customcommand.CustomSubcommand;

public class ChatVisibility extends CustomSubcommand {
    public ChatVisibility(Minetwitch plugin) {
        super(
            "chatVisibility",
            "Configure who can see the Twitch chat in Minecraft.",
            "minetwitch.chatvisibility"
        );

        addSubcommand(new BroadcastToEveryone(plugin));
        addSubcommand(new Exceptions(plugin));
    }
}
