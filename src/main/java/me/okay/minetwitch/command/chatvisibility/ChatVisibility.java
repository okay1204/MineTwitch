package me.okay.minetwitch.command.chatvisibility;

import me.okay.minetwitch.CustomSubcommand;
import me.okay.minetwitch.Minetwitch;

public class ChatVisibility extends CustomSubcommand {
    public ChatVisibility(Minetwitch plugin) {
        super(
            "chatvisibility",
            "Configure who can see the Twitch chat in Minecraft.",
            "minetwitch.chatvisibility"
        );

        addSubcommand(new BroadcastToEveryone(plugin));
        addSubcommand(new Exceptions(plugin));
    }
}
