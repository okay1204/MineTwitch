package me.okay.minetwitch.command.chatvisibility.exceptions;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.okay.minetwitch.Minetwitch;
import me.okay.minetwitch.customcommand.CommandResult;
import me.okay.minetwitch.customcommand.CustomSubcommand;
import me.okay.minetwitch.utils.ColorFormat;

public class ExceptionsAdd extends CustomSubcommand {
    private Minetwitch plugin;

    public ExceptionsAdd(Minetwitch plugin) {
        super(
            "add",
            "Add players to the visibility exceptions list.",
            "minetwitch.chatvisibility.exceptions.add",
            "add <player>"
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
            OfflinePlayer player = Bukkit.getOfflinePlayer(args[0]);
    
            List<String> exceptions = plugin.getConfig().getStringList("share-chat.twitch-to-minecraft.exceptions");
            
            if (exceptions.contains(player.getUniqueId().toString())) {
                sender.sendMessage(ColorFormat.colorize("&c" + player.getName() + " is already in the exceptions list."));
                return;
            }
    
            exceptions.add(player.getUniqueId().toString());
    
            plugin.getConfig().set("share-chat.twitch-to-minecraft.exceptions", exceptions);
            plugin.saveConfig();
    
            sender.sendMessage(ColorFormat.colorize("&e" + player.getName() + " &6has been added to the exceptions list."));
        });

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
