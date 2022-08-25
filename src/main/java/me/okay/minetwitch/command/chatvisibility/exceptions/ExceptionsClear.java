package me.okay.minetwitch.command.chatvisibility.exceptions;

import java.util.List;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.okay.minetwitch.CommandResult;
import me.okay.minetwitch.CustomSubcommand;
import me.okay.minetwitch.Minetwitch;
import me.okay.minetwitch.utils.ColorFormat;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.hover.content.Text;

public class ExceptionsClear extends CustomSubcommand {
    private Minetwitch plugin;

    public ExceptionsClear(Minetwitch plugin) {
        super(
            "clear",
            "Clear visibility exceptions list.",
            "minetwitch.chatvisibility.exceptions.clear"
        );

        this.plugin = plugin;
    }

    @Override
    public CommandResult onRun(CommandSender sender, CustomSubcommand command, String label, String[] args) {
        if (args.length < 1 || !args[0].equals("confirm")) {
            if (sender instanceof Player) {
                TextComponent message = new TextComponent(ColorFormat.colorize("&6Are you sure you want to clear the chat visibility exceptions list? This cannot be undone. "));

                TextComponent confirmButton = new TextComponent(ColorFormat.colorize("&a&l[Click to confirm]"));
                confirmButton.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(ColorFormat.colorize("&aClick to confirm. &cThis cannot be undone."))));
                confirmButton.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/minetwitch:minetwitch chatVisibility exceptions clear confirm"));
                sender.spigot().sendMessage(message, confirmButton);
            }
            else {
                sender.sendMessage(ColorFormat.colorize("&6Are you sure you want to clear all global coordinates? This cannot be undone. &7(/minetwitch:minetwitch chatVisibility exceptions clear confirm)"));
            }
        }
        else {
            plugin.getConfig().set("share-chat.twitch-to-minecraft.exceptions", List.of());
            plugin.saveConfig();
            sender.sendMessage(ColorFormat.colorize("&6The chat visibility exceptions list has been cleared."));
        }

        return CommandResult.SUCCESS;
    }
}
