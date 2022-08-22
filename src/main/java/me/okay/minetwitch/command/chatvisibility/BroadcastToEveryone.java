package me.okay.minetwitch.command.chatvisibility;

import java.util.List;

import org.bukkit.command.CommandSender;

import me.okay.minetwitch.CommandResult;
import me.okay.minetwitch.CustomSubcommand;
import me.okay.minetwitch.Minetwitch;
import me.okay.minetwitch.utils.ColorFormat;

public class BroadcastToEveryone extends CustomSubcommand {
    private Minetwitch plugin;

    public BroadcastToEveryone(Minetwitch plugin) {
        super(
            "broadcastToEveryone",
            "Enable/disable whether or not everyone can see Twitch chat in Minecraft.",
            "minetwitch.chatvisibility.broadcast-to-everyone",
            "broadcastToEveryone <true|false>"
        );

        this.plugin = plugin;
    }

    @Override
    public CommandResult onRun(CommandSender sender, CustomSubcommand command, String label, String[] args) {
        if (args.length < 1) {
            return CommandResult.USAGE_FAILURE;
        }

        boolean value;
        if (args[0].equalsIgnoreCase("true")) {
            value = true;
        }
        else if (args[0].equalsIgnoreCase("false")) {
            value = false;
        }
        else {
            return CommandResult.USAGE_FAILURE;
        }

        plugin.getConfig().set("share-chat.twitch-to-minecraft.broadcast-to-everyone", value);
        plugin.saveConfig();
        sender.sendMessage(ColorFormat.colorize("&6Everyone can " + (value ? "&anow" : "&cno longer") + " see &6Twitch chat in Minecraft."));

        return CommandResult.SUCCESS;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, CustomSubcommand command, String label, String[] args) {
        if (args.length == 1) {
            return List.of("true", "false");
        }
        else {
            return List.of();
        }
    }
}
