package me.okay.minetwitch.command;

import java.util.List;

import org.bukkit.command.CommandSender;

import me.okay.minetwitch.CommandResult;
import me.okay.minetwitch.CustomSubcommand;
import me.okay.minetwitch.Minetwitch;
import me.okay.minetwitch.utils.ColorFormat;

public class Broadcast extends CustomSubcommand {
    private Minetwitch plugin;

    public Broadcast(Minetwitch plugin) {
        super(
            "broadcast",
            "Enable/disable the broadcasting of twitch to minecraft chat or vice versa.",
            "minetwitch.broadcast",
            "broadcast <twitchToMinecraft|minecraftToTwitch> <true|false>"
        );

        this.plugin = plugin;
    }

    @Override
    public CommandResult onRun(CommandSender sender, CustomSubcommand command, String label, String[] args) {
        if (args.length < 2) {
            return CommandResult.USAGE_FAILURE;
        }

        boolean value;
        if (args[1].equalsIgnoreCase("true")) {
            value = true;
        }
        else if (args[1].equalsIgnoreCase("false")) {
            value = false;
        }
        else {
            return CommandResult.USAGE_FAILURE;
        }

        String formattedValue = value ? "&aenabled" : "&cdisabled";

        String option = args[0];
        if (option.equals("twitchToMinecraft")) {
            if (!sender.hasPermission("minetwitch.broadcast.twitch-to-minecraft")) {
                return CommandResult.PERMISSION_FAILURE;
            }
            
            plugin.getConfig().set("share-chat.twitch-to-minecraft.enabled", value);
            sender.sendMessage(ColorFormat.colorize("&6Twitch to Minecraft chat sharing has been " + formattedValue + "."));
        }
        else if (option.equals("minecraftToTwitch")) {
            if (!sender.hasPermission("minetwitch.broadcast.minecraft-to-twitch")) {
                return CommandResult.PERMISSION_FAILURE;
            }

            plugin.getConfig().set("share-chat.minecraft-to-twitch.enabled", value);
            sender.sendMessage(ColorFormat.colorize("&6Minecraft to Twitch chat sharing has been " + formattedValue + "."));
        }
        else {
            return CommandResult.USAGE_FAILURE;
        }
        plugin.saveConfig();

        return CommandResult.SUCCESS;
    };

    @Override
    public List<String> onTabComplete(CommandSender sender, CustomSubcommand command, String label, String[] args) {
        if (args.length == 1) {
            return List.of(args[0], "twitchToMinecraft", "minecraftToTwitch");
        }
        else if (args.length == 2) {
            return List.of(args[1], "true", "false");
        }

        return List.of();
    }
}
