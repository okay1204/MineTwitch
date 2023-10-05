package me.okay.minetwitch.command;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.okay.minetwitch.MinetwitchPlugin;
import me.okay.minetwitch.customcommand.CommandResult;
import me.okay.minetwitch.customcommand.CustomSubcommand;
import me.okay.minetwitch.utils.TextFormat;

public class Unlink extends CustomSubcommand {
    private MinetwitchPlugin plugin;

    public Unlink(MinetwitchPlugin plugin) {
        super(
            "unlink",
            "Unlinks your Twitch account from your Minecraft account.",
            "minetwitch.link"
        );

        this.plugin = plugin;
    }
    
    @Override
    public CommandResult run(CommandSender sender, CustomSubcommand command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(TextFormat.colorize("&cYou must be a player to use this command."));
            return CommandResult.FINISHED;
        }

        Player player = (Player) sender;

        // Check if the player is not linked
        if (plugin.getLinkedAccountsManager().getTwitchId(player.getUniqueId()) == null) {
            sender.sendMessage(TextFormat.colorize("&cYou are not linked to a Twitch account!"));
            return CommandResult.FINISHED;
        }

        // Unlink the player
        plugin.getLinkedAccountsManager().setLinkedAccounts(player.getUniqueId(), null);
        sender.sendMessage(TextFormat.colorize("&aYou have successfully unlinked your Twitch account!"));
            
        return CommandResult.FINISHED;
    }
}
