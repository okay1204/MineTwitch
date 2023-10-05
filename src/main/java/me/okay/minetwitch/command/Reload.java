package me.okay.minetwitch.command;

import org.bukkit.command.CommandSender;

import me.okay.minetwitch.MinetwitchPlugin;
import me.okay.minetwitch.customcommand.CommandResult;
import me.okay.minetwitch.customcommand.CustomSubcommand;
import me.okay.minetwitch.utils.TextFormat;

public class Reload extends CustomSubcommand {
    private MinetwitchPlugin plugin;

    public Reload(MinetwitchPlugin plugin) {
        super(
            "reload",
            "Reloads the configuration file.",
            "minetwitch.reload"
        );

        this.plugin = plugin;
    }

    @Override
    public CommandResult run(CommandSender sender, CustomSubcommand command, String label, String[] args) {
        plugin.reloadConfig();
        plugin.onDisable();
        plugin.onEnable();
        sender.sendMessage(TextFormat.colorize("&aMinetwitch has been reloaded."));
        return CommandResult.FINISHED;
    };
}
