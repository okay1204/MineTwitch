package me.okay.minetwitch.customcommand;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import me.okay.minetwitch.MinetwitchPlugin;
import me.okay.minetwitch.utils.TextFormat;

public abstract class CustomSubcommand {
    protected static final String NO_PERMS_MESSAGE = ChatColor.RED + "I'm sorry, but you do not have permission to perform this command. " +
    "Please contact the server administrators if you believe that this is a mistake.";
    private ArrayList<CustomSubcommand> subcommands = new ArrayList<CustomSubcommand>();
    private CustomSubcommand parent = null;

    private String name;
    private String description;
    private String permission;
    private String usage;
    private String permissionMessage;

    public CustomSubcommand(String name, String description, String permission) {
        this(name, description, permission, name);
    }

    public CustomSubcommand(String name, String description, String permission, String usage) {
        this(name, description, permission, usage, NO_PERMS_MESSAGE);
    }

    public CustomSubcommand(String name, String description, String permission, String usage, String permissionMessage) {
        this.name = name;
        this.description = description;
        this.permission = permission;
        this.usage = usage;
        this.permissionMessage = permissionMessage;
    }

    public CustomSubcommand(MinetwitchPlugin plugin, String name) {
        this.name = name;
        Command command = plugin.getCommand(name);
        this.description = command.getDescription();
        this.permission = command.getPermission();
        this.usage = command.getUsage();
        this.permissionMessage = command.getPermissionMessage() != null ? command.getPermissionMessage() : NO_PERMS_MESSAGE;
    }
    
    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getUsage() {
        return usage;
    }

    public String getFullUsage() {
        if (parent != null) {
            return parent.getFullUsage() + " " + usage;
        }
        else {
            return usage;
        }
    }

    public String getPermission() {
        return permission;
    }

    protected void addSubcommand(CustomSubcommand subcommand) {
        subcommands.add(subcommand);
        subcommand.setParent(this);
    }

    public List<CustomSubcommand> getSubcommands() {
        return subcommands;
    }

    public void setParent(CustomSubcommand parent) {
        this.parent = parent;
    }

    public CustomSubcommand getParent() {
        return parent;
    }

    public String getPermissionMessage() {
        return permissionMessage;
    }

    private String[] cutOffFirstArg(String[] args) {
        String[] newArgs = new String[args.length - 1];
        for (int i = 1; i < args.length; i++) {
            newArgs[i - 1] = args[i];
        }
        return newArgs;
    }

    public CommandResult onCommand(CommandSender sender, CustomSubcommand command, String label, String[] args) {
        if (subcommands.size() > 0) {
            if (args.length > 0) {
                for (CustomSubcommand subcommand : subcommands) {
                    if (subcommand.getName().equals(args[0])) {
                        return subcommand.onCommand(sender, this, label, cutOffFirstArg(args));
                    }
                }
            }

            CommandResult result = run(sender, command, label, args);
            if (result == CommandResult.USAGE_FAILURE) {
                // form a usage command that combines all subcommands
                String allUsages = TextFormat.colorize("&cUsages: \n");
                for (CustomSubcommand subcommand : subcommands) {
                    allUsages += subcommand.getFullUsage() + "\n";
                }
    
                sender.sendMessage(allUsages);
            }
            else if (result == CommandResult.PERMISSION_FAILURE) {
                sender.sendMessage(getPermissionMessage());
            }

            return result;
        }
        else {
            if (permission != null && !sender.hasPermission(permission)) {
                sender.sendMessage(getPermissionMessage());
                return CommandResult.PERMISSION_FAILURE;
            }
            else {
                CommandResult result = run(sender, command, label, args);
                if (result == CommandResult.USAGE_FAILURE) {
                    sender.sendMessage(TextFormat.colorize("&cUsage: ") + getFullUsage());
                }
                else if (result == CommandResult.PERMISSION_FAILURE) {
                    sender.sendMessage(getPermissionMessage());
                }

                return result;
            }
        }
    };

    public CommandResult run(CommandSender sender, CustomSubcommand command, String label, String[] args) {
        return CommandResult.USAGE_FAILURE;
    };

    public List<String> tabComplete(CommandSender sender, CustomSubcommand command, String label, String[] args) {
        if (getSubcommands().size() > 0) {
            List<String> tabCompleteStrings = new ArrayList<String>();

            if (args.length == 1) {
                for (CustomSubcommand subcommand : getSubcommands()) {
                    if (subcommand.getName().startsWith(args[0]) && sender.hasPermission(subcommand.getPermission())) {
                        tabCompleteStrings.add(subcommand.getName());
                    }
                }
            }
            else {
                for (CustomSubcommand subcommand : getSubcommands()) {
                    if (subcommand.getName().equals(args[0]) && sender.hasPermission(subcommand.getPermission())) {
                        tabCompleteStrings.addAll(subcommand.tabComplete(sender, this, label, cutOffFirstArg(args)));
                    }
                }
            }
            
            return tabCompleteStrings;
        }

        return List.of();
    }
}
