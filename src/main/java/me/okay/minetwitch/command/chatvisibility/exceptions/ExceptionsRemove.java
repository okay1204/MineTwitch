package me.okay.minetwitch.command.chatvisibility.exceptions;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;

import me.okay.minetwitch.CommandResult;
import me.okay.minetwitch.CustomSubcommand;
import me.okay.minetwitch.Minetwitch;
import me.okay.minetwitch.utils.ColorFormat;

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
    @SuppressWarnings("deprecation")
    public CommandResult onRun(CommandSender sender, CustomSubcommand command, String label, String[] args) {
        if (args.length < 1) {
            return CommandResult.USAGE_FAILURE;
        }

        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            String playerName = args[0];
            OfflinePlayer player = Bukkit.getOfflinePlayer(playerName);
    
            List<String> exceptions = plugin.getConfig().getStringList("share-chat.twitch-to-minecraft.exceptions");
            
            boolean removed = false;
            if (exceptions.contains(playerName)) {
                exceptions.remove(playerName);
                removed = true;
            }
            if (exceptions.contains(player.getUniqueId().toString())) {
                exceptions.remove(player.getUniqueId().toString());
                removed = true;
            }
    
            if (!removed) {
                sender.sendMessage(ColorFormat.colorize("&c" + playerName + " is not in the exceptions list."));
                return;
            }
    
            plugin.getConfig().set("share-chat.twitch-to-minecraft.exceptions", exceptions);
            plugin.saveConfig();
    
            sender.sendMessage(ColorFormat.colorize("&e" + player.getName() + " &6has been removed from the exceptions list."));
        });

        return CommandResult.SUCCESS;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, CustomSubcommand command, String label, String[] args) {
        if (args.length == 1) {
            List<String> exceptions = plugin.getConfig().getStringList("share-chat.twitch-to-minecraft.exceptions");
            List<String> completions = new ArrayList<>();
            for (String exception : exceptions) {
                String name = exception;
                UUID uuid = null;
                try {
                    uuid = UUID.fromString(exception);
                }
                catch (IllegalArgumentException e) {
                }

                if (uuid != null) {
                    OfflinePlayer player = Bukkit.getOfflinePlayer(uuid);
                    if (player.getName() != null) {
                        name = player.getName();
                    }
                }

                if (name.toLowerCase().startsWith(args[0].toLowerCase())) {
                    completions.add(name);
                }
            }
            return completions;
        }
        else {
            return List.of();
        }
    }
}

