package me.okay.minetwitch.command;

import org.bukkit.command.CommandSender;

import me.okay.minetwitch.CommandResult;
import me.okay.minetwitch.CustomSubcommand;
import me.okay.minetwitch.Minetwitch;
import me.okay.minetwitch.utils.ColorFormat;

public class Reload extends CustomSubcommand {
    private Minetwitch plugin;

    public Reload(Minetwitch plugin) {
        super(
            "reload",
            "Reloads the configuration file.",
            "minetwitch.reload"
        );

        this.plugin = plugin;
    }

    @Override
    public CommandResult onRun(CommandSender sender, CustomSubcommand command, String label, String[] args) {
        plugin.reloadConfig();
        plugin.reloadTwitchBot();
        sender.sendMessage(ColorFormat.colorize("&aMinetwitch has been reloaded."));
        return CommandResult.SUCCESS;
    };
}
