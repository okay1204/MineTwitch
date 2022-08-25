package me.okay.minetwitch.command.chatvisibility.exceptions;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;

import me.okay.minetwitch.CommandResult;
import me.okay.minetwitch.CustomSubcommand;
import me.okay.minetwitch.Minetwitch;
import me.okay.minetwitch.utils.ColorFormat;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.hover.content.Text;

public class ExceptionsList extends CustomSubcommand {
    private static final String BORDER_LINE = ChatColor.of("#9146FF") + "" + ChatColor.STRIKETHROUGH + "----------------------------------------------------";
    private static final int PER_PAGE = 10;
    private Minetwitch plugin;

    public ExceptionsList(Minetwitch plugin) {
        super(
            "list",
            "Lists players on the visibility exceptions list.",
            "minetwitch.chatvisibility.exceptions.list",
            "list [<page>]"
        );

        this.plugin = plugin;
    }

    @Override
    public CommandResult onRun(CommandSender sender, CustomSubcommand command, String label, String[] args) {
        int page;
        if (args.length < 1) {
            page = 1;
        }
        else {
            try {
                page = Integer.parseInt(args[0]);
            }
            catch (NumberFormatException e) {
                sender.sendMessage(ColorFormat.colorize("&cInvalid page number."));
                return CommandResult.SUCCESS;
            }

            if (page < 1) {
                page = 1;
            }
        }
        
        List<String> exceptions = plugin.getConfig().getStringList("share-chat.twitch-to-minecraft.exceptions");

        int maxPages = (int) Math.ceil(exceptions.size() / (double) PER_PAGE);
        if (maxPages == 0) {
            sender.sendMessage(ColorFormat.colorize("&cThere are no players in the chat visibility exception list."));
            return CommandResult.SUCCESS;
        }

        if (page > maxPages) {
            page = maxPages;
        }

        List<String> currentPage = exceptions.stream()
            .skip((page - 1) * PER_PAGE)
            .limit(PER_PAGE)
            .collect(Collectors.toList());

        TextComponent backArrow;
        if (page == 1) {
            backArrow = new TextComponent(ColorFormat.colorize("&7&l<< "));
        }
        else {
            backArrow = new TextComponent(ColorFormat.colorize("&6&l<< "));
            backArrow.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(ColorFormat.colorize("&6Go to page " + (page - 1)))));
            backArrow.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/minetwitch:minetwitch chatVisibility exceptions list " + (page - 1)));
        }

        TextComponent forwardArrow;
        if (page == maxPages) {
            forwardArrow = new TextComponent(ColorFormat.colorize("&7&l >>"));
        }
        else {
            forwardArrow = new TextComponent(ColorFormat.colorize("&6&l >>"));
            forwardArrow.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(ColorFormat.colorize("&6Go to page " + (page + 1)))));
            forwardArrow.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/minetwitch:minetwitch chatVisibility exceptions list " + (page + 1)));
        }

        TextComponent centerText = new TextComponent(ColorFormat.colorize("&6Chat Visibility Exception List: &e(Page " + page + " of " + maxPages + ")"));

        sender.sendMessage(BORDER_LINE);
        sender.spigot().sendMessage(backArrow, centerText, forwardArrow);
        for (String exception : currentPage) {
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

            if (uuid != null && !name.equals(uuid.toString())) {
                sender.sendMessage(ColorFormat.colorize("&e- " + name + " &7(" + uuid.toString() + ")"));
            }
            else {
                sender.sendMessage(ColorFormat.colorize("&e- " + name));
            }
        }
        sender.sendMessage(BORDER_LINE);

        return CommandResult.SUCCESS;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, CustomSubcommand command, String label, String[] args) {
        return List.of();
    }
}
