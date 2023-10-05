package me.okay.minetwitch.customcommand;

import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.command.TabCompleter;

import me.okay.minetwitch.MinetwitchPlugin;

public abstract class CustomCommand extends CustomSubcommand implements CommandExecutor, TabCompleter {
    private PluginCommand command;

    public CustomCommand(MinetwitchPlugin plugin, String commandName) {
        super(plugin, commandName);
        
        command = plugin.getCommand(commandName);
        command.setExecutor(this);
    }

    public PluginCommand getCommand() {
        return command;
    }

    @Override
    public CommandResult run(CommandSender sender, CustomSubcommand command, String label, String[] args) {
        return CommandResult.USAGE_FAILURE;
    };

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {   
        super.onCommand(sender, this, label, args);

        return true;
    };

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        return tabComplete(sender, this, label, args);
    }

    @Override
    public List<String> tabComplete(CommandSender sender, CustomSubcommand command, String label, String[] args) {
        return super.tabComplete(sender, command, label, args);
    }
}
